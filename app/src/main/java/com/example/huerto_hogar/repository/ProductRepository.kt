package com.example.huerto_hogar.repository

import com.example.huerto_hogar.data.api.ProductApiService
import com.example.huerto_hogar.data.di.NetworkModule
import com.example.huerto_hogar.model.Product
import com.example.huerto_hogar.model.ProductCategory
import com.example.huerto_hogar.utils.NetworkUtils
import com.example.huerto_hogar.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * Repositorio para gestionar datos de productos
 * 
 * Capa intermedia entre ViewModel y la fuente de datos (API)
 * Maneja la lógica de negocio y transformación de datos
 * Utiliza Flow para emitir estados de Resource (Loading, Success, Error)
 */
class ProductRepository(
    private val apiService: ProductApiService = NetworkModule.productApiService
) {
    
    /**
     * Obtiene todos los productos del catálogo
     * 
     * @return Flow<Resource<List<Product>>> - Stream de estados de la petición
     */
    fun getAllProducts(): Flow<Resource<List<Product>>> = flow {
        try {
            emit(Resource.Loading())
            
            val response = apiService.getAllProducts()
            
            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error("Error al obtener productos: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(NetworkUtils.handleNetworkException(e)))
        }
    }.flowOn(Dispatchers.IO)
    
    /**
     * Obtiene productos filtrados por categoría
     * 
     * @param category Categoría de productos a filtrar (String del backend)
     * @return Flow<Resource<List<Product>>> - Stream de estados de la petición
     */
    fun getProductsByCategory(category: String): Flow<Resource<List<Product>>> = flow {
        try {
            emit(Resource.Loading())
            
            val response = apiService.getProductsByCategory(category)
            
            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error("Error al obtener productos por categoría: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(NetworkUtils.handleNetworkException(e)))
        }
    }.flowOn(Dispatchers.IO)
    
    /**
     * Busca productos por nombre
     * 
     * @param query Término de búsqueda
     * @return Flow<Resource<List<Product>>> - Stream de estados de la petición
     */
    fun searchProducts(query: String): Flow<Resource<List<Product>>> = flow {
        try {
            emit(Resource.Loading())
            
            val response = apiService.searchProducts(query)
            
            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error("Error al buscar productos: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(NetworkUtils.handleNetworkException(e)))
        }
    }.flowOn(Dispatchers.IO)
    
    /**
     * Obtiene un producto específico por su ID
     * 
     * @param productId ID del producto
     * @return Flow<Resource<Product>> - Stream de estados de la petición
     */
    fun getProductById(productId: Int): Flow<Resource<Product>> = flow {
        try {
            emit(Resource.Loading())
            
            val response = apiService.getProductById(productId)
            
            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error("Error al obtener producto: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(NetworkUtils.handleNetworkException(e)))
        }
    }.flowOn(Dispatchers.IO)
    
    /**
     * Crea un nuevo producto (requiere token de autenticación)
     * 
     * @param product Producto a crear
     * @param token Token de autenticación del admin
     * @return Flow<Resource<Product>> - Stream de estados de la petición
     */
    fun createProduct(product: Product, token: String): Flow<Resource<Product>> = flow {
        try {
            emit(Resource.Loading())
            
            val response = apiService.createProduct(product, "Bearer $token")
            
            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error("Error al crear producto: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(NetworkUtils.handleNetworkException(e)))
        }
    }.flowOn(Dispatchers.IO)
    
    /**
     * Actualiza un producto existente (requiere token de autenticación)
     * 
     * @param productId ID del producto a actualizar
     * @param product Datos actualizados del producto
     * @param token Token de autenticación del admin
     * @return Flow<Resource<Product>> - Stream de estados de la petición
     */
    fun updateProduct(productId: Int, product: Product, token: String): Flow<Resource<Product>> = flow {
        try {
            emit(Resource.Loading())
            
            val response = apiService.updateProduct(productId, product, "Bearer $token")
            
            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error("Error al actualizar producto: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(NetworkUtils.handleNetworkException(e)))
        }
    }.flowOn(Dispatchers.IO)
    
    /**
     * Elimina un producto (requiere token de autenticación)
     * 
     * @param productId ID del producto a eliminar
     * @param token Token de autenticación del admin
     * @return Flow<Resource<Unit>> - Stream de estados de la petición
     */
    fun deleteProduct(productId: Int, token: String): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            
            val response = apiService.deleteProduct(productId, "Bearer $token")
            
            if (response.isSuccessful) {
                emit(Resource.Success(Unit))
            } else {
                emit(Resource.Error("Error al eliminar producto: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(NetworkUtils.handleNetworkException(e)))
        }
    }.flowOn(Dispatchers.IO)
    
    /**
     * Actualiza el stock de un producto (requiere token de autenticación)
     * 
     * @param productId ID del producto
     * @param newStock Nueva cantidad de stock
     * @param token Token de autenticación del admin
     * @return Flow<Resource<Product>> - Stream de estados de la petición
     */
    fun updateStock(productId: Int, newStock: Int, token: String): Flow<Resource<Product>> = flow {
        try {
            emit(Resource.Loading())
            
            val stockUpdate = mapOf("stock" to newStock)
            val response = apiService.updateStock(productId, stockUpdate, "Bearer $token")
            
            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error("Error al actualizar stock: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(NetworkUtils.handleNetworkException(e)))
        }
    }.flowOn(Dispatchers.IO)
}
