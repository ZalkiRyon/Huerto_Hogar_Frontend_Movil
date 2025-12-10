package com.example.huerto_hogar.data.api

import com.example.huerto_hogar.model.AuthResponse
import com.example.huerto_hogar.model.CreateUserRequest
import com.example.huerto_hogar.model.UpdateUserRequest
import com.example.huerto_hogar.model.User
import com.example.huerto_hogar.model.UserLoginRequest
import com.example.huerto_hogar.model.UserRegisterRequest
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path


// Respuesta de subida de imagen de perfil
data class UploadProfileImageResponse(
    val message: String,
    val user: User,
    val imageUrl: String
)

interface UserApiService {

    // Registra un nuevo usuario
    @POST("usuarios/register")
    suspend fun registerUser(@Body request: UserRegisterRequest, @Header("Authorization") token: String = ""): AuthResponse

    //Inicia sesión de usuario
    @POST("usuarios/login")
    suspend fun loginUser(@Body request: UserLoginRequest,@Header("Authorization") token: String = ""): AuthResponse

    // Crea un nuevo usuario desde admin (con role_id)
    @POST("usuarios")
    suspend fun createUser(
        @Body userData: CreateUserRequest,
        @Header("Authorization") token: String = ""
    ): Response<User>


    //  Obtiene todos los usuarios (solo admin) - retorna solo activos
    @GET("usuarios")
    suspend fun getAllUsers(
        @Header("Authorization") token: String = ""
    ): Response<List<User>>


    //  Obtiene todos los usuarios incluyendo inactivos (solo admin)
    @GET("usuarios/todos")
    suspend fun getAllUsersIncludingInactive(
        @Header("Authorization") token: String = ""
    ): Response<List<User>>


    // Obtiene un usuario por su ID
    @GET("usuarios/{id}")
    suspend fun getUserById(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Response<User>


    // Actualiza información del usuario
    @PUT("usuarios/{id}")
    suspend fun updateUser(
        @Path("id") id: Int,
        @Body userRequest: UpdateUserRequest,
        @Header("Authorization") token: String
    ): Response<User>


    // Elimina un usuario (solo admin)
    @DELETE("usuarios/{id}")
    suspend fun deleteUser(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Response<Unit>


    //  Cambia la contraseña del usuario
    @PATCH("usuarios/{id}/password")
    suspend fun changePassword(
        @Path("id") id: Int,
        @Body passwordData: Map<String, String>,
        @Header("Authorization") token: String
    ): Response<Unit>


    //  Obtiene el perfil del usuario autenticado
    @GET("usuarios/profile")
    suspend fun getProfile(
        @Header("Authorization") token: String
    ): Response<User>


    // Reactiva un usuario desactivado (solo admin)
    @PATCH("usuarios/{id}/reactivar")
    suspend fun reactivateUser(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Response<Unit>


    //  Sube imagen de perfil del usuario
    @Multipart
    @POST("usuarios/{id}/foto-perfil")
    suspend fun uploadProfileImage(
        @Path("id") id: Int,
        @Part file: MultipartBody.Part,
        @Header("Authorization") token: String = ""
    ): Response<UploadProfileImageResponse>
}
