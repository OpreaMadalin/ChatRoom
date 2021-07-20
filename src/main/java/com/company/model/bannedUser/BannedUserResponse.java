package com.company.model.bannedUser;

public class BannedUserResponse {

    private final String insertedId;


    public BannedUserResponse(String insertedId) {
        this.insertedId = insertedId;
    }

    public String getInsertedId() {
        return insertedId;
    }
}
