package com.company;

import com.company.controller.Hasher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChatroomApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatroomApplication.class, args);

        Hasher hasher = new Hasher("SHA256");

        String passwordToHash = hasher.saltAndHash("madalinpass");
        System.out.println(passwordToHash);

        boolean check = hasher.checkPassword(passwordToHash, "madalinpass");
        System.out.println(check);

    }


}
