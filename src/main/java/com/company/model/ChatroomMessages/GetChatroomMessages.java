package com.company.model.ChatroomMessages;

import org.bson.Document;

import java.util.ArrayList;

public class GetChatroomMessages {

    private final ArrayList<Document> chatroomMessages;

    public GetChatroomMessages(ArrayList<Document> chatroomMessages) {
        this.chatroomMessages = chatroomMessages;
    }

    public ArrayList<Document> getChatroomMessages() {
        return chatroomMessages;
    }
}
