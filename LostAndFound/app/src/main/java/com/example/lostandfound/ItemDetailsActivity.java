package com.example.lostandfound;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ItemDetailsActivity extends AppCompatActivity {

    private TextView itemName, itemDescription, itemDate, itemLocation;
    private Button removeButton, backButton;
    private int itemId; // The ID of the item that was clicked

    private AdvertDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        // Initialize the UI components
        itemName = findViewById(R.id.itemName);
        itemDescription = findViewById(R.id.itemDescription);
        itemDate = findViewById(R.id.itemDate);
        itemLocation = findViewById(R.id.itemLocation);
        removeButton = findViewById(R.id.removeButton);
        backButton = findViewById(R.id.backButton);

        // Initialize the database helper
        dbHelper = new AdvertDatabaseHelper(this);

        // Get the item ID passed from ShowItemsActivity
        Intent intent = getIntent();
        itemId = intent.getIntExtra("ITEM_ID", -1);

        // Fetch item details from the database
        displayItemDetails();

        // Set the action for the "Remove" button
        removeButton.setOnClickListener(v -> removeItem());

        // Set the action for the "Back" button
        backButton.setOnClickListener(v -> finish()); // Go back to previous activity
    }

    @SuppressLint("Range")
    private void displayItemDetails() {
        // Get readable database
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Query to fetch the selected item
        Cursor cursor = db.query(
                AdvertDatabaseHelper.TABLE_ADVERTS,    // Table name
                null,                                  // All columns
                AdvertDatabaseHelper.COLUMN_ID + " = ?",  // Selection (specific item by ID)
                new String[]{String.valueOf(itemId)},   // Selection arguments (item ID)
                null,                                  // Group By
                null,                                  // Having
                null                                   // Order By
        );

        if (cursor != null && cursor.moveToFirst()) {
            // Populate the views with data from the database
            itemName.setText(cursor.getString(cursor.getColumnIndex(AdvertDatabaseHelper.COLUMN_NAME)));
            itemDescription.setText(cursor.getString(cursor.getColumnIndex(AdvertDatabaseHelper.COLUMN_DESCRIPTION)));
            itemDate.setText(cursor.getString(cursor.getColumnIndex(AdvertDatabaseHelper.COLUMN_DATE)));
            itemLocation.setText(cursor.getString(cursor.getColumnIndex(AdvertDatabaseHelper.COLUMN_LOCATION)));
        }

        cursor.close();
        db.close();
    }

    private void removeItem() {
        // Get writable database
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Delete the item from the database
        int rowsDeleted = db.delete(
                AdvertDatabaseHelper.TABLE_ADVERTS,
                AdvertDatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(itemId)}
        );

        if (rowsDeleted > 0) {
            // Show a success message
            Toast.makeText(this, "Item removed!", Toast.LENGTH_SHORT).show();

            // Redirect to the ShowItemsActivity (to update the list)
            Intent intent = new Intent(ItemDetailsActivity.this, ShowItemsActivity.class);
            startActivity(intent);

            // Optionally, finish the current activity so the user cannot go back to it
            finish();
        } else {
            // Show an error message if the item wasn't removed
            Toast.makeText(this, "Error removing item.", Toast.LENGTH_SHORT).show();
        }
    }
}
