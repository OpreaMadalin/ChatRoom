package com.company.model.deleteChatroom;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeleteChatroomRequestBody {
    private final String chatroomName;
    private final String password;

    public DeleteChatroomRequestBody(@JsonProperty("chatroomName") String chatroomName,
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
