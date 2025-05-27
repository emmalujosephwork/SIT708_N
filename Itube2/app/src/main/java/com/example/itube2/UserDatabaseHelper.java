package com.example.itube2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class UserDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "itube.db";
    public static final int DATABASE_VERSION = 2; // Increment version to force database update

    // Tables
    public static final String TABLE_USERS = "users";
    public static final String TABLE_PLAYLIST = "playlist";

    // Users table columns
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FULL_NAME = "full_name";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";

    // Playlist table columns
    public static final String COLUMN_VIDEO_URL = "video_url"; // Video URL column
    public static final String COLUMN_USERNAME_PLAYLIST = "username"; // Add username column

    // SQL query to create the users table
    private static final String CREATE_TABLE_USERS =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_FULL_NAME + " TEXT, " +
                    COLUMN_USERNAME + " TEXT, " +
                    COLUMN_PASSWORD + " TEXT);";

    // SQL query to create the playlist table (with username and video URL)
    private static final String CREATE_TABLE_PLAYLIST =
            "CREATE TABLE " + TABLE_PLAYLIST + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_VIDEO_URL + " TEXT, " +
                    COLUMN_USERNAME_PLAYLIST + " TEXT);"; // Add the username to the playlist table

    public UserDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_PLAYLIST); // Create the playlist table with username and video URL
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYLIST); // Drop playlist table if exists
        onCreate(db); // Recreate the tables on upgrade
    }

    // Method to add a video URL to the playlist with the username
    public long addToPlaylist(String username, String videoUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_VIDEO_URL, videoUrl);
        values.put(COLUMN_USERNAME_PLAYLIST, username); // Store the username along with the video URL
        return db.insert(TABLE_PLAYLIST, null, values); // Insert the video URL into the playlist table
    }

    // Method to retrieve all videos from the playlist for a specific user
    public ArrayList<String> getPlaylistForUser(String username) {
        ArrayList<String> playlist = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PLAYLIST + " WHERE " + COLUMN_USERNAME_PLAYLIST + "=?", new String[]{username});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String videoUrl = cursor.getString(cursor.getColumnIndex(COLUMN_VIDEO_URL));
                playlist.add(videoUrl);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return playlist; // Return list of video URLs in the playlist for the specified user
    }
}
