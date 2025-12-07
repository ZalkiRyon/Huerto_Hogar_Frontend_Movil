package com.example.huerto_hogar.data.api

import com.example.huerto_hogar.model.LoginUser
import com.example.huerto_hogar.model.RegisterUser
import com.example.huerto_hogar.model.User
import retrofit2.Response
import retrofit2.http.*

/**
 * Respuesta de login desde el backend
 */
data class LoginResponse(
    val user: User,
    val token: String,
    val message: String? = null
)

/**
 * Respuesta de registro desde el backend
 */
data class RegisterResponse(
    val user: User,
    val token: String? = null,
    val message: String
)

/**
 * Interfaz de Retrofit para operaciones con usuarios
 */
interface UserApiService {
    
    /**
     * Inicia sesión de usuario
     */
    @POST("users/login")
    suspend fun login(
        @Body credentials: LoginUser
    ): Response<LoginResponse>
    
    /**
     * Registra un nuevo usuario
     */
    @POST("users/register")
    suspend fun register(
        @Body userData: RegisterUser
    ): Response<RegisterResponse>
    
    /**
     * Obtiene todos los usuarios (solo admin)
     */
    @GET("users")
    suspend fun getAllUsers(
        @Header("Authorization") token: String
    ): Response<List<User>>
    
    /**
     * Obtiene un usuario por su ID
     */
    @GET("users/{id}")
    suspend fun getUserById(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Response<User>
    
    /**
     * Actualiza información del usuario
     */
    @PUT("users/{id}")
    suspend fun updateUser(
        @Path("id") id: Int,
        @Body user: User,
        @Header("Authorization") token: String
    ): Response<User>
    
    /**
     * Elimina un usuario (solo admin)
     */
    @DELETE("users/{id}")
    suspend fun deleteUser(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Response<Unit>
    
    /**
     * Cambia la contraseña del usuario
     */
    @PATCH("users/{id}/password")
    suspend fun changePassword(
        @Path("id") id: Int,
        @Body passwordData: Map<String, String>,
        @Header("Authorization") token: String
    ): Response<Unit>
    
    /**
     * Obtiene el perfil del usuario autenticado
     */
    @GET("users/profile")
    suspend fun getProfile(
        @Header("Authorization") token: String
    ): Response<User>
}
