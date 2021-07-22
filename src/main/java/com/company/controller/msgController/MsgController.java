package com.company.controller.msgController;

import com.auth0.jwt.interfaces.Claim;
import com.company.controller.databaseController.MongoController;
import com.company.controller.hashersController.HashAlgorithm;
import com.company.controller.hashersController.SHA256Hasher;
import com.company.controller.tokenController.TokenManager;
import com.company.exception.NotFoundException;
import com.company.exception.UnauthorizedException;
import com.company.model.chatroomMessages.ChatroomMessageRequestBody;
import com.company.model.chatroomMessages.ChatroomMessageResponse;
import org.bson.Document;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class MsgController {

    private final HashAlgorithm hasher = new SHA256Hasher();

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

        boolean existPass = mc.checkPasswordFieldExistInChatroom(body.getChatroomName());
        if (existPass) {
            String referencePassword = (String) result.get("password");
            boolean isPasswordValid = hasher.checkPassword(referencePassword, body.getPassword());
            if (!isPasswordValid) {
                throw new UnauthorizedException();
            }
        }

        mc.addMessage(body.getChatroomName(), body.getMessage(), claims.get("username").asString());
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

        boolean existPass = mc.checkPasswordFieldExistInChatroom(body.getChatroomName());
        if (existPass) {
            String referencePassword = (String) result.get("password");
            boolean isPasswordValid = hasher.checkPassword(referencePassword, body.getPassword());
            if (!isPasswordValid) {
                throw new UnauthorizedException();
            }
        }

        ArrayList<Document> chatrooms = mc.getChatroomWithName(body.getChatroomName());
        ArrayList<Document> chatroomMessages = new ArrayList<>();
        for (Document chatroom : chatrooms) {
            List<Document> messages = chatroom.getList("messages", Document.class);
            chatroomMessages.addAll(messages);
        }
        return new ChatroomMessageResponse(chatroomMessages);
    }

}
