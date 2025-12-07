package com.example.huerto_hogar.model

/**
 * DTO para la petición de login al backend
 * Solo contiene los campos que el backend espera
 */
data class LoginRequest(
    val email: String,
    val password: String
)
