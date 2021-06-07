package com.company.controller.tokens;

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

    public String sign(String str) {
        return encrypt(signature + tokenConnector + str);
    }

    public boolean verify(String str) {
        String decryptedToken = decrypt(str);
        if (decryptedToken.equals("")) {
            return false;
        }
        String[] tokenComponent = decryptedToken.split(tokenConnector);
        if (tokenComponent.length != 2) {
            return false;
        }
        if (!tokenComponent[0].equals(signature)) {
            return false;
        }
        //in token components[1]
        return true;
    }

    public String encrypt(String str) {
        return "encryptedwith" + publicKey + ";" + str;
    }

    public String decrypt(String str) {
        return "decriptedwith" + privateKey + ":" + str;
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
