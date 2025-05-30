package com.example.learningapp;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity {

    private LinearLayout resultsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        resultsContainer = findViewById(R.id.resultsContainer);

        ArrayList<ResultItem> results = getIntent().getParcelableArrayListExtra("results");

        if (results == null || results.isEmpty()) {
            TextView tv = new TextView(this);
            tv.setText("No results to display.");
            resultsContainer.addView(tv);
            return;
        }

        // Calculate score
        int score = 0;
        for (ResultItem r : results) {
            if (r.userAnswer != null && r.userAnswer.equalsIgnoreCase(r.correctAnswer)) {
                score++;
            }
        }

        // Show score at top with margin and green color
        TextView scoreView = new TextView(this);
        scoreView.setTypeface(null, Typeface.BOLD);
        scoreView.setTextSize(20);
        scoreView.setText("Your Score: " + score + " out of " + results.size());
        scoreView.setPadding(0, 80, 0, 24);
        scoreView.setTextColor(Color.parseColor("#388E3C")); // Dark green color
        resultsContainer.addView(scoreView);

        // Display each question with answers and separator
        for (int i = 0; i < results.size(); i++) {
            ResultItem r = results.get(i);

            TextView questionTv = new TextView(this);
            questionTv.setTypeface(null, Typeface.BOLD);
            questionTv.setText((i + 1) + ". " + r.question);
            resultsContainer.addView(questionTv);

            TextView userAnswerTv = new TextView(this);
            userAnswerTv.setText("Your Answer: " + (r.userAnswer != null ? r.userAnswer : "No answer"));
            resultsContainer.addView(userAnswerTv);

            TextView correctAnswerTv = new TextView(this);
            correctAnswerTv.setText("Correct Answer: " + r.correctAnswer);
            resultsContainer.addView(correctAnswerTv);

            // Separator line
            TextView separator = new TextView(this);
            separator.setText("-------------------------------");
            resultsContainer.addView(separator);
        }
    }
}
