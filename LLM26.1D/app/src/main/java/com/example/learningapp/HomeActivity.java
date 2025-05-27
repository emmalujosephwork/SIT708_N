package com.example.learningapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class HomeActivity extends AppCompatActivity {
    TextView tvGreeting, tvTaskCount, tvTaskTitle, tvTaskDescription;
    CardView taskCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tvGreeting = findViewById(R.id.tvGreeting);
        tvTaskCount = findViewById(R.id.tvTaskCount);
        tvTaskTitle = findViewById(R.id.tvTaskTitle);
        tvTaskDescription = findViewById(R.id.tvTaskDescription);
        taskCard = findViewById(R.id.taskCard);

        String username = getIntent().getStringExtra("username");
        tvGreeting.setText("Hello, " + username);

        // Dummy data for task
        tvTaskCount.setText("You have 1 task due");
        tvTaskTitle.setText("Generated Task 1");
        tvTaskDescription.setText("Small Description for the generated Task");

        // Set click listener to open quiz activity
        taskCard.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, QuizActivity.class);
            startActivity(intent);
        });
    }
}
