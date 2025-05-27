package com.example.itube2;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;


import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    EditText fullNameEditText, usernameEditText, passwordEditText, confirmPasswordEditText;
    Button createAccountButton;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize UI elements
        fullNameEditText = findViewById(R.id.full_name);
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.confirm_password);
        createAccountButton = findViewById(R.id.create_account_button);

        // Create or open the database
        UserDatabaseHelper dbHelper = new UserDatabaseHelper(this);
        database = dbHelper.getWritableDatabase();

        // Create account logic
        createAccountButton.setOnClickListener(v -> {
            String fullName = fullNameEditText.getText().toString();
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String confirmPassword = confirmPasswordEditText.getText().toString();

            // Check if passwords match
            if (!password.equals(confirmPassword)) {
                Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // Insert the user data into the database
            ContentValues contentValues = new ContentValues();
            contentValues.put(UserDatabaseHelper.COLUMN_FULL_NAME, fullName);
            contentValues.put(UserDatabaseHelper.COLUMN_USERNAME, username);
            contentValues.put(UserDatabaseHelper.COLUMN_PASSWORD, password);

            long rowId = database.insert(UserDatabaseHelper.TABLE_USERS, null, contentValues);

            if (rowId != -1) {
                Toast.makeText(SignUpActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class); // Redirect to login page
                startActivity(intent);
                finish(); // Close the sign-up page
            } else {
                Toast.makeText(SignUpActivity.this, "Error Creating Account", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
