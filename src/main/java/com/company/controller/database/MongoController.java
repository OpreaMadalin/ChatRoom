package com.company.controller.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.NoSuchElementException;

import static com.mongodb.client.model.Updates.push;

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

    public Document getChatRoomWithName(String chatroomName) {
        MongoCollection<Document> chatRoomsCollection = getChatRoomsCollection();
        Bson bsonFilter = Filters.eq("chatroomName", chatroomName);
        return chatRoomsCollection.find(bsonFilter).first();
    }

    public ArrayList<Document> getChatroomWithName(String chatroomName) {
        MongoCollection<Document> chatRoomsCollection = getChatRoomsCollection();
        MongoCursor<Document> cursor = chatRoomsCollection.find(Filters.eq("chatroomName", chatroomName)).cursor();
        ArrayList<Document> result = new ArrayList<>();
        try {
            while (cursor.hasNext()) {
                Document currentDoc = cursor.next();
                result.add(currentDoc);
            }
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return result;
    }

    public ArrayList<Document> getChatrooms() {
        MongoCollection<Document> chatroomsCollections = getChatRoomsCollection();
        MongoCursor<Document> cursor = chatroomsCollections.find().cursor();
        ArrayList<Document> result = new ArrayList<>();

        try {
            while (cursor.hasNext()) {
                Document currentDoc = cursor.next();
                result.add(currentDoc);
            }
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return result;
    }

    public void addChatroom(String chatroomName) {
        Document doc = new Document();
        doc.append("chatroomName", chatroomName);
        getChatRoomsCollection().insertOne(doc);
    }

    public void deleteChatroom(String chatroomName) {
        Document doc = new Document();
        doc.append("chatroomName", chatroomName);
        getChatRoomsCollection().deleteOne(doc);
    }

    public void addMessage(String chatroomName, String message, String username) {

        MongoCollection<Document> chatRoomsCollection = getChatRoomsCollection();
        Bson bsonFilter = Filters.eq("chatroomName", chatroomName);

        Document messageDoc = new Document("username", username)
                .append("message", message)
                .append("timestamp", new Date().toString());

        Bson updateOperation = push("messages", messageDoc);
        chatRoomsCollection.updateOne(bsonFilter, updateOperation);

    }

    public MongoCollection<Document> getChatRoomsCollection() {
        return getDatabase().getCollection(chatRoomsCollectionName);
    }


}
