package com.mongodb.repository;

import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import com.mongodb.config.MongoConfig;
import com.mongodb.model.Order;
import org.bson.Document;

public class OrderRepository {
    private final MongoCollection<Document> orders;

    public OrderRepository() {
        this.orders = MongoConfig.getMongoDatabase().getCollection("orders");
    }

    public void save(Order order) {
        Document doc = new Document("orderId", order.getOrderId())
                .append("amount", order.getAmount());
        orders.insertOne(doc);
    }

    public void updateAmount(ClientSession session, String orderId, double amount) {
        orders.updateOne(session,
                new Document("orderId", orderId),
                new Document("$inc", new Document("amount", amount)));
    }

}
