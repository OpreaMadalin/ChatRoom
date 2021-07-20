package com.company.model.deleteChatroom;

public class DeleteChatroomResponse {

    private final String chatroomName;

    public DeleteChatroomResponse(String chatroomName) {
        this.chatroomName = chatroomName;
    }

    public String getChatroomName() {
        return chatroomName;
    }
}
