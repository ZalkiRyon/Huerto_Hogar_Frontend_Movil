package com.example.huerto_hogar.repository

import com.example.huerto_hogar.data.api.BlogApiService
import com.example.huerto_hogar.data.di.NetworkModule
import com.example.huerto_hogar.model.Blog
import com.example.huerto_hogar.utils.NetworkUtils
import com.example.huerto_hogar.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * Repositorio para gestionar datos de blogs
 * 
 * Capa intermedia entre ViewModel y la fuente de datos (API)
 * Maneja la lógica de negocio de artículos de blog
 * Utiliza Flow para emitir estados de Resource (Loading, Success, Error)
 */
class BlogRepository(
    private val apiService: BlogApiService = NetworkModule.blogApiService
) {
    
    /**
     * Obtiene todos los artículos de blog
     * 
     * @return Flow<Resource<List<Blog>>> - Stream de estados de la petición
     */
    fun getAllBlogs(): Flow<Resource<List<Blog>>> = flow {
        try {
            emit(Resource.Loading())
            
            val response = apiService.getAllBlogs()
            
            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error("Error al obtener blogs: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(NetworkUtils.handleNetworkException(e)))
        }
    }.flowOn(Dispatchers.IO)
    
    /**
     * Obtiene un artículo de blog por su ID
     * 
     * @param blogId ID del artículo
     * @return Flow<Resource<Blog>> - Stream de estados de la petición
     */
    fun getBlogById(blogId: Int): Flow<Resource<Blog>> = flow {
        try {
            emit(Resource.Loading())
            
            val response = apiService.getBlogById(blogId)
            
            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error("Error al obtener blog: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(NetworkUtils.handleNetworkException(e)))
        }
    }.flowOn(Dispatchers.IO)
    
    /**
     * Busca blogs por etiquetas (tags)
     * 
     * @param tags Lista de etiquetas separadas por comas
     * @return Flow<Resource<List<Blog>>> - Stream de estados de la petición
     */
    fun getBlogsByTags(tags: String): Flow<Resource<List<Blog>>> = flow {
        try {
            emit(Resource.Loading())
            
            val response = apiService.getBlogsByTags(tags)
            
            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error("Error al buscar blogs por etiquetas: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(NetworkUtils.handleNetworkException(e)))
        }
    }.flowOn(Dispatchers.IO)
    
    /**
     * Busca blogs por título o contenido
     * 
     * @param query Término de búsqueda
     * @return Flow<Resource<List<Blog>>> - Stream de estados de la petición
     */
    fun searchBlogs(query: String): Flow<Resource<List<Blog>>> = flow {
        try {
            emit(Resource.Loading())
            
            val response = apiService.searchBlogs(query)
            
            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error("Error al buscar blogs: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(NetworkUtils.handleNetworkException(e)))
        }
    }.flowOn(Dispatchers.IO)
    
    /**
     * Crea un nuevo artículo de blog (requiere token de autenticación)
     * 
     * @param blog Artículo a crear
     * @param token Token de autenticación del admin
     * @return Flow<Resource<Blog>> - Stream de estados de la petición
     */
    fun createBlog(blog: Blog, token: String): Flow<Resource<Blog>> = flow {
        try {
            emit(Resource.Loading())
            
            val response = apiService.createBlog(blog, "Bearer $token")
            
            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error("Error al crear blog: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(NetworkUtils.handleNetworkException(e)))
        }
    }.flowOn(Dispatchers.IO)
    
    /**
     * Actualiza un artículo de blog existente (requiere token de autenticación)
     * 
     * @param blogId ID del artículo a actualizar
     * @param blog Datos actualizados del artículo
     * @param token Token de autenticación del admin
     * @return Flow<Resource<Blog>> - Stream de estados de la petición
     */
    fun updateBlog(blogId: Int, blog: Blog, token: String): Flow<Resource<Blog>> = flow {
        try {
            emit(Resource.Loading())
            
            val response = apiService.updateBlog(blogId, blog, "Bearer $token")
            
            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error("Error al actualizar blog: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(NetworkUtils.handleNetworkException(e)))
        }
    }.flowOn(Dispatchers.IO)
    
    /**
     * Elimina un artículo de blog (requiere token de autenticación)
     * 
     * @param blogId ID del artículo a eliminar
     * @param token Token de autenticación del admin
     * @return Flow<Resource<Unit>> - Stream de estados de la petición
     */
    fun deleteBlog(blogId: Int, token: String): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            
            val response = apiService.deleteBlog(blogId, "Bearer $token")
            
            if (response.isSuccessful) {
                emit(Resource.Success(Unit))
            } else {
                emit(Resource.Error("Error al eliminar blog: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(NetworkUtils.handleNetworkException(e)))
        }
    }.flowOn(Dispatchers.IO)
}
