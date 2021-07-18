package com.company.model.UpdateChatroomPassword;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateChatroomPasswordRequestBody {

    private final String chatroomName;
    private final String password;
    private final String newPassword;


    public UpdateChatroomPasswordRequestBody(@JsonProperty("chatroomName") String chatroomName,
                                             @JsonProperty("password") String password,
                                             @JsonProperty("newPassword") String newPassword) {
        this.chatroomName = chatroomName;
        this.password = password;
        this.newPassword = newPassword;
    }

    public String getChatroomName() {
        return chatroomName;
    }

    public String getPassword() {
        return password;
    }

    public String getNewPassword() {
        return newPassword;
    }
}
