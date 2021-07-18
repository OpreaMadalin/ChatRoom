package com.company.controller;

import com.auth0.jwt.interfaces.Claim;
import com.company.controller.database.MongoController;
import com.company.controller.hashers.HashAlgorithm;
import com.company.controller.hashers.SHA256Hasher;
import com.company.controller.tokens.TokenManager;
import com.company.exception.NotFoundException;
import com.company.exception.UnauthorizedException;
import com.company.model.ChatroomMessages.ChatroomMessagesRequestBody;
import com.company.model.ChatroomMessages.GetChatroomMessages;
import com.company.model.DeleteChatroom.DeleteChatroomRequestBody;
import com.company.model.DeleteChatroom.DeleteChatroomResponse;
import com.company.model.GetChatrooms.GetChatroomsResponse;
import com.company.model.PostChatroom.PostChatroomRequestBody;
import com.company.model.PostChatroom.PostChatroomResponse;
import com.company.model.UpdateChatroom.UpdateChatroomRequestBody;
import com.company.model.UpdateChatroom.UpdateChatroomResponse;
import com.company.model.UpdateChatroomPassword.UpdateChatroomPasswordRequestBody;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class ChatController {

    private final HashAlgorithm hasher = new SHA256Hasher();

    @GetMapping("/chatrooms")
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
    public GetChatroomMessages getChatroomMessages(@RequestHeader(name = "Authorization") String authHeader,
                                                   @RequestBody ChatroomMessagesRequestBody body) {

        TokenManager tm = new TokenManager();
        Map<String, Claim> claims = tm.verifyToken(authHeader);

        if (claims == null) {
            throw new UnauthorizedException();
        }
        MongoController mc = new MongoController();
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
        return new GetChatroomMessages(chatroomMessages);
    }

    @PostMapping("/addChatroomMessage")
    public void addMessages(@RequestHeader(name = "Authorization") String authHeader,
                            @RequestBody ChatroomMessagesRequestBody body) {

        TokenManager tm = new TokenManager();
        Map<String, Claim> claims = tm.verifyToken(authHeader);

        if (claims == null) {
            throw new UnauthorizedException();
        }

        MongoController mc = new MongoController();
        Document result = mc.getChatRoomWithName(body.getChatroomName());
        if (result == null) {
            throw new NotFoundException();
        }
        String referencePassword = (String) result.get("password");
        boolean isPasswordValid = hasher.checkPassword(referencePassword, body.getPassword());
        if (!isPasswordValid) {
            throw new UnauthorizedException();
        }
        mc.addMessage(body.getChatroomName(), body.getMessages(), claims.get("username").toString());

    }

    @PostMapping("/chatrooms")
    public PostChatroomResponse addChatroom(@RequestHeader(name = "Authorization") String authHeader,
                                            @RequestBody PostChatroomRequestBody body) {

        TokenManager tm = new TokenManager();
        Map<String, Claim> claims = tm.verifyToken(authHeader);

        if (claims == null) {
            throw new UnauthorizedException();
        }
        String hashedPassword = hasher.saltAndHash(body.getPassword());
        MongoController mc = new MongoController();
        mc.addChatroom(body.getChatroomName(), hashedPassword);
        Document result = mc.getChatRoomWithName(body.getChatroomName());
        String insertedID = "";
        if (result != null) {
            insertedID = ((ObjectId) result.get("_id")).toString();
        }
        return new PostChatroomResponse(insertedID);
    }

    @DeleteMapping("/chatrooms")
    public DeleteChatroomResponse deleteChatrooms(@RequestHeader(name = "Authorization") String authHeader,
                                                  @RequestBody DeleteChatroomRequestBody body) {

        TokenManager tm = new TokenManager();
        Map<String, Claim> claims = tm.verifyToken(authHeader);
        if (claims == null) {
            throw new UnauthorizedException();
        }
        MongoController mc = new MongoController();
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
    public UpdateChatroomResponse updateChatroomName(@RequestHeader(name = "Authorization") String authHeader,
                                                     @RequestBody UpdateChatroomRequestBody body) {
        TokenManager tm = new TokenManager();
        Map<String, Claim> claims = tm.verifyToken(authHeader);
        if (claims == null) {
            throw new UnauthorizedException();
        }
        MongoController mc = new MongoController();
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
        return new UpdateChatroomResponse(body.getNewChatroomName());
    }

    @PostMapping("/updateChatroomPassword")
    public void updateChatroomPassword(@RequestHeader(name = "Authorization") String authHeader,
                                       @RequestBody UpdateChatroomPasswordRequestBody body) {

        TokenManager tm = new TokenManager();
        Map<String, Claim> claims = tm.verifyToken(authHeader);
        if (claims == null) {
            throw new UnauthorizedException();
        }
        MongoController mc = new MongoController();

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
    }
}
