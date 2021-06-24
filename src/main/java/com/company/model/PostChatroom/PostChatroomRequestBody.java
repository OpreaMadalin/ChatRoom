package com.company.model.PostChatroom;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PostChatroomRequestBody {

    private final String name;

    public PostChatroomRequestBody(@JsonProperty("name") String name) {
        this.name = name;

    }

    public String getName() {
        return this.name;
    }


}
