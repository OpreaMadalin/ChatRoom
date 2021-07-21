package com.company.controller.authController;

import com.company.controller.databaseController.MongoController;
import com.company.controller.hashersController.HashAlgorithm;
import com.company.controller.hashersController.SHA256Hasher;
import com.company.controller.tokenController.TokenManager;
import com.company.exception.UnauthorizedException;
import com.company.model.login.LoginRequestBody;
import com.company.model.login.LoginResponse;
import com.company.model.register.RegisterRequestBody;
import com.company.model.register.RegisterResponse;
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
            throw new UnauthorizedException();
        }
        String referencePassword = (String) result.get("password");
        boolean isPasswordValid = hasher.checkPassword(referencePassword, body.getPassword());
        if (!isPasswordValid) {
            throw new UnauthorizedException();
        }
        TokenManager tm = new TokenManager();
        String token = tm.generateToken(body.getUsername());
        return new LoginResponse(token);
    }
}
