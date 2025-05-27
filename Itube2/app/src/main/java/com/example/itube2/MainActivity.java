package com.example.itube2;

import android.content.ContentValues;
import android.database.Cursor;  // Correct import
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText usernameEditText, passwordEditText;
    Button loginButton, signUpButton;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);
        signUpButton = findViewById(R.id.signup_button);

        // Create or open the database
        UserDatabaseHelper dbHelper = new UserDatabaseHelper(this);
        database = dbHelper.getReadableDatabase();

        // Login button logic
        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            // Check credentials from the database
            Cursor cursor = database.rawQuery("SELECT * FROM users WHERE username=? AND password=?",
                    new String[]{username, password});

            if (cursor != null && cursor.getCount() > 0) {
                // Login successful
                Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                // Pass the username to HomeActivity
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                intent.putExtra("USERNAME", username); // Pass the username
                startActivity(intent);
                finish(); // Close the login page
            } else {
                // Invalid credentials
                Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
            }
        });

        // Redirect to SignUp activity
        signUpButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }
}
