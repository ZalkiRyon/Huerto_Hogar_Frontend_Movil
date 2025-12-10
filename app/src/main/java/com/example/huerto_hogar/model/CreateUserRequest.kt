package com.example.huerto_hogar.model

import com.google.gson.annotations.SerializedName

/**
 * Request DTO para crear usuario desde admin
 * Mapea a UserRequestDTO del backend
 */
data class CreateUserRequest(
    @SerializedName("email")
    val email: String,
    
    @SerializedName("password")
    val password: String,
    
    @SerializedName("nombre")
    val name: String,
    
    @SerializedName("apellido")
    val lastname: String,
    
    @SerializedName("run")
    val run: String,
    
    @SerializedName("telefono")
    val phone: String? = null,
    
    @SerializedName("region")
    val region: String,
    
    @SerializedName("comuna")
    val comuna: String,
    
    @SerializedName("direccion")
    val address: String,
    
    @SerializedName("comentario")
    val comment: String? = null,
    
    @SerializedName("fotoPerfil")
    val profilePhoto: String? = null,
    
    @SerializedName("role_id")
    val roleId: Int // 1=admin, 2=vendedor, 3=cliente
)

/**
 * Respuesta al crear usuario
 */
data class CreateUserResponse(
    val user: User,
    val message: String? = null
)
