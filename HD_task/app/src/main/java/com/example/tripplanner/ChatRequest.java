package com.example.tripplanner;

public class ChatRequest {
    private String message;

    public ChatRequest(String message) {
        this.message = message;
    }

    // Getter for message
    public String getMessage() {
        return message;
    }

    // Setter for message
    public void setMessage(String message) {
        this.message = message;
    }
}
