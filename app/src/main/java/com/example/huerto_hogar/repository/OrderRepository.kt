package com.example.huerto_hogar.repository

import com.example.huerto_hogar.data.api.OrderApiService
import com.example.huerto_hogar.data.di.NetworkModule
import com.example.huerto_hogar.model.Order
import com.example.huerto_hogar.utils.NetworkUtils
import com.example.huerto_hogar.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * Repositorio para gestionar datos de órdenes
 */
class OrderRepository(
    private val apiService: OrderApiService = NetworkModule.orderApiService
) {
    
    /**
     * Obtiene todas las órdenes
     */
    fun getAllOrders(): Flow<Resource<List<Order>>> = flow {
        try {
            emit(Resource.Loading())
            val response = apiService.getAllOrders()
            
            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error("Error al obtener órdenes: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(NetworkUtils.handleNetworkException(e)))
        }
    }.flowOn(Dispatchers.IO)
    
    /**
     * Obtiene una orden por ID
     */
    fun getOrderById(id: Int): Flow<Resource<Order>> = flow {
        try {
            emit(Resource.Loading())
            val response = apiService.getOrderById(id)
            
            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error("Orden no encontrada"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(NetworkUtils.handleNetworkException(e)))
        }
    }.flowOn(Dispatchers.IO)
    
    /**
     * Elimina una orden
     */
    fun deleteOrder(id: Int, token: String): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            val response = apiService.deleteOrder(id, "Bearer $token")
            
            if (response.isSuccessful) {
                emit(Resource.Success(Unit))
            } else {
                emit(Resource.Error("Error al eliminar orden: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(NetworkUtils.handleNetworkException(e)))
        }
    }.flowOn(Dispatchers.IO)
}
