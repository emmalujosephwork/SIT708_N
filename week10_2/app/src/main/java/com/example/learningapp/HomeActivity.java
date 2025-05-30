package com.example.learningapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.cardview.widget.CardView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {
    TextView tvGreeting, tvTaskCount, tvTaskTitle, tvTaskDescription;
    CardView taskCard;
    Button btnUpgrade, btnHistory, btnViewProfile;  // Added btnViewProfile

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize views
        tvGreeting = findViewById(R.id.tvGreeting);
        tvTaskCount = findViewById(R.id.tvTaskCount);
        tvTaskTitle = findViewById(R.id.tvTaskTitle);
        tvTaskDescription = findViewById(R.id.tvTaskDescription);
        taskCard = findViewById(R.id.taskCard);
        btnUpgrade = findViewById(R.id.btnUpgrade);
        btnHistory = findViewById(R.id.btnHistory);
        btnViewProfile = findViewById(R.id.btnViewProfile);  // Initialize the new button

        // Get username passed via Intent
        String username = getIntent().getStringExtra("username");
        tvGreeting.setText("Hello, " + username);

        // Set dummy task data
        tvTaskCount.setText("You have 1 task due");
        tvTaskTitle.setText("Generated Task 1");
        tvTaskDescription.setText("Small Description for the generated Task");

        // Quiz card click -> QuizActivity
        taskCard.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, QuizActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        // Upgrade button click -> UpgradeActivity
        btnUpgrade.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, UpgradeActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        // History button click -> HistoryActivity
        btnHistory.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, HistoryActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        // View Profile button click -> ProfileActivity
        btnViewProfile.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });
    }
}
