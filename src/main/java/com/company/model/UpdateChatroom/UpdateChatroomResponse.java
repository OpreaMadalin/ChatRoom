package com.company.model.UpdateChatroom;

public class UpdateChatroomResponse {

    private final String chatroomName;

    public UpdateChatroomResponse(String chatroomName) {
        this.chatroomName = chatroomName;
    }

    public String getChatroomName() {
        return chatroomName;
    }

}
