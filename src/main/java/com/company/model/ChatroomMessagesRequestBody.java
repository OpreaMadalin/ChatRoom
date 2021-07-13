package com.company.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChatroomMessagesRequestBody {

    private final String name;
    private final String messages;

    public ChatroomMessagesRequestBody(@JsonProperty("name") String name, String messages) {
        this.name = name;
        this.messages = messages;
    }

    public String getName() {
        return this.name;
    }

    public String getMessages() {
        return messages;
    }
}
