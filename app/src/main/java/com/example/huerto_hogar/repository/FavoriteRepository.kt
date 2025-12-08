package com.example.huerto_hogar.repository

import com.example.huerto_hogar.data.api.FavoriteApiService
import com.example.huerto_hogar.data.di.NetworkModule
import com.example.huerto_hogar.model.Favorite
import com.example.huerto_hogar.utils.NetworkUtils
import com.example.huerto_hogar.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * Repositorio para gestionar datos de favoritos
 */
class FavoriteRepository(
    private val apiService: FavoriteApiService = NetworkModule.favoriteApiService
) {
    
    /**
     * Obtiene todos los favoritos de un usuario
     */
    fun getUserFavorites(usuarioId: Int): Flow<Resource<List<Favorite>>> = flow {
        try {
            emit(Resource.Loading())
            val response = apiService.getUserFavorites(usuarioId)
            
            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error("Error al obtener favoritos: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(NetworkUtils.handleNetworkException(e)))
        }
    }.flowOn(Dispatchers.IO)
    
    /**
     * Agrega un producto a favoritos
     */
    fun addFavorite(usuarioId: Int, productoId: Int): Flow<Resource<Favorite>> = flow {
        try {
            emit(Resource.Loading())
            val request = mapOf("usuarioId" to usuarioId, "productoId" to productoId)
            val response = apiService.addFavorite(request)
            
            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error("Error al agregar favorito: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(NetworkUtils.handleNetworkException(e)))
        }
    }.flowOn(Dispatchers.IO)
    
    /**
     * Elimina un producto de favoritos
     */
    fun removeFavorite(usuarioId: Int, productoId: Int): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            val request = mapOf("usuarioId" to usuarioId, "productoId" to productoId)
            val response = apiService.removeFavorite(request)
            
            if (response.isSuccessful) {
                emit(Resource.Success(Unit))
            } else {
                emit(Resource.Error("Error al eliminar favorito: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(NetworkUtils.handleNetworkException(e)))
        }
    }.flowOn(Dispatchers.IO)
    
    /**
     * Verifica si un producto es favorito del usuario
     */
    fun isFavorite(usuarioId: Int, productoId: Int): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading())
            val response = apiService.isFavorite(usuarioId, productoId)
            
            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error("Error al verificar favorito"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(NetworkUtils.handleNetworkException(e)))
        }
    }.flowOn(Dispatchers.IO)
}
