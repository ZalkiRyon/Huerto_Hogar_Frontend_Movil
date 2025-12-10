package com.example.huerto_hogar.repository

import com.example.huerto_hogar.data.api.UserApiService
import com.example.huerto_hogar.data.di.NetworkModule
import com.example.huerto_hogar.model.AuthResponse
import com.example.huerto_hogar.model.CreateUserRequest
import com.example.huerto_hogar.model.UpdateUserRequest
import com.example.huerto_hogar.model.User
import com.example.huerto_hogar.model.UserLoginRequest
import com.example.huerto_hogar.model.UserRegisterRequest
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
class UserRepository {
    private val apiService: UserApiService = NetworkModule.userApiService

    suspend fun registerUser(request: UserRegisterRequest): AuthResponse {
        return apiService.registerUser(request)
    }

    suspend fun loginUser(request: UserLoginRequest): AuthResponse {
        return  apiService.loginUser(request)
    }


    /**
     * Obtiene todos los usuarios (solo para administradores)
     *
     * @param token Token de autenticación del admin (opcional para desarrollo)
     * @return Flow<Resource<List<User>>> - Stream con lista de usuarios
     */
    fun getAllUsers(token: String = ""): Flow<Resource<List<User>>> = flow {
        try {
            emit(Resource.Loading())

            val authHeader = if (token.isNotEmpty()) "Bearer $token" else ""
            val response = apiService.getAllUsers(authHeader)

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
     * @param updateRequest DTO con datos actualizados del usuario
     * @param token Token de autenticación
     * @return Flow<Resource<User>> - Stream con el usuario actualizado
     */
    fun updateUser(
        userId: Int,
        updateRequest: UpdateUserRequest,
        token: String
    ): Flow<Resource<User>> = flow {
        try {
            emit(Resource.Loading())

            val response = apiService.updateUser(userId, updateRequest, "Bearer $token")

            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!))
            } else {
                // Intentar parsear el error del backend
                val errorBody = response.errorBody()?.string()
                val errorMessage = if (errorBody != null) {
                    try {
                        when {
                            errorBody.contains("email") && errorBody.contains("ya se encuentra registrado") ->
                                "El email ya está registrado por otro usuario"

                            errorBody.contains("RUN") && errorBody.contains("ya se encuentra registrado") ->
                                "El RUN ya está registrado"

                            errorBody.contains("Duplicate entry") ->
                                "Ya existe un registro con estos datos"

                            else -> errorBody
                        }
                    } catch (e: Exception) {
                        "Error al actualizar usuario: ${response.code()}"
                    }
                } else {
                    "Error al actualizar usuario: ${response.code()}"
                }
                emit(Resource.Error(errorMessage))
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

    /**
     * Crea un nuevo usuario desde el panel de administración
     *
     * @param userData Datos del usuario a crear
     * @param token Token de autenticación del admin (opcional en desarrollo)
     * @return Flow<Resource<User>> - Stream con el usuario creado
     */
    fun createUser(userData: CreateUserRequest, token: String = ""): Flow<Resource<User>> = flow {
        try {
            emit(Resource.Loading())

            val authHeader = if (token.isNotEmpty()) "Bearer $token" else ""
            val response = apiService.createUser(userData, authHeader)

            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!))
            } else {
                // Intentar parsear el mensaje de error del backend
                val errorBody = response.errorBody()?.string()
                val errorMsg = when (response.code()) {
                    400 -> {
                        if (errorBody?.contains("run", ignoreCase = true) == true) {
                            "RUN duplicado - Este RUN ya está registrado en el sistema"
                        } else if (errorBody?.contains("email", ignoreCase = true) == true) {
                            "Email duplicado - Este email ya está registrado"
                        } else {
                            "Datos inválidos: $errorBody"
                        }
                    }

                    409 -> "El email o RUN ya está registrado en el sistema"
                    500 -> {
                        if (errorBody?.contains("Duplicate entry", ignoreCase = true) == true) {
                            when {
                                errorBody.contains("run", ignoreCase = true) ->
                                    "Error: El RUN ingresado ya existe en la base de datos"

                                errorBody.contains("email", ignoreCase = true) ->
                                    "Error: El email ingresado ya existe en la base de datos"

                                else -> "Error: Datos duplicados en la base de datos"
                            }
                        } else {
                            "Error interno del servidor. Verifique los datos e intente nuevamente."
                        }
                    }

                    else -> "Error al crear usuario (código ${response.code()})"
                }
                emit(Resource.Error(errorMsg))
            }
        } catch (e: Exception) {
            val errorMsg = when {
                e.message?.contains("duplicate", ignoreCase = true) == true ->
                    "Error: Ya existe un usuario con estos datos"

                else -> NetworkUtils.handleNetworkException(e)
            }
            emit(Resource.Error(errorMsg))
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Obtiene todos los usuarios incluyendo inactivos (solo admin)
     *
     * @param token Token de autenticación del admin
     * @return Flow<Resource<List<User>>>
     */
    fun getAllUsersIncludingInactive(token: String): Flow<Resource<List<User>>> = flow {
        try {
            emit(Resource.Loading())

            val response = apiService.getAllUsersIncludingInactive("Bearer $token")

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
     * Reactiva un usuario desactivado
     *
     * @param userId ID del usuario a reactivar
     * @param token Token de autenticación del admin
     * @return Flow<Resource<Unit>>
     */
    fun reactivateUser(userId: Int, token: String): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())

            val response = apiService.reactivateUser(userId, "Bearer $token")

            if (response.isSuccessful) {
                emit(Resource.Success(Unit))
            } else {
                emit(Resource.Error("Error al reactivar usuario: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(NetworkUtils.handleNetworkException(e)))
        }
    }.flowOn(Dispatchers.IO)
}
