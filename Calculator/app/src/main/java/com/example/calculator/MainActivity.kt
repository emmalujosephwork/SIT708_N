package com.example.calculator

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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.ui.text.font.FontWeight
import com.example.calculator.ui.theme.CalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CalculatorScreen()
                }
            }
        }
    }
}

@Composable
fun CalculatorScreen() {
    var firstValue by remember { mutableStateOf("") }
    var secondValue by remember { mutableStateOf("") }
    var operation by remember { mutableStateOf<String?>(null) }
    var result by remember { mutableStateOf("") }

    val buttonColorAdd = if (operation == "Add") Color.Gray else MaterialTheme.colorScheme.primary
    val buttonColorSubtract = if (operation == "Subtract") Color.Gray else MaterialTheme.colorScheme.primary
    val buttonColorMultiply = if (operation == "Multiply") Color.Gray else MaterialTheme.colorScheme.primary
    val buttonColorDivide = if (operation == "Divide") Color.Gray else MaterialTheme.colorScheme.primary

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars) // Ensures the top padding accounts for status bar
            .padding(16.dp) // Add some extra padding for general spacing
    ) {
        Text("Enter two numbers to perform operations", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Input Fields
        TextField(
            value = firstValue,
            onValueChange = { firstValue = it },
            label = { Text("First Number") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )
        TextField(
            value = secondValue,
            onValueChange = { secondValue = it },
            label = { Text("Second Number") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Buttons for operations
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // First row for Add and Subtract
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { operation = "Add" },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColorAdd)
                ) {
                    Text("Add")
                }
                Button(
                    onClick = { operation = "Subtract" },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColorSubtract)
                ) {
                    Text("Subtract")
                }
            }

            // Second row for Multiply and Divide
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { operation = "Multiply" },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColorMultiply)
                ) {
                    Text("Multiply")
                }
                Button(
                    onClick = { operation = "Divide" },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColorDivide)
                ) {
                    Text("Divide")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Calculate Button
        Button(
            onClick = {
                val num1 = firstValue.toDoubleOrNull()
                val num2 = secondValue.toDoubleOrNull()

                if (num1 != null && num2 != null) {
                    result = when (operation) {
                        "Add" -> (num1 + num2).toString()
                        "Subtract" -> (num1 - num2).toString()
                        "Multiply" -> (num1 * num2).toString()
                        "Divide" -> {
                            if (num2 != 0.0) {
                                (num1 / num2).toString()
                            } else {
                                "Cannot divide by zero"
                            }
                        }
                        else -> "Invalid Operation"
                    }
                } else {
                    result = "Please enter valid numbers."
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Calculate")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display result with large bold text
        if (result.isNotEmpty()) {
            Text(
                "Result: $result",
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CalculatorScreenPreview() {
    CalculatorTheme {
        CalculatorScreen()
    }
}
