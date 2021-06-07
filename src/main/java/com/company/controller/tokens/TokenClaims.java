package com.company.controller.tokens;

import java.util.Base64;

public class TokenClaims {

    private final static String strConnector = ":::";

    private final String username;
    private final int expiryTimestamp;

    public TokenClaims(String username, int expiryTimestamp) {
        this.username = username;
        this.expiryTimestamp = expiryTimestamp;
    }

    public String toString() {
        return username + strConnector + expiryTimestamp;
    }

    public String ToBase64() {
        return Base64.getEncoder().encodeToString(this.toString().getBytes());
    }

}
