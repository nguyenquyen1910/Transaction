import mongoose, { connect } from "mongoose";

// Kết nối đến database
async function connectDatabase() {
  const conn = await mongoose.createConnection(
    "mongodb+srv://jrnguyen14:nHe1AqQMxPRUC8aU@transaction.vkh1w.mongodb.net/test?retryWrites=true&w=majority"
  );
  return conn;
}

// Định nghĩa Schema và Model
const userSchema = new mongoose.Schema({
  name: String,
  balance: Number,
});

// Schema cho Transaction History
const transactionSchema = new mongoose.Schema({
  from: { type: String, required: true },
  to: { type: String, required: true },
  amount: { type: Number, required: true },
  timestamp: { type: Date, default: Date.now },
  status: { type: String, enum: ["success", "failed"], default: "success" },
});

const User = mongoose.model("User", userSchema);

// Hàm tạo người dùng trong database
async function setupUsers() {
  const conn = await connectDatabase();
  const User = conn.model("User", userSchema);

  const userA = await User.findOne({ name: "User_A" });
  if (!userA) {
    const newUserA = new User({ name: "User_A", balance: 1000 });
    await newUserA.save();
    console.log("User_A has been created");
  }

  const userB = await User.findOne({ name: "User_B" });
  if (!userB) {
    const newUserB = new User({ name: "User_B", balance: 500 });
    await newUserB.save();
    console.log("User_B has been created");
  }

  await conn.close();
}

// Hàm thực hiện giao dịch tập trung
async function runCentralizedTransaction() {
  let conn = await connectDatabase();
  let session;
  const price = 50;
  const User = conn.model("User", userSchema);
  const Transaction = conn.model("Transaction", transactionSchema);

  try {
    // Bắt đầu session cho từng instance
    session = await conn.startSession();
    session.startTransaction();

    // Lấy userA và userB từ database
    const user1 = await User.findOne({ name: "User_A" }).session(session);
    const user2 = await User.findOne({ name: "User_B" }).session(session);

    // Kiểm tra số dư của userA
    if (!user1 || user1.balance < price) {
      throw new Error("User_A does not have enough balance");
    }

    // Thực hiện chuyển tiền từ userA sang userB
    user1.balance -= price;
    user2.balance += price;

    // Tạo bản ghi giao dịch
    const transaction = new Transaction({
      from: user1.name,
      to: user2.name,
      amount: price,
    });

    // Lưu lại thay đổi cho cả hai người dùng
    await user1.save({ session });
    await user2.save({ session });
    await transaction.save({ session });

    await session.commitTransaction();
    console.log(`Transaction successful! User_A sent ${price} to User_B.`);
  } catch (error) {
    console.log("Failed transaction:", error.message);
    if (session && session.inTransaction()) {
      const failedTransaction = new Transaction({
        from: "User_A",
        to: "User_B",
        amount: price,
        status: "failed",
      });
      await failedTransaction
        .save()
        .catch((err) => console.log("Failed to log transaction:", err.message));
      await session.abortTransaction();
    }
  } finally {
    if (session) session.endSession();
    if (conn) await conn.close();
  }
}

// Chạy chương trình
console.log("Starting setup data...");
await setupUsers();
console.log("Starting transaction...");
await runCentralizedTransaction();
console.log("Successful transaction");
