package com.example.lostandfound;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the buttons
        Button createAdvertButton = findViewById(R.id.createAdvertButton);
        Button showItemsButton = findViewById(R.id.showItemsButton);

        // Set up listeners for the buttons using lambda expressions
        createAdvertButton.setOnClickListener(v -> {
            // Navigate to the Create Advert Activity
            Intent intent = new Intent(MainActivity.this, CreateAdvertActivity.class);
            startActivity(intent);
        });

        showItemsButton.setOnClickListener(v -> {
            // Navigate to the Show Items Activity
            Intent intent = new Intent(MainActivity.this, ShowItemsActivity.class);
            startActivity(intent);
        });
    }
}
