package com.company.controller;

import com.company.controller.database.MongoController;
import com.company.controller.hashers.HashAlgorithm;
import com.company.controller.hashers.SHA256Hasher;
import com.company.model.LoginRequestBody;
import com.company.model.LoginResponse;
import com.company.model.RegisterRequestBody;
import com.company.model.RegisterResponse;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final HashAlgorithm hasher = new SHA256Hasher();

    @RequestMapping("/register")
    public RegisterResponse register(@RequestBody RegisterRequestBody body) {

        String hashedPassword = hasher.saltAndHash(body.getPassword());

        MongoController mc = new MongoController();
        mc.addUser(body.getUsername(), hashedPassword);

        Document result = mc.getUserWithUsername(body.getUsername());

        String insertedID = "";
        if (result != null) {
            insertedID = ((ObjectId) result.get("_id")).toString();
        }

        return new RegisterResponse(insertedID);

    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequestBody body) {

        MongoController mc = new MongoController();
        Document result = mc.getUserWithUsername(body.getUsername());
        if (result == null) {
            return new LoginResponse("");
        }

        String referencePassword = (String) result.get("password");
        boolean isPasswordValid = hasher.checkPassword(referencePassword, body.getPassword());
        if (!isPasswordValid) {
            return new LoginResponse("");
        }
        return new LoginResponse("Login OK");
    }

}
