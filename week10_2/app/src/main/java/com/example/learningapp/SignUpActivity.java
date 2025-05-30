package com.example.learningapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    EditText etUsername, etEmail, etConfirmEmail, etPassword, etConfirmPassword, etPhone;
    Button btnCreateAccount;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        databaseHelper = new DatabaseHelper(this);
        etUsername = findViewById(R.id.etSignUpUsername);
        etEmail = findViewById(R.id.etSignUpEmail);
        etConfirmEmail = findViewById(R.id.etSignUpConfirmEmail);
        etPassword = findViewById(R.id.etSignUpPassword);
        etConfirmPassword = findViewById(R.id.etSignUpConfirmPassword);
        etPhone = findViewById(R.id.etSignUpPhone);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);

        btnCreateAccount.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String confirmEmail = etConfirmEmail.getText().toString().trim();
            String password = etPassword.getText().toString();
            String confirmPassword = etConfirmPassword.getText().toString();
            String phone = etPhone.getText().toString().trim();

            if (username.isEmpty() || email.isEmpty() || confirmEmail.isEmpty() || password.isEmpty() ||
                    confirmPassword.isEmpty() || phone.isEmpty()) {
                Toast.makeText(SignUpActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!email.equals(confirmEmail)) {
                Toast.makeText(SignUpActivity.this, "Emails do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // Insert user to DB
            boolean inserted = databaseHelper.insertUser(username, email, password, phone);
            if (inserted) {
                Toast.makeText(SignUpActivity.this, "Account Created Successfully!", Toast.LENGTH_SHORT).show();

                // Navigate to InterestsActivity with username
                Intent intent = new Intent(SignUpActivity.this, InterestsActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
                finish();

            } else {
                Toast.makeText(SignUpActivity.this, "Failed to create account. Username might already exist.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
