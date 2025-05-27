package com.example.learningapptwo

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
class QuizActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuizScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen() {
    val viewModel: QuizViewModel = viewModel()
    val context = LocalContext.current
    val state by viewModel.state

    LaunchedEffect(Unit) {
        if (state.questions.isEmpty()) {
            viewModel.loadQuestions()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Personalized Quiz") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                !state.error.isNullOrEmpty() -> {
                    Text(
                        text = "Error: ${state.error}",
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                    Button(onClick = { viewModel.loadQuestions() }) {
                        Text("Retry")
                    }
                }

                state.questions.isEmpty() -> {
                    Text("No questions available")
                }

                else -> {
                    QuizContent(
                        state = state,
                        onAnswerSelected = { answer ->
                            viewModel.selectAnswer(answer)
                        },
                        onNextQuestion = {
                            viewModel.nextQuestion()
                        },
                        onSubmit = {
                            viewModel.submitAnswers()
                        },
                        onBackToTasks = {
                            context.startActivity(
                                Intent(context, TaskListActivity::class.java)
                            )
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizContent(
    state: QuizState,
    onAnswerSelected: (String) -> Unit,
    onNextQuestion: () -> Unit,
    onSubmit: () -> Unit,
    onBackToTasks: () -> Unit
) {
    val currentQuestion = state.questions[state.currentQuestionIndex]

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Question ${state.currentQuestionIndex + 1} of ${state.questions.size}",
            style = MaterialTheme.typography.titleLarge
        )

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = currentQuestion.question,
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                currentQuestion.options.forEach { option ->
                    val isSelected = state.selectedAnswer == option
                    val isCorrect = state.showResults && option == currentQuestion.correctAnswer
                    val isIncorrect = state.showResults && isSelected && option != currentQuestion.correctAnswer

                    val containerColor = when {
                        isCorrect -> MaterialTheme.colorScheme.primaryContainer
                        isIncorrect -> MaterialTheme.colorScheme.errorContainer
                        isSelected -> MaterialTheme.colorScheme.secondaryContainer
                        else -> MaterialTheme.colorScheme.surfaceVariant
                    }

                    val contentColor = when {
                        isCorrect -> MaterialTheme.colorScheme.onPrimaryContainer
                        isIncorrect -> MaterialTheme.colorScheme.onErrorContainer
                        isSelected -> MaterialTheme.colorScheme.onSecondaryContainer
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    }

                    OutlinedButton(
                        onClick = { if (!state.showResults) onAnswerSelected(option) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = containerColor,
                            contentColor = contentColor
                        ),
                        enabled = !state.showResults,
                        border = if (!state.showResults) {
                            ButtonDefaults.outlinedButtonBorder
                        } else {
                            BorderStroke(
                                1.dp,
                                when {
                                    isCorrect -> MaterialTheme.colorScheme.primary
                                    isIncorrect -> MaterialTheme.colorScheme.error
                                    else -> MaterialTheme.colorScheme.outline
                                }
                            )
                        }
                    ) {
                        Text(
                            text = option,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Start
                        )
                    }
                }
            }
        }

        if (state.showResults) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Explanation:",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(
                        text = currentQuestion.explanation,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        if (state.currentQuestionIndex < state.questions.size - 1) {
            Button(
                onClick = onNextQuestion,
                modifier = Modifier.fillMaxWidth(),
                enabled = state.selectedAnswer != null || state.showResults
            ) {
                Text("Next Question")
            }
        } else {
            Button(
                onClick = onSubmit,
                modifier = Modifier.fillMaxWidth(),
                enabled = state.selectedAnswer != null || state.showResults
            ) {
                Text(if (state.showResults) "View Results" else "Submit")
            }
        }

        if (state.showResults) {
            Text(
                text = "Score: ${state.score}/${state.questions.size}",
                style = MaterialTheme.typography.titleMedium
            )
        }

        OutlinedButton(
            onClick = onBackToTasks,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Tasks")
        }
    }
}

class QuizViewModel : ViewModel() {
    private val _state = mutableStateOf(QuizState())
    val state = _state

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    fun loadQuestions() {
        coroutineScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val questions = generateQuestions()
                _state.value = _state.value.copy(
                    questions = questions,
                    isLoading = false,
                    answers = MutableList(questions.size) { null }
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error"
                )
            }
        }
    }

    fun selectAnswer(answer: String) {
        _state.value = _state.value.copy(
            selectedAnswer = answer,
            answers = _state.value.answers.toMutableList().apply {
                this[_state.value.currentQuestionIndex] = answer
            },
            showResults = false
        )
    }

    fun nextQuestion() {
        _state.value = _state.value.copy(
            currentQuestionIndex = (_state.value.currentQuestionIndex + 1)
                .coerceAtMost(_state.value.questions.size - 1),
            selectedAnswer = _state.value.answers[_state.value.currentQuestionIndex + 1],
            showResults = false
        )
    }

    fun submitAnswers() {
        _state.value = _state.value.copy(
            showResults = true,
            score = _state.value.questions.mapIndexed { index, question ->
                if (_state.value.answers[index] == question.correctAnswer) 1 else 0
            }.sum()
        )
    }

    private fun generateQuestions(): List<Question> {
        return listOf(
            Question(
                question = "What is the primary language for Android development?",
                options = listOf("Java", "Kotlin", "Dart", "Swift"),
                correctAnswer = "Kotlin",
                explanation = "Kotlin is now the preferred language for Android development."
            ),
            Question(
                question = "What is a Composable in Jetpack Compose?",
                options = listOf(
                    "A UI building block",
                    "A database entity",
                    "A network request",
                    "A build script"
                ),
                correctAnswer = "A UI building block",
                explanation = "Composables are functions that define UI elements in Compose."
            ),
            Question(
                question = "What is the purpose of a ViewModel in Android?",
                options = listOf(
                    "To handle UI rendering",
                    "To manage UI-related data in a lifecycle-conscious way",
                    "To create layout files",
                    "To define database schemas"
                ),
                correctAnswer = "To manage UI-related data in a lifecycle-conscious way",
                explanation = "ViewModel is designed to store and manage UI-related data in a lifecycle conscious way."
            ),
            Question(
                question = "What does LazyColumn do in Jetpack Compose?",
                options = listOf(
                    "Creates a static list of items",
                    "Only renders items that are visible on screen",
                    "Forces all items to load at once",
                    "Works only with images"
                ),
                correctAnswer = "Only renders items that are visible on screen",
                explanation = "LazyColumn only composes and lays out items that are currently visible."
            ),
            Question(
                question = "What is the main benefit of using Room database?",
                options = listOf(
                    "It provides a visual database editor",
                    "It automatically generates SQL code at runtime",
                    "It reduces boilerplate code and provides compile-time checks",
                    "It works without any SQL knowledge"
                ),
                correctAnswer = "It reduces boilerplate code and provides compile-time checks",
                explanation = "Room provides an abstraction layer over SQLite with compile-time verification of SQL queries."
            )
        )
    }
}

data class QuizState(
    val questions: List<Question> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val selectedAnswer: String? = null,
    val answers: List<String?> = emptyList(),
    val showResults: Boolean = false,
    val score: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null
)

data class Question(
    val question: String,
    val options: List<String>,
    val correctAnswer: String,
    val explanation: String
)