package com.company.controller.tokens;

import java.util.Map;

public class TokenManager {
    private final static String tokenConnector = ".";
    private final String privateKey;
    private final String publicKey;
    private final String signature;

    public TokenManager(String privateKey, String publicKey, String signature) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.signature = signature;
    }

    public TokenManager() {
        Map<String, String> env = System.getenv();
        this.privateKey = env.get("AUTH_PRIVATE_KEY");
        this.publicKey = env.get("AUTH_PUBLIC_KEY");
        this.signature = env.get("AUTH_SIGNATURE");
    }

    public String generateToken(TokenClaims claims) {
        return sign(claims.toBase64());

    }

    public String sign(String str) {
        return encrypt(signature + tokenConnector + str);
    }

    public TokenClaims verifyToken(String str) {
        String decryptedToken = decrypt(str);
        if (decryptedToken.equals("")) {
            return null;
        }
        String[] tokenComponent = decryptedToken.split(tokenConnector);
        if (tokenComponent.length != 2) {
            return null;
        }
        if (!tokenComponent[0].equals(signature)) {
            return null;
        }

        return TokenClaims.fromBase64(tokenComponent[1]);
    }

    public String encrypt(String str) {
        return "encryptedwith" + publicKey + "shouldbedecryptedwith" + privateKey + ":" + str;
    }

    public String decrypt(String str) {
        return str.replace(
                "encryptedwith" + publicKey + "shouldbedecrypted" + privateKey + ":", "");
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getSignature() {
        return signature;
    }


}
