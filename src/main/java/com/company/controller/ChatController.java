package com.company.controller;

import com.auth0.jwt.interfaces.Claim;
import com.company.controller.database.MongoController;
import com.company.controller.hashers.HashAlgorithm;
import com.company.controller.hashers.SHA256Hasher;
import com.company.controller.tokens.TokenManager;
import com.company.exception.NotFoundException;
import com.company.exception.UnauthorizedException;
import com.company.model.addAdmin.AddAdminRequestBody;
import com.company.model.addAdmin.AddAdminResponse;
import com.company.model.bannedUser.BannedUserRequestBody;
import com.company.model.bannedUser.BannedUserResponse;
import com.company.model.chatroomMessages.ChatroomMessageRequestBody;
import com.company.model.chatroomMessages.ChatroomMessageResponse;
import com.company.model.deleteChatroom.DeleteChatroomRequestBody;
import com.company.model.deleteChatroom.DeleteChatroomResponse;
import com.company.model.getChatrooms.GetChatroomsResponse;
import com.company.model.postChatroom.PostChatroomRequestBody;
import com.company.model.postChatroom.PostChatroomResponse;
import com.company.model.updateChatroomName.UpdateChatroomNameRequestBody;
import com.company.model.updateChatroomName.UpdateChatroomNameResponse;
import com.company.model.updateChatroomPassword.UpdateChatroomPasswordRequestBody;
import com.company.model.updateChatroomPassword.UpdateChatroomPasswordResponse;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
public class ChatController {

    private final HashAlgorithm hasher = new SHA256Hasher();


    @GetMapping("/getChatrooms")
    public GetChatroomsResponse getChatrooms(@RequestHeader(name = "Authorization") String authHeader) {

        TokenManager tm = new TokenManager();
        Map<String, Claim> claims = tm.verifyToken(authHeader);
        if (claims == null) {
            throw new UnauthorizedException();
        }

        MongoController mc = new MongoController();
        ArrayList<Document> chatrooms = mc.getChatrooms();
        ArrayList<String> chatroomNames = new ArrayList<>();
        for (Document chatroom : chatrooms) {
            chatroomNames.add(chatroom.get("chatroomName").toString());
        }
        return new GetChatroomsResponse(chatroomNames);
    }

    @PostMapping("/getChatroomMessages")
    public ChatroomMessageResponse getChatroomMessages(@RequestHeader(name = "Authorization") String authHeader,
                                                       @RequestBody ChatroomMessageRequestBody body) {

        TokenManager tm = new TokenManager();
        Map<String, Claim> claims = tm.verifyToken(authHeader);

        if (claims == null) {
            throw new UnauthorizedException();
        }

        MongoController mc = new MongoController();
        String username = claims.get("username").asString();
        if (mc.isBanned(username, body.getChatroomName())) {
            throw new UnauthorizedException();
        }

        Document result = mc.getChatRoomWithName(body.getChatroomName());
        if (result == null) {
            throw new NotFoundException();
        }

        String referencePassword = (String) result.get("password");
        boolean isPasswordValid = hasher.checkPassword(referencePassword, body.getPassword());

        if (!isPasswordValid) {
            throw new UnauthorizedException();
        }

        ArrayList<Document> chatrooms = mc.getChatroomWithName(body.getChatroomName());
        ArrayList<Document> chatroomMessages = new ArrayList<>();
        for (Document chatroom : chatrooms) {
            List<Document> messages = chatroom.getList("messages", Document.class);
            chatroomMessages.addAll(messages);
        }
        return new ChatroomMessageResponse(chatroomMessages);
    }

    @PostMapping("/addChatroomMessage")
    public void addMessage(@RequestHeader(name = "Authorization") String authHeader,
                           @RequestBody ChatroomMessageRequestBody body) {

        TokenManager tm = new TokenManager();

        Map<String, Claim> claims = tm.verifyToken(authHeader);
        if (claims == null) {
            throw new UnauthorizedException();
        }

        MongoController mc = new MongoController();
        String username = claims.get("username").asString();
        if (mc.isBanned(username, body.getChatroomName())) {
            throw new UnauthorizedException();
        }

        Document result = mc.getChatRoomWithName(body.getChatroomName());
        if (result == null) {
            throw new NotFoundException();
        }

        String referencePassword = (String) result.get("password");
        boolean isPasswordValid = hasher.checkPassword(referencePassword, body.getPassword());
        if (!isPasswordValid) {
            throw new UnauthorizedException();
        }
        mc.addMessage(body.getChatroomName(), body.getMessage(), claims.get("username").asString());

    }

    @PostMapping("/addChatroom")
    public PostChatroomResponse addChatroom(@RequestHeader(name = "Authorization") String authHeader,
                                            @RequestBody PostChatroomRequestBody body) {

        TokenManager tm = new TokenManager();
        Map<String, Claim> claims = tm.verifyToken(authHeader);

        if (claims == null) {
            throw new UnauthorizedException();
        }

        MongoController mc = new MongoController();
        String hashedPassword = hasher.saltAndHash(body.getPassword());
        mc.addChatroom(body.getChatroomName(), hashedPassword, claims.get("username").asString());
        Document result = mc.getChatRoomWithName(body.getChatroomName());
        String insertedID = "";
        if (result != null) {
            insertedID = ((ObjectId) result.get("_id")).toString();
        }
        return new PostChatroomResponse(insertedID);
    }


    @DeleteMapping("/deleteChatroom")
    public DeleteChatroomResponse deleteChatroom(@RequestHeader(name = "Authorization") String authHeader,
                                                 @RequestBody DeleteChatroomRequestBody body) {

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

        String referencePassword = (String) result.get("password");
        boolean isPasswordValid = hasher.checkPassword(referencePassword, body.getPassword());
        if (!isPasswordValid) {
            throw new UnauthorizedException();
        }

        mc.deleteChatroom(body.getChatroomName());
        return new DeleteChatroomResponse(body.getChatroomName() + " deleted");
    }

    @PostMapping("/updateChatroomName")
    public UpdateChatroomNameResponse updateChatroomName(@RequestHeader(name = "Authorization") String authHeader,
                                                         @RequestBody UpdateChatroomNameRequestBody body) {
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
        String referencePassword = (String) result.get("password");
        boolean isPasswordValid = hasher.checkPassword(referencePassword, body.getPassword());
        if (!isPasswordValid) {
            throw new UnauthorizedException();
        }
        mc.updateChatroomName(body.getChatroomName(), body.getNewChatroomName());
        return new UpdateChatroomNameResponse(body.getNewChatroomName());
    }

    @PostMapping("/updateChatroomPassword")
    public UpdateChatroomPasswordResponse updateChatroomPassword(@RequestHeader(name = "Authorization") String authHeader,
                                                                 @RequestBody UpdateChatroomPasswordRequestBody body) {

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

        String hashedPassword = hasher.saltAndHash(body.getNewPassword());
        String referencePassword = (String) result.get("password");
        boolean isPasswordValid = hasher.checkPassword(referencePassword, body.getPassword());
        if (!isPasswordValid) {
            throw new UnauthorizedException();
        }

        mc.updateChatroomPassword(body.getChatroomName(), hashedPassword);

        return new UpdateChatroomPasswordResponse(body.getChatroomName());
    }

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
