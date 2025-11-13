package com.example.huerto_hogar.model

class Blog(
    val title: String,
    val bannerImg: Int,
    var summary: String,
    val bodyText: List<String>,
    val authorImg: Int,
    val authorName: String,
    val publishDate: String,
    val tags: List<String>
);