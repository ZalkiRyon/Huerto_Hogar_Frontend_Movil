package com.example.huerto_hogar.repository

import com.example.huerto_hogar.data.api.UserApiService
import com.example.huerto_hogar.data.api.LoginResponse
import com.example.huerto_hogar.data.api.RegisterResponse
import com.example.huerto_hogar.data.di.NetworkModule
import com.example.huerto_hogar.model.LoginRequest
import com.example.huerto_hogar.model.RegisterUser
import com.example.huerto_hogar.model.User
import com.example.huerto_hogar.utils.NetworkUtils
import com.example.huerto_hogar.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * Repositorio para gestionar datos de usuarios
 * 
 * Capa intermedia entre ViewModel y la fuente de datos (API)
 * Maneja autenticación, registro y operaciones CRUD de usuarios
 * Utiliza Flow para emitir estados de Resource (Loading, Success, Error)
 */
class UserRepository(
    private val apiService: UserApiService = NetworkModule.userApiService
) {
    
    /**
     * Inicia sesión de un usuario
     * 
     * @param email Correo electrónico del usuario
     * @param password Contraseña del usuario
     * @return Flow<Resource<LoginResponse>> - Stream con respuesta de login (usuario + token)
     */
    fun login(email: String, password: String): Flow<Resource<LoginResponse>> = flow {
        try {
            emit(Resource.Loading())
            
            val credentials = LoginRequest(email = email, password = password)
            val response = apiService.login(credentials)
            
            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error("Credenciales inválidas"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(NetworkUtils.handleNetworkException(e)))
        }
    }.flowOn(Dispatchers.IO)
    
    /**
     * Registra un nuevo usuario
     * 
     * @param name Nombre
     * @param lastname Apellido
     * @param email Correo electrónico
     * @param password Contraseña
     * @param phone Teléfono
     * @param address Dirección
     * @return Flow<Resource<RegisterResponse>> - Stream con respuesta de registro
     */
    fun register(
        name: String,
        lastname: String,
        email: String,
        password: String,
        phone: String,
        address: String
    ): Flow<Resource<RegisterResponse>> = flow {
        try {
            emit(Resource.Loading())
            
            val userData = RegisterUser(
                name = name,
                lastname = lastname,
                email = email,
                password = password,
                phone = phone,
                address = address
            )
            
            val response = apiService.register(userData)
            
            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error("Error al registrar usuario: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(NetworkUtils.handleNetworkException(e)))
        }
    }.flowOn(Dispatchers.IO)
    
    /**
     * Obtiene todos los usuarios (solo para administradores)
     * 
     * @param token Token de autenticación del admin
     * @return Flow<Resource<List<User>>> - Stream con lista de usuarios
     */
    fun getAllUsers(token: String): Flow<Resource<List<User>>> = flow {
        try {
            emit(Resource.Loading())
            
            val response = apiService.getAllUsers("Bearer $token")
            
            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error("Error al obtener usuarios: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(NetworkUtils.handleNetworkException(e)))
        }
    }.flowOn(Dispatchers.IO)
    
    /**
     * Obtiene un usuario específico por su ID
     * 
     * @param userId ID del usuario
     * @param token Token de autenticación
     * @return Flow<Resource<User>> - Stream con el usuario
     */
    fun getUserById(userId: Int, token: String): Flow<Resource<User>> = flow {
        try {
            emit(Resource.Loading())
            
            val response = apiService.getUserById(userId, "Bearer $token")
            
            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error("Error al obtener usuario: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(NetworkUtils.handleNetworkException(e)))
        }
    }.flowOn(Dispatchers.IO)
    
    /**
     * Obtiene el perfil del usuario autenticado
     * 
     * @param token Token de autenticación
     * @return Flow<Resource<User>> - Stream con el perfil del usuario
     */
    fun getProfile(token: String): Flow<Resource<User>> = flow {
        try {
            emit(Resource.Loading())
            
            val response = apiService.getProfile("Bearer $token")
            
            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error("Error al obtener perfil: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(NetworkUtils.handleNetworkException(e)))
        }
    }.flowOn(Dispatchers.IO)
    
    /**
     * Actualiza información de un usuario
     * 
     * @param userId ID del usuario a actualizar
     * @param user Datos actualizados del usuario
     * @param token Token de autenticación
     * @return Flow<Resource<User>> - Stream con el usuario actualizado
     */
    fun updateUser(userId: Int, user: User, token: String): Flow<Resource<User>> = flow {
        try {
            emit(Resource.Loading())
            
            val response = apiService.updateUser(userId, user, "Bearer $token")
            
            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error("Error al actualizar usuario: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(NetworkUtils.handleNetworkException(e)))
        }
    }.flowOn(Dispatchers.IO)
    
    /**
     * Elimina un usuario (solo para administradores)
     * 
     * @param userId ID del usuario a eliminar
     * @param token Token de autenticación del admin
     * @return Flow<Resource<Unit>> - Stream de estados de la petición
     */
    fun deleteUser(userId: Int, token: String): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            
            val response = apiService.deleteUser(userId, "Bearer $token")
            
            if (response.isSuccessful) {
                emit(Resource.Success(Unit))
            } else {
                emit(Resource.Error("Error al eliminar usuario: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(NetworkUtils.handleNetworkException(e)))
        }
    }.flowOn(Dispatchers.IO)
    
    /**
     * Cambia la contraseña de un usuario
     * 
     * @param userId ID del usuario
     * @param oldPassword Contraseña actual
     * @param newPassword Nueva contraseña
     * @param token Token de autenticación
     * @return Flow<Resource<Unit>> - Stream de estados de la petición
     */
    fun changePassword(
        userId: Int,
        oldPassword: String,
        newPassword: String,
        token: String
    ): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            
            val passwordData = mapOf(
                "oldPassword" to oldPassword,
                "newPassword" to newPassword
            )
            
            val response = apiService.changePassword(userId, passwordData, "Bearer $token")
            
            if (response.isSuccessful) {
                emit(Resource.Success(Unit))
            } else {
                emit(Resource.Error("Error al cambiar contraseña: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(NetworkUtils.handleNetworkException(e)))
        }
    }.flowOn(Dispatchers.IO)
}
