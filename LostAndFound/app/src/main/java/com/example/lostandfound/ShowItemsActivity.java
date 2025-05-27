package com.example.lostandfound;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;

public class ShowItemsActivity extends AppCompatActivity {

    private ListView itemsListView;
    private AdvertDatabaseHelper dbHelper;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_items);

        // Initialize UI components
        itemsListView = findViewById(R.id.itemsListView);
        backButton = findViewById(R.id.backButton);

        // Initialize the database helper
        dbHelper = new AdvertDatabaseHelper(this);

        // Fetch and display found items
        displayFoundItems();

        // Set item click listener to navigate to item details page
        itemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the ID of the selected item
                Cursor cursor = (Cursor) itemsListView.getItemAtPosition(position);
                @SuppressLint("Range") int itemId = cursor.getInt(cursor.getColumnIndex(AdvertDatabaseHelper.COLUMN_ID));

                // Create an Intent to open the ItemDetailsActivity
                Intent intent = new Intent(ShowItemsActivity.this, ItemDetailsActivity.class);
                intent.putExtra("ITEM_ID", itemId); // Pass the item ID
                startActivity(intent);
            }
        });

        // Set OnClickListener for the back button
        backButton.setOnClickListener(v -> finish()); // Close the current activity (go back)
    }

    private void displayFoundItems() {
        // Get readable database
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Query to fetch all found items
        Cursor cursor = db.query(
                AdvertDatabaseHelper.TABLE_ADVERTS,    // Table name
                null,                                  // All columns
                AdvertDatabaseHelper.COLUMN_POST_TYPE + " = ?",  // Selection (only found items)
                new String[]{"Found"},                // Selection arguments (Found items only)
                null,                                  // Group By
                null,                                  // Having
                null                                   // Order By
        );

        // Define the columns to display in the ListView
        String[] fromColumns = {AdvertDatabaseHelper.COLUMN_NAME, AdvertDatabaseHelper.COLUMN_DESCRIPTION};
        int[] toViews = {android.R.id.text1, android.R.id.text2};

        // Create an adapter to bind the cursor to the ListView
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_2, // Default layout for 2 lines
                cursor,
                fromColumns,
                toViews,
                0
        );

        // Set the adapter for the ListView
        itemsListView.setAdapter(adapter);
    }
}
