package com.example.itube2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    EditText youtubeUrlEditText;
    Button playButton, addToPlaylistButton, viewPlaylistButton;
    WebView youtubeWebView;
    UserDatabaseHelper dbHelper;
    String loggedInUsername; // Store logged-in user's username

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Retrieve the logged-in username from the intent
        loggedInUsername = getIntent().getStringExtra("USERNAME");

        // Initialize UI components
        youtubeUrlEditText = findViewById(R.id.youtube_url);
        playButton = findViewById(R.id.play_button);
        addToPlaylistButton = findViewById(R.id.add_to_playlist_button);
        viewPlaylistButton = findViewById(R.id.view_playlist_button);
        youtubeWebView = findViewById(R.id.youtube_player_view);

        // Initialize Database Helper
        dbHelper = new UserDatabaseHelper(this);

        // Configure WebView
        youtubeWebView.getSettings().setJavaScriptEnabled(true);
        youtubeWebView.setWebViewClient(new WebViewClient());
        youtubeWebView.setWebChromeClient(new WebChromeClient());

        // Play button logic (plays video based on URL)
        playButton.setOnClickListener(v -> {
            String url = youtubeUrlEditText.getText().toString();
            String videoId = extractVideoIdFromUrl(url);

            if (videoId != null) {
                // Construct the YouTube iframe embed URL
                String iframeUrl = "https://www.youtube.com/embed/" + videoId;
                youtubeWebView.loadUrl(iframeUrl);
            } else {
                Toast.makeText(HomeActivity.this, "Invalid YouTube URL", Toast.LENGTH_SHORT).show();
            }
        });

        // Add to Playlist button logic
        addToPlaylistButton.setOnClickListener(v -> {
            String url = youtubeUrlEditText.getText().toString();
            String videoId = extractVideoIdFromUrl(url);

            if (videoId != null) {
                // Save the video to the playlist (SQLite) for the logged-in user
                long result = dbHelper.addToPlaylist(loggedInUsername, url);
                if (result != -1) {
                    Toast.makeText(HomeActivity.this, "Video Added to Playlist", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(HomeActivity.this, "Failed to Add Video", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(HomeActivity.this, "Invalid YouTube URL", Toast.LENGTH_SHORT).show();
            }
        });

        // View Playlist button logic
        viewPlaylistButton.setOnClickListener(v -> {
            // Navigate to the View Playlist screen, pass the username
            Intent intent = new Intent(HomeActivity.this, ViewPlaylistActivity.class);
            intent.putExtra("USERNAME", loggedInUsername); // Pass the logged-in username to ViewPlaylistActivity
            startActivity(intent);
        });
    }

    // Helper method to extract YouTube video ID from URL
    private String extractVideoIdFromUrl(String url) {
        if (url.contains("youtube.com/watch?v=")) {
            return url.split("v=")[1];
        } else if (url.contains("youtu.be/")) {
            return url.split("youtu.be/")[1];
        }
        return null;
    }
}
