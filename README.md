<div align="center">

# 🚀 MongoDB Transaction Demo

[![Java](https://img.shields.io/badge/Java-23-orange.svg?style=for-the-badge&logo=java)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-3.6+-blue.svg?style=for-the-badge&logo=apache-maven)](https://maven.apache.org/)
[![MongoDB](https://img.shields.io/badge/MongoDB-4.0+-green.svg?style=for-the-badge&logo=mongodb)](https://www.mongodb.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg?style=for-the-badge)](LICENSE)

> **A Java application demonstrating MongoDB transaction capabilities with centralized and distributed transaction patterns.**

[![MongoDB Transactions](https://img.shields.io/badge/MongoDB-Transactions-00ED64?style=for-the-badge&logo=mongodb&logoColor=white)](https://docs.mongodb.com/manual/core/transactions/)
[![ACID Compliance](https://img.shields.io/badge/ACID-Compliant-FF6B6B?style=for-the-badge)](https://en.wikipedia.org/wiki/ACID)

---

</div>

## ✨ Features

- 🔄 **Centralized Transactions**: Simple money transfer between accounts
- 🌐 **Distributed Transactions**: Complex multi-collection operations
- 🛡️ **ACID Compliance**: Full transaction support with rollback
- 🗄️ **MongoDB Integration**: Native driver with session management
- ⚠️ **Error Handling**: Robust exception handling

## 🚀 Quick Start

### Prerequisites

- Java 23+
- Maven 3.6+
- MongoDB 4.0+

### Installation

```bash
# Clone and build
git clone <repository-url>
cd transaction
mvn clean compile

# Configure MongoDB in application.properties
# Run the application
mvn exec:java -Dexec.mainClass="com.mongodb.Main"
```

## 💡 Usage Examples

### Centralized Transaction

```java
AccountRepository accountRepository = new AccountRepository();
accountRepository.save(new Account("A1", 1000));
accountRepository.save(new Account("A2", 500));

TransactionService service = new TransactionService(accountRepository, MongoConfig.getMongoClient());
service.TransferMoney("A1", "A2", 200);
```

### Distributed Transaction

```java
DistributedTransactionService service = new DistributedTransactionService(
    accountRepository, orderRepository, MongoConfig.getMongoClient()
);
service.TransferMoney("A3", "A4", 500, "O1", "Laptop", 400);
```

## 🏗️ Project Structure

```
src/main/java/com/mongodb/
├── config/MongoConfig.java              # MongoDB configuration
├── model/
│   ├── Account.java                     # Account entity
│   └── Order.java                       # Order entity
├── repository/
│   ├── AccountRepository.java           # Account data access
│   └── OrderRepository.java             # Order data access
├── service/
│   ├── TransactionService.java          # Centralized transactions
│   └── DistributedTransactionService.java # Distributed transactions
└── Main.java                            # Application entry point
```

## 🔧 Configuration

Edit `src/main/resources/application.properties`:

```properties
mongodb.uri=mongodb+srv://username:password@cluster.mongodb.net/?retryWrites=true&w=majority
mongodb.database=transaction
mongodb.uri_db1=mongodb+srv://username:password@cluster.mongodb.net/usersDB?retryWrites=true&w=majority
mongodb.uri_db2=mongodb+srv://username:password@cluster.mongodb.net/ordersDB?retryWrites=true&w=majority
```

## 📊 Architecture

<div align="center">

| Transaction Type         | Description                 | Use Case               |
| ------------------------ | --------------------------- | ---------------------- |
| **🔄 Centralized** | Single database operations  | Simple money transfers |
| **🌐 Distributed** | Multi-collection operations | Complex business logic |

</div>

## 🧪 Testing

```bash
mvn test
```

## 📚 API Reference

| Service                           | Method                                                          | Description                                 |
| --------------------------------- | --------------------------------------------------------------- | ------------------------------------------- |
| `TransactionService`            | `TransferMoney(from, to, amount)`                             | Centralized money transfer                  |
| `DistributedTransactionService` | `TransferMoney(from, to, amount, orderId, item, orderAmount)` | Distributed transaction with order creation |

## 🤝 Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

- 🐛 [Report Bug](https://github.com/your-username/transaction/issues/new)
- 💡 [Request Feature](https://github.com/your-username/transaction/issues/new)
- 📚 [MongoDB Docs](https://docs.mongodb.com/manual/core/transactions/)

---

<div align="center">

**Made with ❤️ by Quyen**

[![LinkedIn](https://img.shields.io/badge/LinkedIn-Connect-blue?style=for-the-badge&logo=linkedin)](https://linkedin.com/in/your-profile)
[![GitHub](https://img.shields.io/badge/GitHub-Follow-black?style=for-the-badge&logo=github)](https://github.com/your-username)

</div>
