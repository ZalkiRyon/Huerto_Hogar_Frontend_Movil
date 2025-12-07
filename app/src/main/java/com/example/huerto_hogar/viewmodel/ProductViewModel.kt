package com.example.huerto_hogar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.huerto_hogar.model.Product
import com.example.huerto_hogar.model.ProductCategory
import com.example.huerto_hogar.repository.ProductRepository
import com.example.huerto_hogar.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Estados posibles para las operaciones de productos
 */
sealed class ProductUiState {
    object Idle : ProductUiState()
    object Loading : ProductUiState()
    data class Success(val products: List<Product>) : ProductUiState()
    data class Error(val message: String) : ProductUiState()
}

/**
 * ViewModel para gestionar productos
 * 
 * Interactúa con ProductRepository para obtener datos de la API
 * Expone estados de UI reactivos mediante StateFlow
 */
class ProductViewModel(
    private val repository: ProductRepository = ProductRepository()
) : ViewModel() {
    
    // Estado de la lista de productos
    private val _productsState = MutableStateFlow<ProductUiState>(ProductUiState.Idle)
    val productsState: StateFlow<ProductUiState> = _productsState.asStateFlow()
    
    // Lista de productos actualmente cargados (para acceso directo)
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()
    
    /**
     * Obtiene todos los productos del catálogo
     */
    fun getAllProducts() {
        viewModelScope.launch {
            repository.getAllProducts().collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _productsState.value = ProductUiState.Loading
                    }
                    is Resource.Success -> {
                        resource.data?.let { productList ->
                            _products.value = productList
                            _productsState.value = ProductUiState.Success(productList)
                        }
                    }
                    is Resource.Error -> {
                        _productsState.value = ProductUiState.Error(
                            resource.message ?: "Error desconocido"
                        )
                    }
                }
            }
        }
    }
    
    /**
     * Obtiene productos filtrados por categoría
     * 
     * @param category Categoría de productos a filtrar (String del backend)
     */
    fun getProductsByCategory(category: String) {
        viewModelScope.launch {
            repository.getProductsByCategory(category).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _productsState.value = ProductUiState.Loading
                    }
                    is Resource.Success -> {
                        resource.data?.let { productList ->
                            _products.value = productList
                            _productsState.value = ProductUiState.Success(productList)
                        }
                    }
                    is Resource.Error -> {
                        _productsState.value = ProductUiState.Error(
                            resource.message ?: "Error al filtrar productos"
                        )
                    }
                }
            }
        }
    }
    
    /**
     * Busca productos por nombre
     * 
     * @param query Término de búsqueda
     */
    fun searchProducts(query: String) {
        if (query.isBlank()) {
            getAllProducts()
            return
        }
        
        viewModelScope.launch {
            repository.searchProducts(query).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _productsState.value = ProductUiState.Loading
                    }
                    is Resource.Success -> {
                        resource.data?.let { productList ->
                            _products.value = productList
                            _productsState.value = ProductUiState.Success(productList)
                        }
                    }
                    is Resource.Error -> {
                        _productsState.value = ProductUiState.Error(
                            resource.message ?: "Error en la búsqueda"
                        )
                    }
                }
            }
        }
    }
    
    /**
     * Crea un nuevo producto (operación de admin)
     * 
     * @param product Producto a crear
     * @param token Token de autenticación del admin
     * @param onSuccess Callback ejecutado cuando se crea exitosamente
     * @param onError Callback ejecutado cuando hay un error
     */
    fun createProduct(
        product: Product,
        token: String,
        onSuccess: (Product) -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            repository.createProduct(product, token).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _productsState.value = ProductUiState.Loading
                    }
                    is Resource.Success -> {
                        resource.data?.let { newProduct ->
                            onSuccess(newProduct)
                            // Recargar lista de productos
                            getAllProducts()
                        }
                    }
                    is Resource.Error -> {
                        val errorMsg = resource.message ?: "Error al crear producto"
                        onError(errorMsg)
                        _productsState.value = ProductUiState.Error(errorMsg)
                    }
                }
            }
        }
    }
    
    /**
     * Actualiza un producto existente (operación de admin)
     * 
     * @param productId ID del producto a actualizar
     * @param product Datos actualizados
     * @param token Token de autenticación del admin
     * @param onSuccess Callback ejecutado cuando se actualiza exitosamente
     * @param onError Callback ejecutado cuando hay un error
     */
    fun updateProduct(
        productId: Int,
        product: Product,
        token: String,
        onSuccess: (Product) -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            repository.updateProduct(productId, product, token).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _productsState.value = ProductUiState.Loading
                    }
                    is Resource.Success -> {
                        resource.data?.let { updatedProduct ->
                            onSuccess(updatedProduct)
                            // Recargar lista de productos
                            getAllProducts()
                        }
                    }
                    is Resource.Error -> {
                        val errorMsg = resource.message ?: "Error al actualizar producto"
                        onError(errorMsg)
                        _productsState.value = ProductUiState.Error(errorMsg)
                    }
                }
            }
        }
    }
    
    /**
     * Elimina un producto (operación de admin)
     * 
     * @param productId ID del producto a eliminar
     * @param token Token de autenticación del admin
     * @param onSuccess Callback ejecutado cuando se elimina exitosamente
     * @param onError Callback ejecutado cuando hay un error
     */
    fun deleteProduct(
        productId: Int,
        token: String,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            repository.deleteProduct(productId, token).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _productsState.value = ProductUiState.Loading
                    }
                    is Resource.Success -> {
                        onSuccess()
                        // Recargar lista de productos
                        getAllProducts()
                    }
                    is Resource.Error -> {
                        val errorMsg = resource.message ?: "Error al eliminar producto"
                        onError(errorMsg)
                        _productsState.value = ProductUiState.Error(errorMsg)
                    }
                }
            }
        }
    }
}
