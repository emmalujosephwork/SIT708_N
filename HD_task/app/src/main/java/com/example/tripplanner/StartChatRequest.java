package com.example.tripplanner;

import com.google.gson.annotations.SerializedName;

public class StartChatRequest {
    @SerializedName("user_email")
    private String userEmail;

    public StartChatRequest(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserEmail() {
        return userEmail;
    }
}
