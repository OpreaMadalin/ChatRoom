package com.company.model;

import java.util.ArrayList;

public class GetChatroomMessages {

    private final ArrayList<String> chatroomMessages;

    public GetChatroomMessages(ArrayList<String> chatroomMessages) {
        this.chatroomMessages = chatroomMessages;
    }

    public ArrayList<String> getChatroomMessages() {
        return chatroomMessages;
    }
}
