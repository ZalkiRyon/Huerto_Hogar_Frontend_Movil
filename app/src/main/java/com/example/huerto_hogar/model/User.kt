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
 * Backend envía: id, email, password, nombre, apellido, run, telefono, region, 
 * comuna, direccion, comentario, fechaRegistro, fotoPerfil, roleNombre
 */
data class User(
    @SerializedName("id")
    val id: Int = 0,
    
    @SerializedName("roleNombre")
    val role: String? = "cliente",

    @SerializedName("comentario")
    val comment: String? = null,
    
    @SerializedName("telefono")
    val phone: String?,

    @SerializedName("nombre")
    val name: String,
    
    @SerializedName("apellido")
    val lastname: String,
    
    @SerializedName("email")
    val email: String,
    
    @SerializedName("password")
    val password: String = "",
    
    @SerializedName("run")
    val run: String? = null,
    
    @SerializedName("region")
    val region: String? = null,
    
    @SerializedName("comuna")
    val comuna: String? = null,
    
    @SerializedName("direccion")
    val address: String,
    
    @SerializedName("fechaRegistro")
    val registrationDate: String? = null,
    
    @SerializedName("fotoPerfil")
    val profilePictureUrl: String? = null
)