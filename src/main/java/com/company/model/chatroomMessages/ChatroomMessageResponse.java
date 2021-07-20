package com.company.model.chatroomMessages;

import org.bson.Document;

import java.util.ArrayList;

public class ChatroomMessageResponse {

    private final ArrayList<Document> chatroomMessages;

    public ChatroomMessageResponse(ArrayList<Document> chatroomMessages) {
        this.chatroomMessages = chatroomMessages;
    }

    public ArrayList<Document> getChatroomMessages() {
        return chatroomMessages;
    }
}
