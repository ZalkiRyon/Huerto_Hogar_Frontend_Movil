package com.example.huerto_hogar.model

import com.google.gson.annotations.SerializedName

// Roles in the system
enum class Role {
    @SerializedName("ADMIN")
    ADMIN,
    
    @SerializedName("CLIENT")
    CLIENT,
    
    @SerializedName("SALESMAN")
    SALESMAN
}

/**
 * Modelo de Usuario (DTO) que coincide con el backend
 * 
 * @SerializedName mapea los nombres de campos del JSON a las propiedades de Kotlin
 */
data class User(
    @SerializedName("id")
    val id: Int = 0,
    
    @SerializedName("role")
    val role: Role = Role.CLIENT,

    @SerializedName("comment")
    val comment: String? = null,
    
    @SerializedName("phone")
    val phone: String?,

    @SerializedName("name")
    val name: String,
    
    @SerializedName("lastname")
    val lastname: String,
    
    @SerializedName("email")
    val email: String,
    
    @SerializedName("password")
    val password: String,
    
    @SerializedName("address")
    val address: String,
    
    @SerializedName("profilePictureUrl")
    val profilePictureUrl: String? = null
)