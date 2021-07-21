package com.company.controller.adminController;

import com.auth0.jwt.interfaces.Claim;
import com.company.controller.databaseController.MongoController;
import com.company.controller.tokenController.TokenManager;
import com.company.exception.NotFoundException;
import com.company.exception.UnauthorizedException;
import com.company.model.addAdmin.AddAdminRequestBody;
import com.company.model.addAdmin.AddAdminResponse;
import com.company.model.bannedUser.BannedUserRequestBody;
import com.company.model.bannedUser.BannedUserResponse;
import com.company.model.getAdmins.GetAdminsRequestBody;
import com.company.model.getAdmins.GetAdminsResponse;
import org.bson.Document;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class AdminController {


    @PostMapping("/addAdmin")
    public AddAdminResponse addAdmin(@RequestHeader(name = "Authorization") String authHeader,
                                     @RequestBody AddAdminRequestBody body) {

        TokenManager tm = new TokenManager();
        Map<String, Claim> claims = tm.verifyToken(authHeader);

        if (claims == null) {
            throw new UnauthorizedException();
        }

        MongoController mc = new MongoController();
        String username = claims.get("username").asString();
        if (!mc.isAdmin(username, body.getChatroomName())) {
            throw new UnauthorizedException();
        }

        mc.addAdmin(body.getChatroomName(), body.getAdminName());

        return new AddAdminResponse("");
    }

    @PostMapping("/getAdmins")
    public GetAdminsResponse getAdmins(@RequestHeader(name = "Authorization") String authHeader,
                                       @RequestBody GetAdminsRequestBody body) {

        TokenManager tm = new TokenManager();
        Map<String, Claim> claims = tm.verifyToken(authHeader);
        if (claims == null) {
            throw new UnauthorizedException();
        }

        MongoController mc = new MongoController();
        String username = claims.get("username").asString();
        if (!mc.isAdmin(username, body.getChatroomName())) {
            throw new UnauthorizedException();
        }

        Document result = mc.getChatRoomWithName(body.getChatroomName());
        if (result == null) {
            throw new NotFoundException();
        }

        ArrayList<Document> chatrooms = mc.getChatroomWithName(body.getChatroomName());
        ArrayList<String> chatroomAdmins = new ArrayList<>();
        for (Document chatroom : chatrooms) {
            List<String> admins = chatroom.getList("admins", String.class);
            chatroomAdmins.addAll(admins);
        }

        return new GetAdminsResponse(chatroomAdmins);
    }

    @PostMapping("/banUser")
    public BannedUserResponse addBannedUser(@RequestHeader(name = "Authorization") String authHeader,
                                            @RequestBody BannedUserRequestBody body) {

        TokenManager tm = new TokenManager();
        Map<String, Claim> claims = tm.verifyToken(authHeader);

        if (claims == null) {
            throw new UnauthorizedException();
        }

        MongoController mc = new MongoController();
        String username = claims.get("username").asString();
        if (!mc.isAdmin(username, body.getChatroomName())) {
            throw new UnauthorizedException();
        }

        mc.addBannedUser(body.getChatroomName(), body.getBannedUser());

        return new BannedUserResponse("");
    }

}
