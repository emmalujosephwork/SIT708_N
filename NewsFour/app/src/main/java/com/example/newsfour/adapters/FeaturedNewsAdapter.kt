package com.example.newsfour.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsfour.databinding.ItemNewsBinding
import com.example.newsfour.models.NewsItem

class FeaturedNewsAdapter(private val featuredNewsList: List<NewsItem>) : RecyclerView.Adapter<FeaturedNewsAdapter.FeaturedNewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeaturedNewsViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FeaturedNewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FeaturedNewsViewHolder, position: Int) {
        val newsItem = featuredNewsList[position]
        holder.bind(newsItem)
    }

    override fun getItemCount() = featuredNewsList.size

    inner class FeaturedNewsViewHolder(private val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(newsItem: NewsItem) {
            binding.titleTextView.text = newsItem.title
            binding.descriptionTextView.text = newsItem.description

            // Use Glide to load image into ImageView
            Glide.with(binding.root.context)
                .load(newsItem.imageRes)  // Image URL from NewsItem
                .into(binding.newsImage)   // ImageView where the image should be loaded
        }
    }
}
