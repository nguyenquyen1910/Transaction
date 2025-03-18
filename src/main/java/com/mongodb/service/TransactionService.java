package com.mongodb.service;

import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.mongodb.config.MongoConfig;
import com.mongodb.repository.AccountRepository;

public class TransactionService {
    private final AccountRepository accountRepository;
    private final MongoClient mongoClient;


    public TransactionService(AccountRepository accountRepository, MongoClient mongoClient) {
        this.accountRepository = new AccountRepository();
        this.mongoClient = MongoConfig.getMongoClient();
    }

    public void TransferMoney(String fromAccountId, String toAccountId, double amount) {
        try(ClientSession clientSession = mongoClient.startSession()) {
            clientSession.startTransaction();
            try {
                // Update account balance
                accountRepository.updateBalance(clientSession, fromAccountId, -amount);
                accountRepository.updateBalance(clientSession, toAccountId, amount);
                // Committed to Distributed Trading
                clientSession.commitTransaction();
                System.out.println("Centralized transaction completed successfully!");
            } catch (Exception e) {
                clientSession.abortTransaction();
                throw new RuntimeException("Transaction failed: " + e.getMessage());
            }
        }
    }
}
