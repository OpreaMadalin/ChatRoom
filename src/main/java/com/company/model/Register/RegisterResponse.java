package com.company.model.Register;

public class RegisterResponse {

    private final String registeredID;

    public RegisterResponse(String registeredID) {
        this.registeredID = registeredID;
    }

    public String getRegisteredID() {
        return this.registeredID;
    }
}
