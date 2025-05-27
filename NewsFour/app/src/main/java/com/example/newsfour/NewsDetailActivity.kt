package com.example.newsfour

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.newsfour.databinding.ActivityNewsDetailBinding

class NewsDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout
        val binding = ActivityNewsDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get data from the intent
        val title = intent.getStringExtra("newsTitle")
        val description = intent.getStringExtra("newsDescription")
        val imageRes = intent.getIntExtra("newsImageRes", 0)

        // Set data to views
        binding.newsTitleTextView.text = title
        binding.newsDescriptionTextView.text = description
        binding.newsImage.setImageResource(imageRes)

        // Set up back button to go back to MainActivity
        binding.backButton.setOnClickListener {
            // Go back to MainActivity
            val intent = Intent(this, MainActivity::class.java)  // Launch MainActivity
            startActivity(intent)
            finish()  // Close NewsDetailActivity so the user can't go back to it using the back button
        }
    }
}
