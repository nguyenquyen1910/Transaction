package com.mongodb.repository;

import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import com.mongodb.model.Account;
import com.mongodb.config.MongoConfig;
import org.bson.Document;

public class AccountRepository {
    private final MongoCollection<Document> accounts;

    public AccountRepository() {
        this.accounts = MongoConfig.getMongoDatabase().getCollection("accounts");
    }

    public void save(Account account) {
        Document doc = new Document("accountId", account.getAccountId())
                .append("balance", account.getBalance());
        accounts.insertOne(doc);
    }

    public void updateBalance(ClientSession session, String accountId, double amount) {
        accounts.updateOne(session,
                new Document("accountId", accountId),
                new Document("$inc", new Document("balance", amount)));
    }
}
