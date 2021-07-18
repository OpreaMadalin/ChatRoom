package com.company.model.ChatroomMessages;


import com.fasterxml.jackson.annotation.JsonProperty;

public class ChatroomMessagesRequestBody {

    private final String chatroomName;
    private final String messages;
    private final String password;

    public ChatroomMessagesRequestBody(@JsonProperty("chatroomName") String chatroomName,
                                       @JsonProperty("messages") String messages,
                                       @JsonProperty("password") String password) {
        this.chatroomName = chatroomName;
        this.messages = messages;
        this.password = password;
    }

    public String getChatroomName() {
        return chatroomName;
    }

    public String getMessages() {
        return messages;
    }

    public String getPassword() {
        return password;
    }
}
