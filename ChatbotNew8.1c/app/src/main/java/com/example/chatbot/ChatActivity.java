package com.example.chatbot;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView chatRecyclerView;
    private EditText chatInputBox;
    private Button sendButton;
    private ProgressBar progressBar;

    private ChatAdapter chatAdapter;
    private ArrayList<ChatMessage> messages = new ArrayList<>();
    private String username = "User";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        username = getIntent().getStringExtra("username");

        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        chatInputBox = findViewById(R.id.chatInputBox);
        sendButton = findViewById(R.id.sendButton);
        progressBar = findViewById(R.id.progressBar);

        chatAdapter = new ChatAdapter(messages);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);

        messages.add(new ChatMessage("Welcome " + username + "!", false));
        chatAdapter.notifyItemInserted(messages.size() - 1);

        sendButton.setOnClickListener(v -> sendMessage());
    }

    private void sendMessage() {
        String userMessage = chatInputBox.getText().toString().trim();
        if (userMessage.isEmpty()) {
            Toast.makeText(this, "Type something", Toast.LENGTH_SHORT).show();
            return;
        }

        messages.add(new ChatMessage(userMessage, true));
        chatAdapter.notifyItemInserted(messages.size() - 1);
        chatRecyclerView.scrollToPosition(messages.size() - 1);
        chatInputBox.setText("");
        progressBar.setVisibility(View.VISIBLE);

        String url = "http://192.168.1.140:5000/chat";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    progressBar.setVisibility(View.GONE);
                    messages.add(new ChatMessage(response.trim(), false));
                    chatAdapter.notifyItemInserted(messages.size() - 1);
                    chatRecyclerView.scrollToPosition(messages.size() - 1);
                },
                error -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Server error", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userMessage", userMessage);
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        Volley.newRequestQueue(this).add(request);
    }
}
