package com.mongodb.service;

import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.mongodb.config.MongoConfig;
import com.mongodb.model.Order;
import com.mongodb.repository.AccountRepository;
import com.mongodb.repository.OrderRepository;

public class DistributedTransactionService {
    private final AccountRepository accountRepository;
    private final OrderRepository orderRepository;
    private final MongoClient mongoClient;


    public DistributedTransactionService(AccountRepository accountRepository, OrderRepository orderRepository, MongoClient mongoClient) {
        this.accountRepository = accountRepository;
        this.orderRepository = orderRepository;
        this.mongoClient = MongoConfig.getMongoClient();
    }

    public void TransferMoney(String fromAccountId, String toAccountId, double amount, String orderId, String item, double orderAmount) {
        try(ClientSession clientSession = mongoClient.startSession()) {
            clientSession.startTransaction();
            try {
                // Update account balance
                accountRepository.updateBalance(clientSession, fromAccountId, -amount);
                accountRepository.updateBalance(clientSession, toAccountId, amount);

                // Create new order
                orderRepository.save(new Order(orderId, item, orderAmount));

                // Committed to Distributed Trading
                clientSession.commitTransaction();
                System.out.println("Distributed transaction completed successfully!");
            } catch (Exception e) {
                clientSession.abortTransaction();
                throw new RuntimeException("Transaction failed: " + e.getMessage());
            }
        }
    }
}
