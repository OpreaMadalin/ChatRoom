package com.company.controller.databaseController;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.*;

import static com.mongodb.client.model.Updates.*;

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
        MongoCollection<Document> chatRoomsCollection = getChatroomsCollection();
        Bson bsonFilter = Filters.eq("chatroomName", chatroomName);
        return chatRoomsCollection.find(bsonFilter).first();
    }

    public ArrayList<Document> getChatroomWithName(String chatroomName) {
        MongoCollection<Document> chatRoomsCollection = getChatroomsCollection();
        MongoCursor<Document> cursor = chatRoomsCollection.find(Filters.eq("chatroomName", chatroomName))
                .cursor();

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

        MongoCollection<Document> chatroomsCollections = getChatroomsCollection();
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

    public void addChatroom(String chatroomName, String password, String creatorName) {

        ArrayList<String> adminUserNames = new ArrayList<>();
        adminUserNames.add(creatorName);

        Document doc = new Document();
        doc.append("chatroomName", chatroomName);
        doc.append("password", password);
        doc.append("admins", adminUserNames);
        doc.append("bannedUsers", new ArrayList<String>());
        getChatroomsCollection().insertOne(doc);
    }

    public void addChatroom(String chatroomName, String creatorName) {

        ArrayList<String> adminUserNames = new ArrayList<>();
        adminUserNames.add(creatorName);

        Document doc = new Document();
        doc.append("chatroomName", chatroomName);
        doc.append("admins", adminUserNames);
        doc.append("bannedUsers", new ArrayList<String>());
        getChatroomsCollection().insertOne(doc);
    }

    public boolean checkPasswordFieldExistInChatroom(String chatroomName) {

        MongoCollection<Document> chatRoomsCollection = getChatroomsCollection();
        Bson bsonFilter = Filters.eq("chatroomName", chatroomName);

        try (MongoCursor<Document> cursor = chatRoomsCollection.find(bsonFilter).cursor()) {

            while (cursor.hasNext()) {
                Document currentDoc = cursor.next();
                boolean password;
                password = currentDoc.containsKey("password");
                if (password) {
                    return true;
                }
            }
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void deleteChatroom(String chatroomName) {
        Document doc = new Document();
        doc.append("chatroomName", chatroomName);
        getChatroomsCollection().deleteOne(doc);
    }

    public void updateChatroomName(String chatroomName, String newChatroomName) {

        MongoCollection<Document> chatRoomsCollection = getChatroomsCollection();
        Bson bsonFilter = Filters.eq("chatroomName", chatroomName);
        Document doc = new Document();
        doc.append("chatroomName", newChatroomName);
        Bson updateOperation = set("chatroomName", newChatroomName);
        chatRoomsCollection.updateOne(bsonFilter, updateOperation);
    }

    public void updateChatroomPassword(String chatroomName, String newChatroomPassword) {

        MongoCollection<Document> chatRoomsCollection = getChatroomsCollection();
        Bson bsonFilter = Filters.eq("chatroomName", chatroomName);
        Document doc = new Document();
        doc.append("password", newChatroomPassword);
        Bson updateOperation = set("password", newChatroomPassword);
        chatRoomsCollection.updateOne(bsonFilter, updateOperation);
    }

    public void deleteChatroomPassword(String chatroomName) {

        MongoCollection<Document> chatRoomsCollection = getChatroomsCollection();
        Bson bsonFilter = Filters.eq("chatroomName", chatroomName);
        Bson updateOperation = unset("password");
        chatRoomsCollection.updateOne(bsonFilter, updateOperation);
    }

    public void addMessage(String chatroomName, String message, String username) {

        MongoCollection<Document> chatRoomsCollection = getChatroomsCollection();
        Bson bsonFilter = Filters.eq("chatroomName", chatroomName);

        Document messageDoc = new Document("username", username)
                .append("message", message)
                .append("timestamp", new Date().toString());

        Bson updateOperation = push("messages", messageDoc);
        chatRoomsCollection.updateOne(bsonFilter, updateOperation);
    }

    public MongoCollection<Document> getChatroomsCollection() {
        return getDatabase().getCollection(chatRoomsCollectionName);
    }

    public boolean isAdmin(String username, String chatroom) {

        MongoCollection<Document> chatRoomsCollection = getChatroomsCollection();
        Bson bsonFilter = Filters.eq("chatroomName", chatroom);

        try (MongoCursor<Document> cursor = chatRoomsCollection.find(bsonFilter).cursor()) {
            while (cursor.hasNext()) {
                Document currentDoc = cursor.next();
                List<String> admins;
                admins = currentDoc.getList("admins", String.class, new ArrayList<>());
                for (String admin : admins) {
                    if (admin.equals(username)) {
                        return true;
                    }
                }
            }
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isBanned(String username, String chatroom) {

        MongoCollection<Document> chatRoomsCollection = getChatroomsCollection();
        Bson bsonFilter = Filters.eq("chatroomName", chatroom);

        try (MongoCursor<Document> cursor = chatRoomsCollection.find(bsonFilter).cursor()) {
            while (cursor.hasNext()) {
                Document currentDoc = cursor.next();
                List<String> bannedUsers;
                bannedUsers = currentDoc.getList("bannedUsers", String.class, new ArrayList<>());

                for (String bannedUser : bannedUsers) {
                    if (bannedUser.equals(username)) {
                        return true;
                    }
                }
            }
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addAdmin(String chatroomName, String username) {

        MongoCollection<Document> chatroomsCollection = getChatroomsCollection();
        Bson bsonFilter = Filters.eq("chatroomName", chatroomName);

        Bson updateOperation = push("admins", username);
        chatroomsCollection.updateOne(bsonFilter, updateOperation);
    }

    public void addBannedUser(String chatroomName, String username) {

        MongoCollection<Document> chatroomsCollection = getChatroomsCollection();
        Bson bsonFilter = Filters.eq("chatroomName", chatroomName);

        Bson updateOperation = push("bannedUsers", username);
        chatroomsCollection.updateOne(bsonFilter, updateOperation);
    }

}
