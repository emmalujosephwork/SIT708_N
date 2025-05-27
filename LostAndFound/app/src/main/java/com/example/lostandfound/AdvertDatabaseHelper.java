package com.example.lostandfound;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AdvertDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "lost_and_found.db";
    private static final int DATABASE_VERSION = 1;

    // Table and columns
    public static final String TABLE_ADVERTS = "adverts";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_POST_TYPE = "post_type"; // "Lost" or "Found"

    // SQL statement to create the table
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_ADVERTS + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NAME + " TEXT, " +
            COLUMN_PHONE + " TEXT, " +
            COLUMN_DESCRIPTION + " TEXT, " +
            COLUMN_DATE + " TEXT, " +
            COLUMN_LOCATION + " TEXT, " +
            COLUMN_POST_TYPE + " TEXT);";

    // Constructor
    public AdvertDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when the database is created
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    // Called when the database version is upgraded
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADVERTS);
        onCreate(db);
    }
}
