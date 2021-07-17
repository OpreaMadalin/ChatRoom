package com.company.model.DeleteChatroom;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeleteChatroomRequestBody {
    private final String chatroomName;

    public DeleteChatroomRequestBody(@JsonProperty("chatroomName") String chatroomName) {
        this.chatroomName = chatroomName;

    }

    public String getChatroomName() {
        return this.chatroomName;
    }


}
