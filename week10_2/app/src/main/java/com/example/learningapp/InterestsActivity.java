package com.example.learningapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InterestsActivity extends AppCompatActivity {

    LinearLayout interestsContainer;
    Button btnNext;
    String username;

    List<String> interests = Arrays.asList(
            "Algorithms",
            "Data Structures",
            "Web Development",
            "Testing"
    );

    List<CheckBox> checkBoxes = new ArrayList<>();

    DBHelper dbHelper; // Your SQLite helper class

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests);

        interestsContainer = findViewById(R.id.interestsContainer);
        btnNext = findViewById(R.id.btnNext);

        // Get username from intent extras (pass it when launching this activity)
        username = getIntent().getStringExtra("username");
        if (username == null) {
            Toast.makeText(this, "Error: username missing", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        dbHelper = new DBHelper(this);

        // Dynamically create checkboxes for interests in rows of 3
        int itemsPerRow = 3;
        LinearLayout currentRow = null;
        for (int i = 0; i < interests.size(); i++) {
            if (i % itemsPerRow == 0) {
                currentRow = new LinearLayout(this);
                currentRow.setOrientation(LinearLayout.HORIZONTAL);
                currentRow.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                interestsContainer.addView(currentRow);
            }

            CheckBox cb = new CheckBox(this);
            cb.setText(interests.get(i));
            cb.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1
            ));
            currentRow.addView(cb);
            checkBoxes.add(cb);
        }

        btnNext.setOnClickListener(v -> {
            ArrayList<String> selectedInterests = new ArrayList<>();
            for (CheckBox cb : checkBoxes) {
                if (cb.isChecked()) selectedInterests.add(cb.getText().toString());
            }

            if (selectedInterests.isEmpty()) {
                Toast.makeText(this, "Please select at least one interest", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save selected interests to database for this user
            saveUserInterests(username, selectedInterests);

            // Go to Login Activity after saving
            Intent intent = new Intent(InterestsActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void saveUserInterests(String username, List<String> interests) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Optional: delete old interests of this user before inserting new (depends on your schema)
        db.delete("user_interests", "username = ?", new String[]{username});

        for (String interest : interests) {
            ContentValues values = new ContentValues();
            values.put("username", username);
            values.put("interest", interest);
            db.insert("user_interests", null, values);
        }

        Toast.makeText(this, "Interests saved!", Toast.LENGTH_SHORT).show();
    }
}
