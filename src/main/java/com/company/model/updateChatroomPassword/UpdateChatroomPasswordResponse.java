package com.company.model.updateChatroomPassword;

public class UpdateChatroomPasswordResponse {

    private final String chatroomName;

    public UpdateChatroomPasswordResponse(String chatroomName) {
        this.chatroomName = chatroomName;
    }

    public String getChatroomName() {
        return chatroomName;
    }

}
