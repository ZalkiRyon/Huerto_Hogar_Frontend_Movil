package com.example.huerto_hogar.model

import com.google.gson.annotations.SerializedName

/**
 * Request DTO para actualizar usuario desde admin
 * Mapea a UserRequestDTO del backend
 * 
 * Nota: Al actualizar, el backend requiere TODOS los campos
 * incluyendo email (aunque no se puede cambiar) y password
 */
data class UpdateUserRequest(
    @SerializedName("email")
    val email: String,
    
    @SerializedName("password")
    val password: String, // Password actual del usuario
    
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
    val roleId: Int // 1=admin, 2=cliente, 3=vendedor
)
