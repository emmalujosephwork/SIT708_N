package com.example.newsfour.models

data class NewsItem(
    val title: String,
    val description: String,
    val imageRes: Int  // Change from imageUrl (String) to imageRes (Int) for local drawable
)
