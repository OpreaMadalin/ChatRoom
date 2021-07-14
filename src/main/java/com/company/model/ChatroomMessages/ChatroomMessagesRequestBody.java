package com.company.model.ChatroomMessages;


import com.fasterxml.jackson.annotation.JsonProperty;

public class ChatroomMessagesRequestBody {

    private final String name;
    private final String messages;

    public ChatroomMessagesRequestBody(@JsonProperty("name") String name,
                                       @JsonProperty("messages") String messages) {
        this.name = name;
        this.messages = messages;
    }

    public String getName() {
        return name;
    }

    public String getMessages() {
        return messages;
    }

}
