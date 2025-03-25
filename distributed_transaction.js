import mongoose from "mongoose";

// Kết nối đến database
async function connectDatabase() {
  const conn1 = await mongoose.createConnection(
    "mongodb+srv://jrnguyen14:nHe1AqQMxPRUC8aU@transaction.vkh1w.mongodb.net/usersDB1?retryWrites=true&w=majority"
  );

  const conn2 = await mongoose.createConnection(
    "mongodb+srv://jrnguyen14:nHe1AqQMxPRUC8aU@transaction.vkh1w.mongodb.net/usersDB2?retryWrites=true&w=majority"
  );

  return { conn1, conn2 };
}

// Định nghĩa Schema và Model
const UserSchema = new mongoose.Schema({
  name: String,
  balance: Number,
});

// Schema cho Transaction History
const TransactionHistorySchema = new mongoose.Schema({
  fromUserId: String,
  toUserId: String,
  amount: Number,
  status: {
    type: String,
    enum: ["success", "failed"],
    default: "success",
  },
  timestamp: {
    type: Date,
    default: Date.now,
  },
  description: String,
});

// Thiết lập dữ liệu ban đầu
async function setUpData() {
  const { conn1, conn2 } = await connectDatabase();
  const UserA = conn1.model("User", UserSchema);
  const UserB = conn2.model("User", UserSchema);

  // Xóa dữ liệu cũ và thêm dữ liệu mẫu trong usersDB1 và usersDB2
  await UserA.deleteMany({});
  await UserB.deleteMany({});

  const userA = await UserA.findOne({ name: "User_A" });
  if (!userA) {
    const newUserA = new UserA({ name: "User_A", balance: 1000 });
    await newUserA.save();
    console.log("User_A has been created");
  }
  const userB = await UserB.findOne({ name: "User_B" });
  if (!userB) {
    const newUserB = new UserB({ name: "User_B", balance: 500 });
    await newUserB.save();
    console.log("User_B has been created");
  }
  console.log("Successfully created user");

  await conn1.close();
  await conn2.close();
}

// Hàm thực hiện giao dịch phân tán
async function runDistributedTransaction() {
  let conn1, conn2;
  let session1, session2;
  const price = 50;
  const TransactionHistory = conn1.model(
    "Transaction",
    TransactionHistorySchema
  );
  try {
    // Kết nối tới hai database
    ({ conn1, conn2 } = await connectDatabase());
    const User1 = conn1.model("User", UserSchema);
    const User2 = conn2.model("User", UserSchema);

    // Bắt đầu session cho từng instance
    session1 = await conn1.startSession();
    session2 = await conn2.startSession();
    session1.startTransaction();
    session2.startTransaction();

    // Lấy userA và userB từ 2 database
    const userA = await User1.findOne({ name: "User_A" }).session(session1);
    const userB = await User2.findOne({ name: "User_B" }).session(session2);

    // Kiểm tra số dư của user1 và user2
    if (!userA || userA.balance < price)
      throw new Error("User1 does not have enough balance");
    if (!userB) throw new Error("User2 does not exist");

    // Thực hiện chuyển tiền từ userA qua userB
    // Trừ tiền từ tài khoản người gửi trong userDB1
    userA.balance -= price;
    await userA.save({ session: session1 });

    // Cộng tiền vào tài khoản người nhận trong userDB2
    userB.balance += price;
    await userB.save({ session: session2 });

    // Tạo bản ghi giao dịch
    const transaction = new TransactionHistory({
      from: userA.name,
      to: userB.name,
      amount: price,
      status: "Successful",
    });
    await transaction.save({ session1 });

    // Commit cả hai giao dịch nếu thành công
    await session1.commitTransaction();
    await session2.commitTransaction();
    console.log(`Successfully transaction! Price: ${price}`);
  } catch (error) {
    console.log("Failed transaction", error.message);
    if (
      (session1 && session1.inTransaction()) ||
      (session2 && session2.inTransaction())
    ) {
      const failedTransaction = new TransactionHistory({
        from: userA.name,
        to: userB.name,
        amount: price,
        status: "Failed",
      });
      await failedTransaction
        .save()
        .catch((err) => console.log("Failed to log transaction:", err.message));
      await session1.abortTransaction();
      await session2.abortTransaction();
    }
  } finally {
    if (session1) session1.endSession();
    if (session2) session2.endSession();
    if (conn1) await conn1.close();
    if (conn2) await conn2.close();
  }
}

// Chạy chương trình
await setUpData();
await runDistributedTransaction();
console.log("Transaction complete.");
