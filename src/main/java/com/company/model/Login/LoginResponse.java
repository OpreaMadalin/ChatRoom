package com.company.model.Login;

public class LoginResponse {

    private final String token;

    public LoginResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }
}
