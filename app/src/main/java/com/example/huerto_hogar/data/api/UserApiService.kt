package com.example.huerto_hogar.data.api

import com.example.huerto_hogar.model.CreateUserRequest
import com.example.huerto_hogar.model.LoginRequest
import com.example.huerto_hogar.model.RegisterUser
import com.example.huerto_hogar.model.UpdateUserRequest
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
    @POST("usuarios/login")
    suspend fun login(
        @Body credentials: LoginRequest
    ): Response<LoginResponse>
    
    /**
     * Registra un nuevo usuario
     */
    @POST("usuarios/register")
    suspend fun register(
        @Body userData: RegisterUser
    ): Response<RegisterResponse>
    
    /**
     * Crea un nuevo usuario desde admin (con role_id)
     */
    @POST("usuarios")
    suspend fun createUser(
        @Body userData: CreateUserRequest,
        @Header("Authorization") token: String = ""
    ): Response<User>
    
    /**
     * Obtiene todos los usuarios (solo admin)
     */
    @GET("usuarios")
    suspend fun getAllUsers(
        @Header("Authorization") token: String = ""
    ): Response<List<User>>
    
    /**
     * Obtiene un usuario por su ID
     */
    @GET("usuarios/{id}")
    suspend fun getUserById(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Response<User>
    
    /**
     * Actualiza información del usuario
     */
    @PUT("usuarios/{id}")
    suspend fun updateUser(
        @Path("id") id: Int,
        @Body userRequest: UpdateUserRequest,
        @Header("Authorization") token: String
    ): Response<User>
    
    /**
     * Elimina un usuario (solo admin)
     */
    @DELETE("usuarios/{id}")
    suspend fun deleteUser(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Response<Unit>
    
    /**
     * Cambia la contraseña del usuario
     */
    @PATCH("usuarios/{id}/password")
    suspend fun changePassword(
        @Path("id") id: Int,
        @Body passwordData: Map<String, String>,
        @Header("Authorization") token: String
    ): Response<Unit>
    
    /**
     * Obtiene el perfil del usuario autenticado
     */
    @GET("usuarios/profile")
    suspend fun getProfile(
        @Header("Authorization") token: String
    ): Response<User>
    
    /**
     * Reactiva un usuario desactivado (solo admin)
     */
    @PATCH("usuarios/{id}/reactivar")
    suspend fun reactivateUser(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Response<Unit>
}
