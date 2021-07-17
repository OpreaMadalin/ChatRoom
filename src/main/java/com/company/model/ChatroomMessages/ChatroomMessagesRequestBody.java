package com.company.model.ChatroomMessages;


import com.fasterxml.jackson.annotation.JsonProperty;

public class ChatroomMessagesRequestBody {

    private final String chatroomName;
    private final String messages;

    public ChatroomMessagesRequestBody(@JsonProperty("chatroomName") String chatroomName,
                                       @JsonProperty("messages") String messages) {
        this.chatroomName = chatroomName;
        this.messages = messages;
    }

    public String getChatroomName() {
        return chatroomName;
    }

    public String getMessages() {
        return messages;
    }

}
