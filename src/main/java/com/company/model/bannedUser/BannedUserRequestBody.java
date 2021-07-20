package com.company.model.bannedUser;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BannedUserRequestBody {

    private final String chatroomName;
    private final String bannedUser;

    public BannedUserRequestBody(@JsonProperty("chatroomName") String chatroomName,
                                 @JsonProperty("bannedUser") String bannedUser) {
        this.chatroomName = chatroomName;
        this.bannedUser = bannedUser;

    }

    public String getChatroomName() {
        return chatroomName;
    }

    public String getBannedUser() {
        return bannedUser;
    }
}
