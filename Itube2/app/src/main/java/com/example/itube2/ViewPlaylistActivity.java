package com.example.itube2;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ViewPlaylistActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PlaylistAdapter playlistAdapter;
    private ArrayList<PlaylistItem> playlistItems;
    private UserDatabaseHelper dbHelper;
    private String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_playlist);

        // Get the logged-in username from the intent
        currentUsername = getIntent().getStringExtra("USERNAME");

        // Initialize the RecyclerView
        recyclerView = findViewById(R.id.playlist_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize database helper
        dbHelper = new UserDatabaseHelper(this);

        // Initialize the playlist items list
        playlistItems = new ArrayList<>();

        // Initialize and set the adapter for RecyclerView
        playlistAdapter = new PlaylistAdapter(playlistItems);
        recyclerView.setAdapter(playlistAdapter);

        // Load playlist for the current user
        loadPlaylist();
    }

    // Method to load playlist from the database for the current user
    private void loadPlaylist() {
        // Clear current playlist before adding new data
        playlistItems.clear();

        // Fetch playlist for the current user
        ArrayList<String> playlistData = dbHelper.getPlaylistForUser(currentUsername);

        // If the playlist is empty, show a message
        if (playlistData.isEmpty()) {
            Toast.makeText(ViewPlaylistActivity.this, "No Videos in Playlist", Toast.LENGTH_SHORT).show();
        }

        // Populate playlistItems list with data
        for (String url : playlistData) {
            playlistItems.add(new PlaylistItem(url));
        }

        // Notify the adapter that the data has changed
        playlistAdapter.notifyDataSetChanged();
    }
}
