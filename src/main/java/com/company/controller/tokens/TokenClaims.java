package com.company.controller.tokens;

import java.util.Arrays;
import java.util.Base64;

public class TokenClaims {

    private final static String strConnector = ".";
    private final String username;
   // private final int expiryTimestamp;


    public TokenClaims(String username) {
        this.username = username;
       // this.expiryTimestamp = expiryTimestamp;
    }

    public String toString() {
        return username + strConnector;
    }

    public String toBase64() {
        return Base64.getEncoder().encodeToString(this.toString().getBytes());
    }

    public static TokenClaims fromBase64(String encodedClaims) {
        byte[] claimsStr = Base64.getDecoder().decode(encodedClaims);
        String decodedString = new String(claimsStr);
        return TokenClaims.fromString(decodedString);
    }

    public static TokenClaims fromString(String claimsStr) {
        String[] strComponents = claimsStr.split("\\.");
        int expiryTimeStamp = Integer.parseInt(strComponents[1]);
        return new TokenClaims(strComponents[0]);
    }

    public static String getStrConnector() {
        return strConnector;
    }

    public  String getUsername() {
        return username;
    }

}
