package com.example.huerto_hogar.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    val id: Int,
    @SerializedName("roleNombre")
    val role: String? = "cliente",
    @SerializedName("comentario")
    val comment: String? = null,
    @SerializedName("telefono")
    val phone: String? = null,
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
    
    @SerializedName("fotoPerfilUrl")
    val profilePictureUrl: String? = null,
    
    @SerializedName("activo")
    val activo: Boolean = true
)

data class AuthResponse(
    val message: String,
    @SerializedName("user")
    val user: User?,
    val token: String?
)

data class UserRegisterRequest(
    val email: String,
    val password: String,
    val nombre: String,
    val apellido: String?,
    val run: String,
    val telefono: String?,
    val region: String?,
    val comuna: String?,
    val direccion: String,
    val comentario: String?,
    val fotoPerfil: String?
)


data class UserLoginRequest(
    val email: String,
    val password: String,
)
