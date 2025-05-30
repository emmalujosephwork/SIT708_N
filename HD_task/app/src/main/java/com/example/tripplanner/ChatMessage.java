package com.example.tripplanner;

public class ChatMessage {
    public static final int SENT = 0, RECEIVED = 1;
    private final String message;
    private final int type;

    public ChatMessage(String message, int type) {
        this.message = message;
        this.type = type;
    }

    public String getMessage() { return message; }
    public int getType() { return type; }
}
