package com.example.a41p

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Sort
import com.example.a41p.ui.theme._41PtaskTheme
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {

    private val taskRepository by lazy { TaskRepository(context = this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            _41PtaskTheme {
                TaskManagerApp()
            }
        }
    }

    @Composable
    fun TaskManagerApp() {
        var taskTitle by remember { mutableStateOf("") }
        var taskDescription by remember { mutableStateOf("") }
        var taskDueDate by remember { mutableStateOf("") }
        val tasks = remember { mutableStateListOf<Task>() }
        var editingTask by remember { mutableStateOf<Task?>(null) }

        // Load tasks from the database
        LaunchedEffect(Unit) {
            tasks.clear()
            tasks.addAll(taskRepository.getAllTasks().sortedBy { parseDate(it.dueDate) }) // Sort tasks by due date
        }

        // Dialog for editing a task
        if (editingTask != null) {
            EditTaskDialog(
                task = editingTask!!,
                onDismiss = { editingTask = null },
                onSave = { updatedTask ->
                    taskRepository.updateTask(updatedTask)
                    // Update the task in the list by replacing the old one
                    tasks.indexOfFirst { it.id == updatedTask.id }.takeIf { it >= 0 }?.let { index ->
                        tasks[index] = updatedTask
                    }
                    editingTask = null
                    // Show a toast after task is updated
                    Toast.makeText(applicationContext, "Task updated", Toast.LENGTH_SHORT).show()
                }
            )
        }

        // Adding padding and background to the entire layout
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp) // Padding for overall content
                .background(MaterialTheme.colorScheme.background) // Set the background color
                .padding(top = 48.dp), // Add padding to push content below the status bar
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp)) // Add more space at the top to avoid content being hidden behind the status bar

            Text("Task Manager", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp)) // Added space between title and inputs

            // Text fields for task title and description
            OutlinedTextField(
                value = taskTitle,
                onValueChange = { taskTitle = it },
                label = { Text("Task Title") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = taskDescription,
                onValueChange = { taskDescription = it },
                label = { Text("Task Description") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = taskDueDate,
                onValueChange = { taskDueDate = it },
                label = { Text("Due Date (yyyy-MM-dd)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Column to center the Add Task button
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Button to add task
                Button(onClick = {
                    if (taskTitle.isNotBlank() && taskDescription.isNotBlank()) {
                        val task = Task(
                            id = tasks.size + 1, // Dummy ID (can be auto-generated later)
                            title = taskTitle,
                            description = taskDescription,
                            dueDate = taskDueDate
                        )
                        taskRepository.insertTask(task)
                        tasks.add(task) // Update the list with the new task
                        taskTitle = ""
                        taskDescription = "" // Clear the input fields
                        taskDueDate = "" // Clear the due date field
                        Toast.makeText(applicationContext, "Task added", Toast.LENGTH_SHORT).show()
                    }
                }) {
                    Text("Add Task")
                }
                Spacer(modifier = Modifier.height(16.dp)) // Spacing between button and sort icon

                // Row for the Sort button aligned to the right
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = {
                        // Sorting the tasks based on the due date
                        tasks.sortBy { parseDate(it.dueDate) }
                    }) {
                        Icon(Icons.Filled.Sort, contentDescription = "Sort Tasks")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp)) // Spacing between button and task list

            // Display task list
            tasks.forEach { task ->
                TaskItem(
                    task = task,
                    onDelete = { deleteTask(task, tasks) },
                    onEdit = { editingTask = task } // Show edit dialog when task is clicked
                )
            }
        }
    }

    @Composable
    fun TaskItem(task: Task, onDelete: () -> Unit, onEdit: () -> Unit) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(task.title, style = MaterialTheme.typography.bodyLarge)
                Text(
                    task.description,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp) // Padding for description
                )
                Text(
                    task.dueDate,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp) // Padding for due date
                )
            }

            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Filled.Edit, contentDescription = "Edit Task")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete Task")
                }
            }
        }
    }

    private fun deleteTask(task: Task, tasks: MutableList<Task>) {
        taskRepository.deleteTask(task) // Delete from database
        tasks.remove(task) // Remove from list
        // Show a toast after task is deleted
        Toast.makeText(applicationContext, "Task deleted", Toast.LENGTH_SHORT).show()
    }

    private fun parseDate(dateString: String): Date {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.parse(dateString) ?: Date() // Return current date if parsing fails
    }
}

@Composable
fun EditTaskDialog(
    task: Task,
    onDismiss: () -> Unit,
    onSave: (Task) -> Unit
) {
    var editedTitle by remember { mutableStateOf(task.title) }
    var editedDescription by remember { mutableStateOf(task.description) }
    var editedDueDate by remember { mutableStateOf(task.dueDate) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Task") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = editedTitle,
                    onValueChange = { editedTitle = it },
                    label = { Text("Task Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = editedDescription,
                    onValueChange = { editedDescription = it },
                    label = { Text("Task Description") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = editedDueDate,
                    onValueChange = { editedDueDate = it },
                    label = { Text("Due Date (yyyy-MM-dd)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val updatedTask = task.copy(title = editedTitle, description = editedDescription, dueDate = editedDueDate)
                    onSave(updatedTask)
                    onDismiss()
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
