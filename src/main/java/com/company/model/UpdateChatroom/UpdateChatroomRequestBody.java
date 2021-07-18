package com.company.model.UpdateChatroom;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateChatroomRequestBody {

    private final String chatroomName;
    private final String newChatroomName;


    public UpdateChatroomRequestBody(@JsonProperty("chatroomName") String chatroomName,
                                     @JsonProperty("newChatroomName") String newChatroomName) {
        this.chatroomName = chatroomName;
        this.newChatroomName = newChatroomName;

    }

    public String getChatroomName() {
        return this.chatroomName;
    }

    public String getNewChatroomName() {
        return this.newChatroomName;
    }
}
