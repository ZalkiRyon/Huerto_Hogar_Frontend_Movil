package com.example.huerto_hogar.data.api

import com.example.huerto_hogar.model.Product
import com.example.huerto_hogar.model.ProductCategory
import retrofit2.Response
import retrofit2.http.*

/**
 * Interfaz de Retrofit para operaciones con productos
 */
interface ProductApiService {
    
    /**
     * Obtiene todos los productos del catálogo (solo activos)
     */
    @GET("productos")
    suspend fun getAllProducts(): Response<List<Product>>
    
    /**
     * Obtiene todos los productos incluyendo inactivos (solo admin)
     */
    @GET("productos/todos")
    suspend fun getAllProductsIncludingInactive(
        @Header("Authorization") token: String
    ): Response<List<Product>>
    
    /**
     * Obtiene un producto por su ID
     */
    @GET("productos/{id}")
    suspend fun getProductById(@Path("id") id: Int): Response<Product>
    
    /**
     * Obtiene productos filtrados por categoría
     */
    @GET("productos/category/{category}")
    suspend fun getProductsByCategory(
        @Path("category") category: String
    ): Response<List<Product>>
    
    /**
     * Busca productos por nombre
     */
    @GET("productos/search")
    suspend fun searchProducts(
        @Query("query") query: String
    ): Response<List<Product>>
    
    /**
     * Crea un nuevo producto (requiere autenticación de admin)
     */
    @POST("productos")
    suspend fun createProduct(
        @Body product: Product,
        @Header("Authorization") token: String
    ): Response<Product>
    
    /**
     * Actualiza un producto existente (requiere autenticación de admin)
     */
    @PUT("productos/{id}")
    suspend fun updateProduct(
        @Path("id") id: Int,
        @Body product: Product,
        @Header("Authorization") token: String
    ): Response<Product>
    
    /**
     * Elimina un producto (requiere autenticación de admin)
     */
    @DELETE("productos/{id}")
    suspend fun deleteProduct(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Response<Unit>
    
    /**
     * Actualiza el stock de un producto
     */
    @PATCH("productos/{id}/stock")
    suspend fun updateStock(
        @Path("id") id: Int,
        @Body stockUpdate: Map<String, Int>,
        @Header("Authorization") token: String
    ): Response<Product>
    
    /**
     * Reactiva un producto desactivado (requiere autenticación de admin)
     */
    @PATCH("productos/{id}/reactivar")
    suspend fun reactivateProduct(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Response<Unit>
}
