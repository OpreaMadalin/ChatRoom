package com.company;

import com.company.controller.hashers.HashAlgorithm;
import com.company.controller.hashers.PBKDF2Hasher;
import com.company.controller.hashers.SHA256Hasher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChatroomApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatroomApplication.class, args);

        HashAlgorithm hasher = new PBKDF2Hasher();

        String passwordToHash = hasher.saltAndHash("madalinpass");
        System.out.println(passwordToHash);

        boolean check = hasher.checkPassword(passwordToHash, "madalinpass");
        System.out.println(check);

    }


}
