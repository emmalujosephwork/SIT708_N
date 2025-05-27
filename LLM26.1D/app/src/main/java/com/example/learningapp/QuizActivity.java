package com.example.learningapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";

    private TextView questionTitle;
    private RadioGroup optionsGroup;
    private Button btnNext;

    private ArrayList<Question> questionList = new ArrayList<>();
    private int currentQuestionIndex = 0;

    // Store user answers, matching question order
    private ArrayList<ResultItem> results = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_single);

        questionTitle = findViewById(R.id.questionTitle);
        optionsGroup = findViewById(R.id.optionsGroup);
        btnNext = findViewById(R.id.btnNext);

        loadQuizFromServer();

        btnNext.setOnClickListener(v -> {
            if (optionsGroup.getCheckedRadioButtonId() == -1) {
                Toast.makeText(QuizActivity.this, "Please select an answer", Toast.LENGTH_SHORT).show();
                return;
            }

            // Get selected answer text (full text without letter prefix)
            RadioButton selected = findViewById(optionsGroup.getCheckedRadioButtonId());
            String selectedText = selected.getText().toString();
            int colonIndex = selectedText.indexOf(":");
            String answerText = colonIndex != -1 && colonIndex + 2 < selectedText.length()
                    ? selectedText.substring(colonIndex + 2)
                    : selectedText;

            // Save user answer with question and correct answer full text
            Question currentQuestion = questionList.get(currentQuestionIndex);
            results.add(new ResultItem(currentQuestion.questionText, currentQuestion.correctAnswer, answerText));

            currentQuestionIndex++;

            if (currentQuestionIndex < questionList.size()) {
                displayQuestion(currentQuestionIndex);

                // Clear selection for next question
                optionsGroup.clearCheck();

                // Change button text to Submit on last question
                if (currentQuestionIndex == questionList.size() - 1) {
                    btnNext.setText("Submit");
                }
            } else {
                // All questions answered - go to results
                Intent intent = new Intent(QuizActivity.this, ResultsActivity.class);
                intent.putParcelableArrayListExtra("results", results);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadQuizFromServer() {
        String url = "http://10.0.2.2:5000/getQuiz?topic=Movies";
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray quizArray = response.getJSONArray("quiz");

                        questionList.clear();

                        for (int i = 0; i < quizArray.length(); i++) {
                            JSONObject obj = quizArray.getJSONObject(i);

                            String questionText = obj.getString("question");
                            JSONArray optionsArray = obj.getJSONArray("options");
                            String correctAnswerLetter = obj.getString("correct_answer");

                            ArrayList<String> options = new ArrayList<>();
                            for (int j = 0; j < optionsArray.length(); j++) {
                                options.add(optionsArray.getString(j));
                            }

                            // Convert correctAnswerLetter (A, B, C, D) to full text answer
                            int correctIndex = correctAnswerLetter.charAt(0) - 'A';
                            String correctAnswerFull = (correctIndex >= 0 && correctIndex < options.size())
                                    ? options.get(correctIndex) : "";

                            Question question = new Question(questionText, options, correctAnswerFull);
                            questionList.add(question);
                        }

                        if (!questionList.isEmpty()) {
                            displayQuestion(0);
                        }

                    } catch (Exception e) {
                        Log.e(TAG, "JSON parsing error: " + e.getMessage(), e);
                        Toast.makeText(QuizActivity.this, "Error loading quiz data", Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    Log.e(TAG, "Volley error: " + error.getMessage(), error);
                    Toast.makeText(QuizActivity.this, "Error loading quiz from server", Toast.LENGTH_LONG).show();
                });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsonObjectRequest);
    }

    private void displayQuestion(int index) {
        Question q = questionList.get(index);
        questionTitle.setText((index + 1) + ". " + q.questionText);

        optionsGroup.removeAllViews();
        for (int i = 0; i < q.options.size(); i++) {
            RadioButton rb = new RadioButton(this);
            char optionLetter = (char) ('A' + i);
            rb.setText(optionLetter + ": " + q.options.get(i));
            rb.setId(View.generateViewId());
            optionsGroup.addView(rb);
        }

        btnNext.setText(index == questionList.size() - 1 ? "Submit" : "Next");
    }

    private static class Question {
        String questionText;
        ArrayList<String> options;
        String correctAnswer;

        Question(String questionText, ArrayList<String> options, String correctAnswer) {
            this.questionText = questionText;
            this.options = options;
            this.correctAnswer = correctAnswer;
        }
    }
}
