package org.example.healthcaremanagementsystem.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import io.github.cdimascio.dotenv.Dotenv;

public class MongoDBConnection {
    private static final Dotenv dotenv = Dotenv.load();
    private static MongoClient mongoClient;
    private static MongoDatabase database;
    private static final String CONNECTION_STRING = dotenv.get("MONGO_CONNECTION_STRING");
    private static final String DATABASE_NAME = dotenv.get("MONGO_DATABASE_NAME");

    private MongoDBConnection() {
    }

    public static MongoDatabase getDatabase() {
        if (mongoClient == null) {
            try {
                mongoClient = MongoClients.create(CONNECTION_STRING);
                database = mongoClient.getDatabase(DATABASE_NAME);
                System.out.println("Connected to MongoDB successfully.");
            } catch (Exception e) {
                System.err.println("Failed to connect to MongoDB: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return database;
    }

    public static void close() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
            database = null;
        }
    }
}
