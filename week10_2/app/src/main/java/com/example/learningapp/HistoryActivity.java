package com.example.learningapp;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class HistoryActivity extends AppCompatActivity {

    private static final String TAG = "HistoryActivity";
    private LinearLayout historyContainer;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historyContainer = findViewById(R.id.historyContainer);
        username = getIntent().getStringExtra("username");
        if (username == null) {
            username = "unknown_user";
        }

        loadUserQuizHistory();
    }

    private void loadUserQuizHistory() {
        String url = "http://10.0.2.2:5000/getUserQuizHistory?username=" + username;

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if (response.length() == 0) {
                            TextView tv = new TextView(this);
                            tv.setText("No quiz history available.");
                            historyContainer.addView(tv);
                            return;
                        }

                        for (int i = 0; i < response.length(); i++) {
                            JSONObject quizObj = response.getJSONObject(i);

                            // Create CardView for each quiz session
                            CardView cardView = new CardView(this);
                            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                            );
                            cardParams.setMargins(16, 16, 16, 16);
                            cardView.setLayoutParams(cardParams);
                            cardView.setRadius(16f);
                            cardView.setCardElevation(8f);
                            cardView.setUseCompatPadding(true);
                            cardView.setContentPadding(24, 24, 24, 24);

                            // Inner LinearLayout inside CardView
                            LinearLayout cardLinearLayout = new LinearLayout(this);
                            cardLinearLayout.setOrientation(LinearLayout.VERTICAL);
                            cardView.addView(cardLinearLayout);

                            // Quiz number
                            TextView tvQuizTitle = new TextView(this);
                            tvQuizTitle.setText("Quiz #" + (i + 1));
                            tvQuizTitle.setTextSize(18);
                            tvQuizTitle.setPadding(0, 0, 0, 8);
                            cardLinearLayout.addView(tvQuizTitle);

                            // Quiz score
                            int score = quizObj.optInt("score", -1);
                            if (score >= 0) {
                                TextView tvScore = new TextView(this);
                                tvScore.setText("Score: " + score);
                                tvScore.setTextSize(16);
                                tvScore.setTextColor(Color.parseColor("#388E3C")); // Optional: green color for score
                                tvScore.setPadding(0, 0, 0, 12);
                                cardLinearLayout.addView(tvScore);
                            }

                            JSONArray questions = quizObj.getJSONArray("quiz");
                            JSONArray userAnswers = quizObj.getJSONArray("user_answers");

                            for (int j = 0; j < questions.length(); j++) {
                                JSONObject question = questions.getJSONObject(j);
                                String questionText = question.getString("questionText");
                                String userAnswer = userAnswers.getString(j);

                                TextView tvQuestion = new TextView(this);
                                tvQuestion.setText((j + 1) + ". " + questionText);
                                tvQuestion.setPadding(0, 8, 0, 4);
                                cardLinearLayout.addView(tvQuestion);

                                TextView tvUserAnswer = new TextView(this);
                                tvUserAnswer.setText("Your answer: " + userAnswer);
                                tvUserAnswer.setPadding(32, 0, 0, 8);
                                cardLinearLayout.addView(tvUserAnswer);
                            }

                            // Add CardView to the main container
                            historyContainer.addView(cardView);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing history data", e);
                        Toast.makeText(this, "Error loading history", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e(TAG, "Volley error loading history: " + error.getMessage());
                    Toast.makeText(this, "Error loading history from server", Toast.LENGTH_SHORT).show();
                });

        queue.add(request);
    }
}
