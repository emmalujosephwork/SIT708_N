package com.example.learningapptwo

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginPage()
        }
    }
}

@Composable
fun LoginPage() {
    val context = LocalContext.current
    // Use mutableStateOf for storing the username and password
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Hardcoded credentials for demo
    val hardcodedUsername = "user123"
    val hardcodedPassword = "password123"

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Login", style = MaterialTheme.typography.headlineLarge)

            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = username,
                onValueChange = { username = it }, // Update the username value
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = password,
                onValueChange = { password = it }, // Update the password value
                label = { Text("Password") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                // Check if username and password match the hardcoded values
                if (username == hardcodedUsername && password == hardcodedPassword) {
                    Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show()
                    // Redirect to TaskListActivity after successful login
                    val intent = Intent(context, TaskListActivity::class.java)
                    context.startActivity(intent)
                } else {
                    Toast.makeText(context, "Invalid username or password", Toast.LENGTH_SHORT).show()
                }
            }) {
                Text("Login")
            }
            Spacer(modifier = Modifier.height(16.dp))
            TextButton(onClick = {
                // Navigate to SignupActivity
                val intent = Intent(context, SignupActivity::class.java)
                context.startActivity(intent)
            }) {
                Text("Don't have an account? Sign up")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    LoginPage()
}
