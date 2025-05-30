package com.example.tripplanner;

public class SendChatMessageRequest {
    private String message;
    private String sender;

    public SendChatMessageRequest(String message, String sender) {
        this.message = message;
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }
}
