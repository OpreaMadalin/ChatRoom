package com.company.controller.tokens;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

import java.util.Date;
import java.util.Map;

import static java.util.Base64.getEncoder;

public class TokenManager {
    private final static String tokenConnector = ".";
    private final String privateKey;
    private final String publicKey;
    private final String signature;
    private final long expireInSeconds = 86000;


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

    public String sign(String str) {
        return encrypt(signature + tokenConnector + str);
    }

    public String generateToken(TokenClaims claims) {

        Algorithm algorithm = Algorithm.HMAC256(privateKey);
        long now = new Date().getTime();
        long expireTime = now + (expireInSeconds);
        Date expireDate = new Date(expireTime);

        String jwtToken = JWT.create()
                .withIssuer("JWT")
                .withIssuer("Madalin")
                .withClaim("claims", String.valueOf(claims))
                .withExpiresAt(expireDate)
                .sign(algorithm);

        return jwtToken;
    }


    public boolean verifyToken(String token) {

        Algorithm algorithm = Algorithm.HMAC256(privateKey);
        try {
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("JWT")
                    .withIssuer("Madalin")
                    .acceptExpiresAt(expireInSeconds)
                    .build();

            verifier.verify(token);
            return true;
        } catch (JWTVerificationException ex) {
            return false;
        }
    }

    public static String toBase64(String str) {
        return getEncoder().encodeToString(str.getBytes());
    }

    public String encrypt(String str) {
        return "encryptedwith" + publicKey + "shouldbedecryptedwith" + privateKey + ":" + str;
    }

    public String decrypt(String str) {
        return str.replace(
                "encryptedwith" + publicKey + "shouldbedecryptedwith" + privateKey + ":", "");
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
