package com.company.controller.hashersController;

public interface HashAlgorithm {

    String hash(String passwordToHash);

    String genSalt();

    String saltAndHash(String str);

    boolean checkPassword(String hashPass, String candidate);

}
