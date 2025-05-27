package com.example.learningapptwo

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

class InterestSelectionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InterestSelectionPage(
                onNextClicked = {
                    // Navigate to the next activity
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            )
        }
    }
}

@Composable
fun InterestSelectionPage(
    onNextClicked: () -> Unit = {}
) {
    val context = LocalContext.current
    var selectedInterests by remember { mutableStateOf(setOf<String>()) }

    // Define list of all interests
    val interests = listOf(
        "Algorithms", "Data Structures", "Web Development", "Testing",
        "Machine Learning", "Cloud Computing", "AI", "Cybersecurity",
        "Blockchain", "Mobile Development", "Data Science", "DevOps",
        "Internet of Things", "Big Data", "Virtual Reality", "Augmented Reality"
    )

    Scaffold(
        bottomBar = {
            // Next button at the bottom
            Button(
                onClick = {
                    if (selectedInterests.isNotEmpty()) {
                        // Save interests to database
                        val dbHelper = DatabaseHelper(context)
                        val username = "sampleUser" // Replace with actual username
                        val interestsString = selectedInterests.joinToString(", ")
                        dbHelper.addInterests(username, interestsString)

                        // Show confirmation and navigate
                        Toast.makeText(
                            context,
                            "${selectedInterests.size} interests selected",
                            Toast.LENGTH_SHORT
                        ).show()

                        onNextClicked()
                    } else {
                        Toast.makeText(
                            context,
                            "Please select at least one interest",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                enabled = selectedInterests.isNotEmpty()
            ) {
                Text("Next", style = MaterialTheme.typography.labelLarge)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Select Your Interests",
                style = MaterialTheme.typography.headlineLarge
            )
            Text(
                "Choose up to 10 topics you're interested in",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Counter showing selected interests
            Text(
                "${selectedInterests.size} of 10 selected",
                style = MaterialTheme.typography.labelLarge.copy(
                    color = if (selectedInterests.size > 10) Color.Red
                    else MaterialTheme.colorScheme.primary
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            // LazyColumn for scrolling through interests
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(interests.size) { index ->
                    val interest = interests[index]
                    val isSelected = interest in selectedInterests

                    Button(
                        onClick = {
                            selectedInterests = if (isSelected) {
                                selectedInterests - interest
                            } else if (selectedInterests.size < 10) {
                                selectedInterests + interest
                            } else {
                                Toast.makeText(
                                    context,
                                    "Maximum 10 interests allowed",
                                    Toast.LENGTH_SHORT
                                ).show()
                                selectedInterests
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = when {
                                isSelected -> Color(0xFFBB86FC) // Purple when selected
                                else -> MaterialTheme.colorScheme.surfaceVariant // Light gray when not selected
                            },
                            contentColor = when {
                                isSelected -> Color.White
                                else -> MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Text(interest)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InterestSelectionPreview() {
    InterestSelectionPage()
}