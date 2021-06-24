package com.company.model.DeleteChatroom;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeleteChatroomRequestBody {
    private final String name;

    public DeleteChatroomRequestBody(@JsonProperty("name") String name) {
        this.name = name;

    }

    public String getName() {
        return this.name;
    }


}
