package com.example.huerto_hogar.data.api

import com.example.huerto_hogar.model.Order
import retrofit2.Response
import retrofit2.http.*

/**
 * Interfaz de Retrofit para operaciones con órdenes
 */
interface OrderApiService {
    
    /**
     * Obtiene todas las órdenes (Admin/Vendedor)
     */
    @GET("ordenes")
    suspend fun getAllOrders(): Response<List<Order>>
    
    /**
     * Obtiene una orden por su ID
     */
    @GET("ordenes/{id}")
    suspend fun getOrderById(@Path("id") id: Int): Response<Order>
    
    /**
     * Elimina una orden (Admin)
     */
    @DELETE("ordenes/{id}")
    suspend fun deleteOrder(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Response<Unit>
    
    /**
     * Actualiza el estado de una orden
     */
    @PUT("ordenes/{id}")
    suspend fun updateOrder(
        @Path("id") id: Int,
        @Body order: Order,
        @Header("Authorization") token: String
    ): Response<Order>
}
