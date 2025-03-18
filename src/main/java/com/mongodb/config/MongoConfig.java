package com.mongodb.config;
import com.mongodb.client.*;
import java.io.IOException;
import java.util.Properties;

public class MongoConfig {
    private static MongoClient mongoClient;
    private static MongoDatabase mongoDatabase;

    static {
        Properties properties = new Properties();
        try {
            properties.load(MongoConfig.class.getClassLoader().getResourceAsStream("application.properties"));
            String uri = properties.getProperty("mongodb.uri");
            String database = properties.getProperty("mongodb.database");
            mongoClient = MongoClients.create(uri);
            mongoDatabase = mongoClient.getDatabase(database);
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
}
