package com.company.model.getAdmins;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetAdminsRequestBody {

    private final String chatroomName;

    public GetAdminsRequestBody(@JsonProperty("chatroomName") String chatroomName) {
        this.chatroomName = chatroomName;
    }

    public String getChatroomName() {
        return chatroomName;
    }
}
