package com.mongodb.config;
import com.mongodb.client.*;
import java.io.IOException;
import java.util.Properties;

public class MongoConfig {
    // Cấu hình cho giao dịch tập trung
    private static MongoClient mongoClient;
    private static MongoDatabase mongoDatabase;

    // Cấu hình cho giao dịch phân tán
    private static MongoClient mongoClient1;
    private static MongoClient mongoClient2;
    private static MongoDatabase mongoDatabase1;
    private static MongoDatabase mongoDatabase2;


    static {
        Properties properties = new Properties();
        try {
            properties.load(MongoConfig.class.getClassLoader().getResourceAsStream("application.properties"));
            String uri = properties.getProperty("mongodb.uri");
            String uri1 = properties.getProperty("mongodb.uri_db1");
            String uri2 = properties.getProperty("mongodb.uri_db2");
            String database = properties.getProperty("mongodb.database");
            String database1 = properties.getProperty("mongodb.database1");
            String database2 = properties.getProperty("mongodb.database2");

            // Kết nối với database cho hệ thống tập trung
            mongoClient = MongoClients.create(uri);
            mongoDatabase = mongoClient.getDatabase(database);

            // Kết nối với database thứ nhất (usersDB)
            mongoClient1 = MongoClients.create(uri1);
            mongoDatabase1 = mongoClient1.getDatabase(database1);

            // Kết nối với database thứ hai (ordersDB)
            mongoClient2 = MongoClients.create(uri2);
            mongoDatabase2 = mongoClient2.getDatabase(database2);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load MongoDB configuration", e);
        }
    }

    public static MongoClient getMongoClient() {
        return mongoClient;
    }

    public static MongoDatabase getMongoDatabase() {
        return mongoDatabase;
    }

    public static void setMongoClient(MongoClient mongoClient) {
        MongoConfig.mongoClient = mongoClient;
    }

    public static void setMongoDatabase(MongoDatabase mongoDatabase) {
        MongoConfig.mongoDatabase = mongoDatabase;
    }

    public static MongoClient getMongoClient1() {
        return mongoClient1;
    }

    public static void setMongoClient1(MongoClient mongoClient1) {
        MongoConfig.mongoClient1 = mongoClient1;
    }

    public static MongoClient getMongoClient2() {
        return mongoClient2;
    }

    public static void setMongoClient2(MongoClient mongoClient2) {
        MongoConfig.mongoClient2 = mongoClient2;
    }

    public static MongoDatabase getMongoDatabase1() {
        return mongoDatabase1;
    }

    public static void setMongoDatabase1(MongoDatabase mongoDatabase1) {
        MongoConfig.mongoDatabase1 = mongoDatabase1;
    }

    public static MongoDatabase getMongoDatabase2() {
        return mongoDatabase2;
    }

    public static void setMongoDatabase2(MongoDatabase mongoDatabase2) {
        MongoConfig.mongoDatabase2 = mongoDatabase2;
    }
}
