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
     * Obtiene todos los productos del catálogo
     */
    @GET("products")
    suspend fun getAllProducts(): Response<List<Product>>
    
    /**
     * Obtiene un producto por su ID
     */
    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Int): Response<Product>
    
    /**
     * Obtiene productos filtrados por categoría
     */
    @GET("products/category/{category}")
    suspend fun getProductsByCategory(
        @Path("category") category: String
    ): Response<List<Product>>
    
    /**
     * Busca productos por nombre
     */
    @GET("products/search")
    suspend fun searchProducts(
        @Query("query") query: String
    ): Response<List<Product>>
    
    /**
     * Crea un nuevo producto (requiere autenticación de admin)
     */
    @POST("products")
    suspend fun createProduct(
        @Body product: Product,
        @Header("Authorization") token: String
    ): Response<Product>
    
    /**
     * Actualiza un producto existente (requiere autenticación de admin)
     */
    @PUT("products/{id}")
    suspend fun updateProduct(
        @Path("id") id: Int,
        @Body product: Product,
        @Header("Authorization") token: String
    ): Response<Product>
    
    /**
     * Elimina un producto (requiere autenticación de admin)
     */
    @DELETE("products/{id}")
    suspend fun deleteProduct(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Response<Unit>
    
    /**
     * Actualiza el stock de un producto
     */
    @PATCH("products/{id}/stock")
    suspend fun updateStock(
        @Path("id") id: Int,
        @Body stockUpdate: Map<String, Int>,
        @Header("Authorization") token: String
    ): Response<Product>
}
