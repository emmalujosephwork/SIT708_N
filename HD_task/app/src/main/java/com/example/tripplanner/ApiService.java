package com.example.tripplanner;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    // User signup endpoint
    @POST("signup")
    Call<ApiResponse> signup(@Body SignupRequest signupRequest);

    // User login endpoint
    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    // Chat AI endpoint (send user message, get AI reply)
    @POST("chat")
    Call<ChatResponse> chat(@Body ChatRequest chatRequest);

    // Start a new chat session for a user
    @POST("chat/start")
    Call<ChatStartResponse> startChat(@Body StartChatRequest startChatRequest);

    // List all chat sessions for a given user email
    @GET("chat/list/{user_email}")
    Call<List<ChatListItem>> listUserChats(@Path("user_email") String userEmail);

    // Get all messages in a chat session by chat ID
    @GET("chat/{chat_id}")
    Call<ChatMessagesResponse> getChatMessages(@Path("chat_id") String chatId);

    // Send a message in an existing chat session
    @POST("chat/{chat_id}/send")
    Call<ApiResponse> sendChatMessage(@Path("chat_id") String chatId, @Body SendChatMessageRequest request);

    // Save entire chat conversation to backend
    @POST("chat/{chat_id}/save")
    Call<ApiResponse> saveChat(@Path("chat_id") String chatId, @Body List<ChatMessage> messages);
}
