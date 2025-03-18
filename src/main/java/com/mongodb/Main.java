package com.mongodb;

import com.mongodb.config.MongoConfig;
import com.mongodb.model.Account;
import com.mongodb.repository.AccountRepository;
import com.mongodb.repository.OrderRepository;
import com.mongodb.service.DistributedTransactionService;
import com.mongodb.service.TransactionService;

public class Main {
    public static void main(String[] args) {
        // Initialize sample data
        AccountRepository accountRepository = new AccountRepository();
        accountRepository.save(new Account("A1", 1000));
        accountRepository.save(new Account("A2", 500));

        // Implement transaction
        TransactionService transactionService = new TransactionService(accountRepository,MongoConfig.getMongoClient());
        //transactionService.TransferMoney("A1", "A2", 200);

        // Implement distributed transactions
        OrderRepository orderRepository = new OrderRepository();
        accountRepository.save(new Account("A3", 2000));
        accountRepository.save(new Account("A4", 1000));
        try {
            DistributedTransactionService service = new DistributedTransactionService(accountRepository, new OrderRepository(), MongoConfig.getMongoClient());
            service.TransferMoney("A3", "A4", 500, "O1", "Laptop", 400);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }


    }
}