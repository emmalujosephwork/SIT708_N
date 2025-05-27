package com.example.a41p

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

class TaskRepository(context: Context) {

    private val databaseHelper = TaskDatabaseHelper(context)
    private var database: SQLiteDatabase? = null

    init {
        database = databaseHelper.writableDatabase
    }

    // Create (Insert) Task
    fun insertTask(task: Task): Long {
        val values = ContentValues().apply {
            put(TaskDatabaseHelper.COLUMN_TITLE, task.title)
            put(TaskDatabaseHelper.COLUMN_DESCRIPTION, task.description)
            put(TaskDatabaseHelper.COLUMN_DUE_DATE, task.dueDate)
        }
        return database?.insert(TaskDatabaseHelper.TABLE_TASKS, null, values) ?: -1
    }

    // Read (Get All) Tasks
    fun getAllTasks(): List<Task> {
        val tasks = mutableListOf<Task>()
        val cursor: Cursor = database?.query(
            TaskDatabaseHelper.TABLE_TASKS,
            arrayOf(
                TaskDatabaseHelper.COLUMN_ID,
                TaskDatabaseHelper.COLUMN_TITLE,
                TaskDatabaseHelper.COLUMN_DESCRIPTION,
                TaskDatabaseHelper.COLUMN_DUE_DATE
            ),
            null, null, null, null, TaskDatabaseHelper.COLUMN_DUE_DATE
        ) ?: return tasks

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.COLUMN_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.COLUMN_TITLE))
            val description = cursor.getString(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.COLUMN_DESCRIPTION))
            val dueDate = cursor.getString(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.COLUMN_DUE_DATE))

            tasks.add(Task(id, title, description, dueDate))
        }
        cursor.close()
        return tasks
    }

    // Update Task
    fun updateTask(task: Task): Int {
        val values = ContentValues().apply {
            put(TaskDatabaseHelper.COLUMN_TITLE, task.title)
            put(TaskDatabaseHelper.COLUMN_DESCRIPTION, task.description)
            put(TaskDatabaseHelper.COLUMN_DUE_DATE, task.dueDate)
        }
        return database?.update(
            TaskDatabaseHelper.TABLE_TASKS,
            values,
            "${TaskDatabaseHelper.COLUMN_ID} = ?",
            arrayOf(task.id.toString())
        ) ?: 0
    }

    // Delete Task
    fun deleteTask(task: Task) {
        database?.delete(
            TaskDatabaseHelper.TABLE_TASKS,
            "${TaskDatabaseHelper.COLUMN_ID} = ?",
            arrayOf(task.id.toString())
        )
    }

    // Close Database connection
    fun close() {
        database?.close()
    }
}
