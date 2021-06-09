package com.company.controller.hashers;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class MadalinHasher implements HashAlgorithm {
    @Override
    public String hash(String passwordToHash) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] messageDigest = md.digest(passwordToHash.getBytes());
            BigInteger generateHash = new BigInteger(messageDigest);
            return generateHash.toString(16);
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String genSalt() {
        int leftLimit = 48;
        int rightLimit = 122;
        int targetStringLength = 22;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
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
