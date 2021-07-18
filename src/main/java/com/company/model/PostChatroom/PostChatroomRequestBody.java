package com.company.model.PostChatroom;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PostChatroomRequestBody {

    private final String chatroomName;
    private final String password;

    public PostChatroomRequestBody(@JsonProperty("chatroomName") String chatroomName,
                                   @JsonProperty("password") String password) {
        this.chatroomName = chatroomName;
        this.password = password;

    }

    public String getChatroomName() {
        return this.chatroomName;
    }

    public String getPassword() {
        return password;
    }
}
