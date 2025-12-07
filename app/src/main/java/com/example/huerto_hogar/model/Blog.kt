package com.example.huerto_hogar.model

import com.google.gson.annotations.SerializedName

/**
 * Modelo de Blog (DTO) que coincide con el backend
 * 
 * Backend envía: id, title, bannerImg, summary, bodyText (String), 
 * authorImg, authorName, publishDate, tags (puede estar ausente)
 */
data class Blog(
    @SerializedName("id")
    val id: Int = 0,
    
    @SerializedName("title")
    val title: String,
    
    @SerializedName("bannerImg")
    val bannerImg: String,
    
    @SerializedName("summary")
    var summary: String,
    
    @SerializedName("bodyText")
    val bodyText: String, // Backend envía String, no List
    
    @SerializedName("authorImg")
    val authorImg: String? = null,
    
    @SerializedName("authorName")
    val authorName: String? = null,
    
    @SerializedName("publishDate")
    val publishDate: String? = null,
    
    @SerializedName("tags")
    val tags: List<String>? = null
)