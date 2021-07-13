package com.company.controller;

import com.company.controller.database.MongoController;
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
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ChatController {

    @GetMapping("/chatrooms")
    public GetChatroomsResponse getChatrooms(@RequestHeader(name = "Authorization") String authHeader) {

        TokenManager tm = new TokenManager();
        boolean claims = tm.verifyToken(authHeader);
        if (!claims) {
            throw new UnauthorizedException();
        }
        MongoController mc = new MongoController();
        ArrayList<Document> chatrooms = mc.getChatrooms();
        ArrayList<String> chatroomNames = new ArrayList<>();
        for (Document chatroom : chatrooms) {
            chatroomNames.add(chatroom.get("name").toString());
        }
        return new GetChatroomsResponse(chatroomNames);
    }

    @GetMapping("/chatroomMessages")
    public GetChatroomMessages getChatroomMessages(@RequestHeader(name = "Authorization") String authHeader,
                                                   @RequestBody ChatroomMessagesRequestBody body) {

        TokenManager tm = new TokenManager();
        boolean claims = tm.verifyToken(authHeader);

        if (!claims) {
            throw new UnauthorizedException();
        }
        MongoController mc = new MongoController();
        ArrayList<Document> chatrooms = mc.getChatroomWithName(body.getName());
        ArrayList<String> chatroomMessages = new ArrayList<>();
        for (Document chatroom : chatrooms) {
            List<String> messages = chatroom.getList("messages", String.class);
            chatroomMessages.addAll(messages);
        }
        return new GetChatroomMessages(chatroomMessages);
    }

    @PostMapping("/chatrooms")
    public PostChatroomResponse addChatroom(@RequestHeader(name = "Authorization") String authHeader,
                                            @RequestBody PostChatroomRequestBody body) {

        TokenManager tm = new TokenManager();
        boolean claims = tm.verifyToken(authHeader);

        if (!claims) {
            throw new UnauthorizedException();
        }
        MongoController mc = new MongoController();
        mc.addChatroom(body.getName());
        Document result = mc.getChatRoomWithName(body.getName());
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
        boolean claims = tm.verifyToken(authHeader);
        if (!claims) {
            throw new UnauthorizedException();
        }
        MongoController mc = new MongoController();
        Document result = mc.getChatRoomWithName(body.getName());
        if (result == null) {
            throw new NotFoundException();
        }
        mc.deleteChatroom(body.getName());
        return new DeleteChatroomResponse(body.getName() + " deleted");
    }
}
