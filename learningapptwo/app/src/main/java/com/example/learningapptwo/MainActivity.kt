package com.example.learningapptwo

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import com.example.learningapptwo.ui.theme.LearningapptwoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LearningapptwoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Centering content vertically and horizontally
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center, // Vertically center
                        horizontalAlignment = Alignment.CenterHorizontally // Horizontally center
                    ) {
                        Greeting("User") // Change greeting message here
                        Spacer(modifier = Modifier.height(16.dp))
                        LoginButton()
                    }
                }
            }
        }
    }
}

// Moved the composable functions outside the MainActivity class
@Composable
fun Greeting(name: String) {
    Text(
        text = "Hello, $name!", // Changed to "Hello, User"
        style = MaterialTheme.typography.headlineLarge
    )
}

@Composable
fun LoginButton() {
    val context = LocalContext.current
    Button(
        onClick = {
            // Navigate to LoginActivity
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Go to Login")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LearningapptwoTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            Greeting("User") // Preview with "User"
            Spacer(modifier = Modifier.height(16.dp))
            LoginButton()
        }
    }
}
