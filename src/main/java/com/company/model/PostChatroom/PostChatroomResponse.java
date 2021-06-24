package com.company.model.PostChatroom;


public class PostChatroomResponse {

    private final String insertedId;

    public PostChatroomResponse(String insertedId) {
        this.insertedId = insertedId;
    }

    public String getInsertedId() {
        return insertedId;
    }

}
