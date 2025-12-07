package com.example.huerto_hogar.model

import com.google.gson.annotations.SerializedName

/**
 * Modelo de Blog (DTO) que coincide con el backend
 * 
 * @SerializedName mapea los nombres de campos del JSON a las propiedades de Kotlin
 * bannerImg y authorImg ahora son String para URLs remotas (antes eran Int para recursos drawable)
 */
data class Blog(
    @SerializedName("id")
    val id: Int = 0,
    
    @SerializedName("title")
    val title: String,
    
    @SerializedName("bannerImg")
    val bannerImg: String, // Ahora es String para URLs remotas
    
    @SerializedName("summary")
    var summary: String,
    
    @SerializedName("bodyText")
    val bodyText: List<String>,
    
    @SerializedName("authorImg")
    val authorImg: String, // Ahora es String para URLs remotas
    
    @SerializedName("authorName")
    val authorName: String,
    
    @SerializedName("publishDate")
    val publishDate: String,
    
    @SerializedName("tags")
    val tags: List<String>
)