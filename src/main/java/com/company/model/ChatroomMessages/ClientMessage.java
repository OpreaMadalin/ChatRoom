package com.company.model.ChatroomMessages;

public class ClientMessage {

    private final String chatroomName;
    private final Message message;


    public ClientMessage(String chatroomName, Message message) {
        this.chatroomName = chatroomName;
        this.message = message;
    }

    public String getChatroomName() {
        return chatroomName;
    }

    public Message getMessage() {
        return message;
    }
}
