package com.example.huerto_hogar.data.api

import com.example.huerto_hogar.model.Blog
import retrofit2.Response
import retrofit2.http.*

/**
 * Interfaz de Retrofit para operaciones con blogs
 */
interface BlogApiService {
    
    /**
     * Obtiene todos los artículos del blog
     */
    @GET("blogs")
    suspend fun getAllBlogs(): Response<List<Blog>>
    
    /**
     * Obtiene un artículo de blog por su ID
     */
    @GET("blogs/{id}")
    suspend fun getBlogById(@Path("id") id: Int): Response<Blog>
    
    /**
     * Busca blogs por etiquetas (tags)
     */
    @GET("blogs/tags")
    suspend fun getBlogsByTags(
        @Query("tags") tags: String
    ): Response<List<Blog>>
    
    /**
     * Busca blogs por título o contenido
     */
    @GET("blogs/search")
    suspend fun searchBlogs(
        @Query("query") query: String
    ): Response<List<Blog>>
    
    /**
     * Crea un nuevo artículo de blog (requiere autenticación de admin)
     */
    @POST("blogs")
    suspend fun createBlog(
        @Body blog: Blog,
        @Header("Authorization") token: String
    ): Response<Blog>
    
    /**
     * Actualiza un artículo de blog existente (requiere autenticación de admin)
     */
    @PUT("blogs/{id}")
    suspend fun updateBlog(
        @Path("id") id: Int,
        @Body blog: Blog,
        @Header("Authorization") token: String
    ): Response<Blog>
    
    /**
     * Elimina un artículo de blog (requiere autenticación de admin)
     */
    @DELETE("blogs/{id}")
    suspend fun deleteBlog(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Response<Unit>
}
