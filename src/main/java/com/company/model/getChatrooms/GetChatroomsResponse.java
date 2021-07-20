package com.company.model.getChatrooms;

import java.util.ArrayList;

public class GetChatroomsResponse {

    private final ArrayList<String> chatrooms;

    public GetChatroomsResponse(ArrayList<String> chatrooms) {
        this.chatrooms = chatrooms;
    }

    public ArrayList<String> getChatrooms() {
        return this.chatrooms;
    }
}
