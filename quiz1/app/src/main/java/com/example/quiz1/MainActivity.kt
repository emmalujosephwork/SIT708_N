package com.example.quiz1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.quiz1.ui.theme.Quiz1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Quiz1Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    QuizApp()
                }
            }
        }
    }
}

sealed class QuizScreen(val route: String) {
    data object Start : QuizScreen("start")
    data object Question1 : QuizScreen("question1")
    data object Question2 : QuizScreen("question2")
    data object Question3 : QuizScreen("question3")
    data object Question4 : QuizScreen("question4")
    data object Question5 : QuizScreen("question5")
    data object Results : QuizScreen("results")
}

@Composable
fun QuizApp() {
    val navController = rememberNavController()
    var score by remember { mutableStateOf(0) }
    var username by remember { mutableStateOf("") }  // Keep track of username

    NavHost(navController = navController, startDestination = QuizScreen.Start.route) {
        composable(QuizScreen.Start.route) {
            StartScreen(
                navController,
                username,
                onStartQuiz = { newUsername ->
                    username = newUsername  // Store username when starting quiz
                    score = 0  // Reset score when starting the quiz again
                }
            )
        }
        composable(QuizScreen.Question1.route) {
            QuestionScreen(
                navController = navController,
                questionNumber = 1,
                totalQuestions = 5,
                question = "What is the capital of Australia?",
                options = listOf("Sydney", "Melbourne", "Canberra", "Brisbane"),
                correctAnswer = "Canberra",
                nextRoute = QuizScreen.Question2.route,
                onAnswer = { isCorrect -> if (isCorrect) score++ }
            )
        }
        composable(QuizScreen.Question2.route) {
            QuestionScreen(
                navController = navController,
                questionNumber = 2,
                totalQuestions = 5,
                question = "What is the biggest city in Victoria?",
                options = listOf("Geelong", "Ballarat", "Melbourne", "Bendigo"),
                correctAnswer = "Melbourne",
                nextRoute = QuizScreen.Question3.route,
                onAnswer = { isCorrect -> if (isCorrect) score++ }
            )
        }
        composable(QuizScreen.Question3.route) {
            QuestionScreen(
                navController = navController,
                questionNumber = 3,
                totalQuestions = 5,
                question = "Which Australian animal is known for carrying its young in a pouch?",
                options = listOf("Koala", "Kangaroo", "Platypus", "Tasmanian Devil"),
                correctAnswer = "Kangaroo",
                nextRoute = QuizScreen.Question4.route,
                onAnswer = { isCorrect -> if (isCorrect) score++ }
            )
        }
        composable(QuizScreen.Question4.route) {
            QuestionScreen(
                navController = navController,
                questionNumber = 4,
                totalQuestions = 5,
                question = "What is the official name of Australia's indigenous people?",
                options = listOf("Maori", "Aboriginal and Torres Strait Islander Peoples", "Koori", "Anangu"),
                correctAnswer = "Aboriginal and Torres Strait Islander Peoples",
                nextRoute = QuizScreen.Question5.route,
                onAnswer = { isCorrect -> if (isCorrect) score++ }
            )
        }
        composable(QuizScreen.Question5.route) {
            QuestionScreen(
                navController = navController,
                questionNumber = 5,
                totalQuestions = 5,
                question = "Which of these is a famous Australian landmark?",
                options = listOf("Great Barrier Reef", "Uluru", "Sydney Opera House", "All of the above"),
                correctAnswer = "All of the above",
                nextRoute = QuizScreen.Results.route,
                onAnswer = { isCorrect -> if (isCorrect) score++ }
            )
        }
        composable(QuizScreen.Results.route) {
            ResultsScreen(
                navController = navController,
                score = score,
                totalQuestions = 5,
                onRestart = {
                    score = 0
                    navController.navigate(QuizScreen.Start.route) { popUpTo(QuizScreen.Start.route) { inclusive = true } }
                }  // Reset score when restarting the quiz from results
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartScreen(
    navController: androidx.navigation.NavController,
    username: String,
    onStartQuiz: (String) -> Unit
) {
    var inputUsername by remember { mutableStateOf(username) }
    var isUsernameValid by remember { mutableStateOf(true) }  // Track if the username is valid

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Australian Quiz App",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Username input field
        TextField(
            value = inputUsername,
            onValueChange = { inputUsername = it },
            label = { Text("Enter Username") },
            isError = !isUsernameValid,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        // Error message if the username is invalid
        if (!isUsernameValid) {
            Text(
                text = "Username is required",
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall
            )
        }

        // Start quiz button
        Button(
            onClick = {
                if (inputUsername.isNotBlank()) {
                    onStartQuiz(inputUsername)  // Pass the username when starting the quiz
                    navController.navigate(QuizScreen.Question1.route)
                } else {
                    isUsernameValid = false  // Set validation to false if username is empty
                }
            },
            modifier = Modifier.width(200.dp)
        ) {
            Text("Start Quiz")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionScreen(
    navController: androidx.navigation.NavController,
    questionNumber: Int,
    totalQuestions: Int,
    question: String,
    options: List<String>,
    correctAnswer: String,
    nextRoute: String,
    onAnswer: (Boolean) -> Unit
) {
    var selectedOption by remember { mutableStateOf<String?>(null) }
    var isSubmitted by remember { mutableStateOf(false) }
    var isCorrect by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 80.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        // Progress bar with increased width and proper padding
        LinearProgressIndicator(
            progress = questionNumber.toFloat() / totalQuestions.toFloat(),
            modifier = Modifier
                .fillMaxWidth(0.85f) // 85% of screen width
                .height(8.dp)
                .padding(bottom = 24.dp)
                .align(Alignment.CenterHorizontally),
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Question $questionNumber of $totalQuestions",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = question,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        options.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedOption == option,
                    onClick = {
                        if (!isSubmitted) {
                            selectedOption = option
                        }
                    },
                    enabled = !isSubmitted
                )
                Text(
                    text = option,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 12.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (!isSubmitted) {
            Button(
                onClick = {
                    isSubmitted = true
                    isCorrect = selectedOption == correctAnswer
                    onAnswer(isCorrect)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = selectedOption != null
            ) {
                Text("Submit Answer")
            }
        } else {
            Text(
                text = if (isCorrect) {
                    "Correct! $correctAnswer is the right answer."
                } else {
                    "Incorrect. The correct answer is $correctAnswer."
                },
                style = MaterialTheme.typography.bodyLarge,
                color = if (isCorrect) {
                    Color.Green
                } else {
                    Color.Red
                },
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        navController.navigate(QuizScreen.Start.route) {
                            popUpTo(QuizScreen.Start.route) { inclusive = true }
                        }
                    },
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                ) {
                    Text("Start Over")
                }

                Button(
                    onClick = {
                        navController.navigate(nextRoute) {
                            popUpTo(QuizScreen.Question1.route) { inclusive = false }
                        }
                    },
                    modifier = Modifier.weight(1f).padding(start = 8.dp)
                ) {
                    Text(if (nextRoute == QuizScreen.Results.route) "See Results" else "Next Question")
                }
            }
        }
    }
}

@Composable
fun ResultsScreen(
    navController: androidx.navigation.NavController,
    score: Int,
    totalQuestions: Int,
    onRestart: () -> Unit
) {
    val percentage = score.toFloat() / totalQuestions.toFloat() * 100

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Quiz Results",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Text(
            text = "Your score: $score out of $totalQuestions",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Percentage: ${"%.1f".format(percentage)}%",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        LinearProgressIndicator(
            progress = percentage / 100,
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(16.dp)
                .padding(horizontal = 32.dp, vertical = 16.dp),
            color = when {
                percentage >= 80 -> Color.Green
                percentage >= 50 -> Color.Yellow
                else -> Color.Red
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                onRestart()  // Reset score when starting new quiz from results screen
                navController.navigate(QuizScreen.Start.route) {
                    popUpTo(QuizScreen.Start.route) { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Take New Quiz")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StartScreenPreview() {
    Quiz1Theme {
        StartScreen(rememberNavController(), "") {}
    }
}

@Preview(showBackground = true)
@Composable
fun QuestionScreenPreview() {
    Quiz1Theme {
        QuestionScreen(
            navController = rememberNavController(),
            questionNumber = 1,
            totalQuestions = 5,
            question = "What is the capital of Australia?",
            options = listOf("Sydney", "Melbourne", "Canberra", "Brisbane"),
            correctAnswer = "Canberra",
            nextRoute = QuizScreen.Question2.route,
            onAnswer = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ResultsScreenPreview() {
    Quiz1Theme {
        ResultsScreen(
            navController = rememberNavController(),
            score = 3,
            totalQuestions = 5,
            onRestart = {}
        )
    }
}
