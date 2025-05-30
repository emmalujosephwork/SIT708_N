package com.example.tripplanner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private Button btnOldChat, btnNewChat;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnOldChat = findViewById(R.id.btnOldChat);
        btnNewChat = findViewById(R.id.btnNewChat);

        // Get user email from intent to pass later
        userEmail = getIntent().getStringExtra("user_email");

        btnOldChat.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ChatListActivity.class);
            intent.putExtra("user_email", userEmail);
            startActivity(intent);
        });

        btnNewChat.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ChatActivity.class);
            intent.putExtra("user_email", userEmail);
            startActivity(intent);
        });
    }
}
