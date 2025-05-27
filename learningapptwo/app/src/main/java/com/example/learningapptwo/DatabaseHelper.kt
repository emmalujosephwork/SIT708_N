package com.example.learningapptwo

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// Database helper to manage user data storage
class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "userDatabase"
        private const val DATABASE_VERSION = 2 // Incremented version to add the interests table
        private const val TABLE_NAME = "users"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_PASSWORD = "password"
        private const val COLUMN_INTEREST = "interest" // Column to store selected interests
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_USER_TABLE = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_USERNAME TEXT," +
                "$COLUMN_PASSWORD TEXT," +
                "$COLUMN_INTEREST TEXT)"
        db?.execSQL(CREATE_USER_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < newVersion) {
            db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
            onCreate(db)
        }
    }

    // Function to add a new user to the database
    fun addUser(username: String, password: String) {
        val db = writableDatabase
        val query = "INSERT INTO $TABLE_NAME ($COLUMN_USERNAME, $COLUMN_PASSWORD) VALUES (?, ?)"
        val statement = db.compileStatement(query)
        statement.bindString(1, username)
        statement.bindString(2, password)
        statement.executeInsert()
        db.close()
    }

    // Function to add selected interests for a user
    fun addInterests(username: String, interests: String) {
        val db = writableDatabase
        val query = "UPDATE $TABLE_NAME SET $COLUMN_INTEREST = ? WHERE $COLUMN_USERNAME = ?"
        val statement = db.compileStatement(query)
        statement.bindString(1, interests)
        statement.bindString(2, username)
        statement.executeUpdateDelete()
        db.close()
    }
}
