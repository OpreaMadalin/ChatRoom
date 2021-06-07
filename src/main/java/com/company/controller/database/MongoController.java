package com.company.controller.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Map;

public class MongoController {

    private static final String usersCollectionName = "users";
    private static final String chatRoomsCollectionName = "chatrooms";
    private final MongoClient client;
    private final String dbName;

    public MongoController(MongoCredentials mongoCredentials) {
        this.client = new MongoClient(new MongoClientURI(mongoCredentials.getConnectionURI()));
        this.dbName = mongoCredentials.getDbName();
    }

    public MongoController() {
        MongoCredentials envCreds = getEnvCreds();
        this.client = new MongoClient(new MongoClientURI(envCreds.getConnectionURI()));
        this.dbName = envCreds.getDbName();

    }

    private MongoCredentials getEnvCreds() {
        Map<String, String> env = System.getenv();
        String mongoUser = env.get("MONGO_USER");
        String mongoPassword = env.get("MONGO_PASSWORD");
        String mongoCluster = env.get("MONGO_CLUSTER");
        String mongoDbName = env.get("MONGO_DB_NAME");
        return new MongoCredentials(mongoUser, mongoPassword, mongoCluster, mongoDbName);
    }

    public MongoDatabase getDatabase() {
        return client.getDatabase(dbName);
    }

    public MongoCollection<Document> getUsersCollection() {
        return getDatabase().getCollection(usersCollectionName);
    }

    public Document getUserWithUsername(String username) {
        MongoCollection<Document> usersCollection = getUsersCollection();
        Bson bsonFilter = Filters.eq("username", username);
        return usersCollection.find(bsonFilter).first();
    }

    public void addUser(String username, String password) {
        Document doc = new Document();
        doc.append("username", username);
        doc.append("password", password);
        getUsersCollection().insertOne(doc);
    }

    public MongoCollection<Document> getChatRoomsCollection() {
        return getDatabase().getCollection(chatRoomsCollectionName);
    }


}
