package com.example.tripplanner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatListActivity extends AppCompatActivity implements ChatListAdapter.OnChatClickListener {

    private RecyclerView recyclerView;
    private ChatListAdapter adapter;
    private String userEmail;  // Pass from login

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        userEmail = getIntent().getStringExtra("user_email");

        recyclerView = findViewById(R.id.chatListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadChatList();
    }

    private void loadChatList() {
        RetrofitClient.getApiService().listUserChats(userEmail).enqueue(new Callback<List<ChatListItem>>() {
            @Override
            public void onResponse(Call<List<ChatListItem>> call, Response<List<ChatListItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ChatListItem> chats = response.body();
                    adapter = new ChatListAdapter(chats, ChatListActivity.this);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(ChatListActivity.this, "Failed to load chats", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ChatListItem>> call, Throwable t) {
                Toast.makeText(ChatListActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onChatClick(String chatId) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("chat_id", chatId);
        intent.putExtra("user_email", userEmail);
        startActivity(intent);
    }
}
