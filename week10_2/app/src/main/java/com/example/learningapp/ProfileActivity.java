package com.example.learningapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvUsername, tvEmail, tvTotalQuestions, tvCorrectAnswers, tvIncorrectAnswers;
    private Button btnShareProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvUsername = findViewById(R.id.tvUsername);
        tvEmail = findViewById(R.id.tvEmail);
        tvTotalQuestions = findViewById(R.id.tvTotalQuestions);
        tvCorrectAnswers = findViewById(R.id.tvCorrectAnswers);
        tvIncorrectAnswers = findViewById(R.id.tvIncorrectAnswers);
        btnShareProfile = findViewById(R.id.btnShareProfile);

        // Example data, replace with actual user data
        tvUsername.setText("emma1");
        tvEmail.setText("emma1@example.com");
        tvTotalQuestions.setText("10");
        tvCorrectAnswers.setText("8");
        tvIncorrectAnswers.setText("2");

        btnShareProfile.setOnClickListener(v -> shareProfileSummary());
    }

    private void shareProfileSummary() {
        String shareText = "Profile Summary:\n"
                + "Username: " + tvUsername.getText() + "\n"
                + "Email: " + tvEmail.getText() + "\n"
                + "Total Questions: " + tvTotalQuestions.getText() + "\n"
                + "Correctly Answered: " + tvCorrectAnswers.getText() + "\n"
                + "Incorrect Answers: " + tvIncorrectAnswers.getText();

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, "Share your profile");
        startActivity(shareIntent);
    }
}
