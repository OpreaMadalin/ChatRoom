package com.company.controller.hashers;

import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MadalinHasher implements HashAlgorithm {
    @Override
    public String hash(String passwordToHash) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] messageDigest = md.digest(passwordToHash.getBytes());
            BigInteger generateHash = new BigInteger(messageDigest);
            return generateHash.toString(16);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String genSalt() {
        int saltLength = 22;
        boolean useLetters = true;
        boolean useNumbers = true;
        String generatedSalt = RandomStringUtils.random(saltLength, useLetters, useNumbers);
        return generatedSalt;
    }

    @Override
    public String saltAndHash(String str) {
        String salt = genSalt();
        return salt + hash(salt + str);
    }

    @Override
    public boolean checkPassword(String hashPass, String candidate) {
        String str = hashPass.substring(22);
        String hash = hash(hashPass.substring(0, 22) + candidate);
        return hash.equals(str);
    }
}
