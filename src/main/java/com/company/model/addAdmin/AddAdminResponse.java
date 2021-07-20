package com.company.model.addAdmin;

public class AddAdminResponse {

    private final String insertedId;

    public AddAdminResponse(String insertedId) {
        this.insertedId = insertedId;
    }

    public String getInsertedId() {
        return insertedId;
    }
}
