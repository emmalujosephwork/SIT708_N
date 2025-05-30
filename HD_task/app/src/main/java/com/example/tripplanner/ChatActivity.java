package com.example.tripplanner;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ChatAdapter adapter;
    private List<ChatMessage> messages;
    private EditText inputEt;
    private ImageButton sendBtn;
    private ApiService apiService;

    private String chatId;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Toolbar setup
        Toolbar toolbar = findViewById(R.id.chatToolbar);
        setSupportActionBar(toolbar);

        chatId = getIntent().getStringExtra("chat_id");
        userEmail = getIntent().getStringExtra("user_email");

        recyclerView = findViewById(R.id.chatRecyclerView);
        inputEt = findViewById(R.id.inputMessage);
        sendBtn = findViewById(R.id.sendButton);
        sendBtn.setEnabled(false);

        apiService = RetrofitClient.getApiService();

        messages = new ArrayList<>();
        adapter = new ChatAdapter(messages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        if (chatId == null) {
            startNewChat();
        } else {
            loadChatMessages(chatId);
            sendBtn.setEnabled(true);
        }

        sendBtn.setOnClickListener(v -> {
            if (chatId == null) {
                Toast.makeText(this, "Chat not ready yet, please wait", Toast.LENGTH_SHORT).show();
                return;
            }
            String text = inputEt.getText().toString().trim();
            if (text.isEmpty()) return;

            messages.add(new ChatMessage(text, ChatMessage.SENT));
            adapter.notifyItemInserted(messages.size() - 1);
            recyclerView.scrollToPosition(messages.size() - 1);
            inputEt.setText("");

            // Save user message
            apiService.sendChatMessage(chatId, new SendChatMessageRequest(text, "user")).enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(ChatActivity.this, "Failed to save message", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Toast.makeText(ChatActivity.this, "Failed to save message: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            // AI response
            apiService.chat(new ChatRequest(text)).enqueue(new Callback<ChatResponse>() {
                @Override
                public void onResponse(Call<ChatResponse> call, Response<ChatResponse> resp) {
                    String reply = (resp.isSuccessful() && resp.body() != null) ? resp.body().getReply() : "Error: no response";
                    messages.add(new ChatMessage(reply, ChatMessage.RECEIVED));
                    adapter.notifyItemInserted(messages.size() - 1);
                    recyclerView.scrollToPosition(messages.size() - 1);

                    // Save AI reply
                    apiService.sendChatMessage(chatId, new SendChatMessageRequest(reply, "bot")).enqueue(new Callback<ApiResponse>() {
                        @Override
                        public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {}
                        @Override
                        public void onFailure(Call<ApiResponse> call, Throwable t) {}
                    });
                }
                @Override
                public void onFailure(Call<ChatResponse> call, Throwable t) {
                    messages.add(new ChatMessage("Failed to get AI response: " + t.getMessage(), ChatMessage.RECEIVED));
                    adapter.notifyItemInserted(messages.size() - 1);
                    recyclerView.scrollToPosition(messages.size() - 1);
                }
            });
        });
    }

    private void startNewChat() {
        apiService.startChat(new StartChatRequest(userEmail)).enqueue(new Callback<ChatStartResponse>() {
            @Override
            public void onResponse(Call<ChatStartResponse> call, Response<ChatStartResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    chatId = response.body().getChat_id();
                    sendBtn.setEnabled(true);
                } else {
                    Toast.makeText(ChatActivity.this, "Failed to start chat", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ChatStartResponse> call, Throwable t) {
                Toast.makeText(ChatActivity.this, "Error starting chat: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadChatMessages(String chatId) {
        apiService.getChatMessages(chatId).enqueue(new Callback<ChatMessagesResponse>() {
            @Override
            public void onResponse(Call<ChatMessagesResponse> call, Response<ChatMessagesResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    messages.clear();
                    messages.addAll(response.body().getMessages());
                    adapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(messages.size() - 1);
                } else {
                    Toast.makeText(ChatActivity.this, "Failed to load messages", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ChatMessagesResponse> call, Throwable t) {
                Toast.makeText(ChatActivity.this, "Error loading messages: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save_chat) {
            if (chatId == null) {
                Toast.makeText(this, "Chat not ready to save", Toast.LENGTH_SHORT).show();
                return true;
            }
            if (messages.isEmpty()) {
                Toast.makeText(this, "No messages to save", Toast.LENGTH_SHORT).show();
                return true;
            }

            // Save all chat messages (assuming you have this API)
            apiService.saveChat(chatId, messages).enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(ChatActivity.this, "Chat saved successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ChatActivity.this, "Failed to save chat", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Toast.makeText(ChatActivity.this, "Error saving chat: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
