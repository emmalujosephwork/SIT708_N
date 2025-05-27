package com.example.newsfour

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsfour.adapters.NewsAdapter
import com.example.newsfour.adapters.FeaturedNewsAdapter
import com.example.newsfour.databinding.ActivityMainBinding
import com.example.newsfour.models.NewsItem

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using ViewBinding
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Generate the sample news list for both horizontal and vertical lists
        val featuredNewsList = generateFeaturedNews()
        val newsList = generateSampleNews()

        // Set up the Horizontal RecyclerView for Featured News (Horizontal Scroll)
        val featuredNewsAdapter = FeaturedNewsAdapter(featuredNewsList)
        binding.featuredRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.featuredRecyclerView.adapter = featuredNewsAdapter

        // Set up the Vertical RecyclerView for Main News with Grid Layout (2 items per row)
        val newsAdapter = NewsAdapter(newsList, this@MainActivity)  // Pass context correctly here
        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)  // 2 items per row
        binding.recyclerView.adapter = newsAdapter
    }

    private fun generateFeaturedNews(): List<NewsItem> {
        return List(5) { index ->
            NewsItem(
                title = "Featured News Title $index",
                description = "This is a sample description for featured news $index",
                imageRes = R.drawable.news1  // Using a local drawable resource for featured news
            )
        }
    }

    private fun generateSampleNews(): List<NewsItem> {
        return List(20) { index ->
            NewsItem(
                title = "Sample News Title $index",
                description = "This is a sample description for news $index",
                imageRes = if (index % 2 == 0) R.drawable.news2 else R.drawable.news1  // Use news2 for even index, news1 for odd
            )
        }
    }
}
