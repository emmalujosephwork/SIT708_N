package com.example.a21ptask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.a21ptask.ui.theme._21PtaskTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            _21PtaskTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    UnitConverterApp()
                }
            }
        }
    }
}

@Composable
fun UnitConverterApp() {
    val lengthUnits = listOf("inch", "foot", "yard", "mile", "cm", "km")
    val weightUnits = listOf("pound", "ounce", "ton", "g", "kg")
    val tempUnits = listOf("Celsius", "Fahrenheit", "Kelvin")
    val allUnits = lengthUnits + weightUnits + tempUnits

    var sourceUnit by remember { mutableStateOf(allUnits[0]) }
    var destUnit by remember { mutableStateOf(allUnits[1]) }
    var inputValue by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    val categoryUnits = when (sourceUnit) {
        in lengthUnits -> lengthUnits
        in weightUnits -> weightUnits
        in tempUnits -> tempUnits
        else -> allUnits
    }

    LaunchedEffect(sourceUnit) {
        if (destUnit !in categoryUnits) destUnit = categoryUnits[0]
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.4f
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Unit Converter", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = inputValue,
                onValueChange = { inputValue = it },
                label = { Text("Enter value") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("From:")
            DropdownMenuUnitSelector(allUnits, sourceUnit) { sourceUnit = it }

            Spacer(modifier = Modifier.height(8.dp))

            Text("To:")
            DropdownMenuUnitSelector(categoryUnits, destUnit) { destUnit = it }

            Spacer(modifier = Modifier.height(16.dp))

            // ✅ Validation and Error Handling
            Button(onClick = {
                result = when {
                    inputValue.isBlank() -> {
                        "Please enter a value!"
                    }

                    sourceUnit == destUnit -> {
                        try {
                            val value = inputValue.toDouble()
                            "Same unit: $value"
                        } catch (e: Exception) {
                            "Invalid input!"
                        }
                    }

                    else -> {
                        try {
                            val value = inputValue.toDouble()
                            val converted = convertUnits(sourceUnit, destUnit, value)
                            if (converted.isNaN()) "Invalid conversion!" else "Result: $converted"
                        } catch (e: Exception) {
                            "Invalid input!"
                        }
                    }
                }
            }) {
                Text("Convert")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(result, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun DropdownMenuUnitSelector(units: List<String>, selected: String, onSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(selected)
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown arrow"
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            units.forEach {
                DropdownMenuItem(
                    text = { Text(it) },
                    onClick = {
                        onSelected(it)
                        expanded = false
                    }
                )
            }
        }
    }
}

fun convertUnits(from: String, to: String, value: Double): Double {
    val lengthMap = mapOf(
        "inch" to 2.54,
        "foot" to 30.48,
        "yard" to 91.44,
        "mile" to 160934.0,
        "cm" to 1.0,
        "km" to 100000.0
    )

    val weightMap = mapOf(
        "pound" to 453.592,
        "ounce" to 28.3495,
        "ton" to 907185.0,
        "g" to 1.0,
        "kg" to 1000.0
    )

    return when {
        from in lengthMap && to in lengthMap -> value * lengthMap[from]!! / lengthMap[to]!!
        from in weightMap && to in weightMap -> value * weightMap[from]!! / weightMap[to]!!
        from == "Celsius" && to == "Fahrenheit" -> (value * 1.8) + 32
        from == "Fahrenheit" && to == "Celsius" -> (value - 32) / 1.8
        from == "Celsius" && to == "Kelvin" -> value + 273.15
        from == "Kelvin" && to == "Celsius" -> value - 273.15
        else -> Double.NaN
    }
}
