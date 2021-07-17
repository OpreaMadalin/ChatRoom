package com.company.model.PostChatroom;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PostChatroomRequestBody {

    private final String chatroomName;

    public PostChatroomRequestBody(@JsonProperty("chatroomName") String chatroomName) {
        this.chatroomName = chatroomName;

    }

    public String getChatroomName() {
        return this.chatroomName;
    }


}
