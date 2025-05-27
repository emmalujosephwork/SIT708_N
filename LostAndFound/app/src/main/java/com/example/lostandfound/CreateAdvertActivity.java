package com.example.lostandfound;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CreateAdvertActivity extends AppCompatActivity {

    private EditText nameField, phoneField, descriptionField, dateField, locationField;
    private RadioGroup postTypeGroup;
    private RadioButton lostRadio, foundRadio;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_advert);

        // Initialize UI components
        nameField = findViewById(R.id.nameField);
        phoneField = findViewById(R.id.phoneField);
        descriptionField = findViewById(R.id.descriptionField);
        dateField = findViewById(R.id.dateField);
        locationField = findViewById(R.id.locationField);
        postTypeGroup = findViewById(R.id.postTypeGroup);
        lostRadio = findViewById(R.id.lostRadio);
        foundRadio = findViewById(R.id.foundRadio);
        saveButton = findViewById(R.id.saveButton);

        // Set onClickListener for Save Button
        saveButton.setOnClickListener(v -> saveAdvert());
    }

    private void saveAdvert() {
        // Get user input
        String name = nameField.getText().toString();
        String phone = phoneField.getText().toString();
        String description = descriptionField.getText().toString();
        String date = dateField.getText().toString();
        String location = locationField.getText().toString();
        String postType = lostRadio.isChecked() ? "Lost" : "Found";

        // Insert into SQLite database
        AdvertDatabaseHelper dbHelper = new AdvertDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(AdvertDatabaseHelper.COLUMN_NAME, name);
        values.put(AdvertDatabaseHelper.COLUMN_PHONE, phone);
        values.put(AdvertDatabaseHelper.COLUMN_DESCRIPTION, description);
        values.put(AdvertDatabaseHelper.COLUMN_DATE, date);
        values.put(AdvertDatabaseHelper.COLUMN_LOCATION, location);
        values.put(AdvertDatabaseHelper.COLUMN_POST_TYPE, postType);

        // Insert data into database
        long newRowId = db.insert(AdvertDatabaseHelper.TABLE_ADVERTS, null, values);
        db.close();

        if (newRowId != -1) {
            // Show a Toast message indicating success
            Toast.makeText(CreateAdvertActivity.this, "Advert saved!", Toast.LENGTH_SHORT).show();

            // Redirect to the MainActivity (Home page)
            Intent intent = new Intent(CreateAdvertActivity.this, MainActivity.class);
            startActivity(intent);

            // Optional: Finish the current activity so user can't go back to it
            finish();
        } else {
            // Show a Toast message indicating failure
            Toast.makeText(CreateAdvertActivity.this, "Error saving advert.", Toast.LENGTH_SHORT).show();
        }
    }
}
