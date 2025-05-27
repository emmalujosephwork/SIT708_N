package com.example.newsfour.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newsfour.NewsDetailActivity
import com.example.newsfour.R
import com.example.newsfour.databinding.ItemNewsBinding
import com.example.newsfour.models.NewsItem

class NewsAdapter(private val newsList: List<NewsItem>, private val context: Context) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val newsItem = newsList[position]
        holder.bind(newsItem, position)  // Pass the position along with the newsItem
    }

    override fun getItemCount() = newsList.size

    inner class NewsViewHolder(private val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(newsItem: NewsItem, position: Int) {
            binding.titleTextView.text = newsItem.title
            binding.descriptionTextView.text = newsItem.description

            // Show the image based on whether the position is odd or even
            if (position % 2 == 0) {  // Even index (0-based index)
                binding.newsImage.setImageResource(R.drawable.news2)  // Use news2 for even
            } else {  // Odd index
                binding.newsImage.setImageResource(R.drawable.news1)  // Use news1 for odd
            }

            // Handle click event on each news item
            binding.root.setOnClickListener {
                // Pass the selected data to NewsDetailActivity
                val intent = Intent(context, NewsDetailActivity::class.java)
                intent.putExtra("newsTitle", newsItem.title)
                intent.putExtra("newsDescription", newsItem.description)
                intent.putExtra("newsImageRes", if (position % 2 == 0) R.drawable.news2 else R.drawable.news1)
                context.startActivity(intent)
            }
        }
    }
}
