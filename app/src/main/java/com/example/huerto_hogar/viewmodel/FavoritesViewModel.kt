package com.example.huerto_hogar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.huerto_hogar.model.Product
import com.example.huerto_hogar.repository.FavoriteRepository
import com.example.huerto_hogar.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log

/**
 * ViewModel para gestionar productos favoritos con backend.
 */
class FavoritesViewModel(
    private val favoriteRepository: FavoriteRepository = FavoriteRepository()
) : ViewModel() {
    
    private val _favoriteItems = MutableStateFlow<List<Product>>(emptyList())
    val favoriteItems: StateFlow<List<Product>> = _favoriteItems.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    // Variable para almacenar el userId actual
    private var currentUserId: Int? = null
    
    /**
     * Establece el usuario actual y carga sus favoritos
     */
    fun setUserId(userId: Int) {
        currentUserId = userId
        loadUserFavorites(userId)
    }
    
    /**
     * Carga los favoritos del usuario desde el backend
     */
    private fun loadUserFavorites(userId: Int) {
        viewModelScope.launch {
            favoriteRepository.getUserFavorites(userId).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _isLoading.value = true
                    }
                    is Resource.Success -> {
                        _isLoading.value = false
                        resource.data?.let { favorites ->
                            _favoriteItems.value = favorites.map { it.toProduct() }
                            Log.d("FavoritesViewModel", "Loaded ${favorites.size} favorites for user $userId")
                        }
                    }
                    is Resource.Error -> {
                        _isLoading.value = false
                        Log.e("FavoritesViewModel", "Error loading favorites: ${resource.message}")
                    }
                }
            }
        }
    }
    
    /**
     * Recarga los favoritos desde el backend (útil para actualizar después de cambios)
     */
    fun reloadFavorites() {
        currentUserId?.let { userId ->
            loadUserFavorites(userId)
        }
    }
    
    /**
     * Agrega un producto a favoritos en el backend.
     * Retorna true si se agregó correctamente, false si ya existía.
     */
    fun addToFavorites(product: Product): Boolean {
        val userId = currentUserId
        if (userId == null) {
            Log.e("FavoritesViewModel", "Cannot add favorite: userId is null")
            return false
        }
        
        // Verificar si ya está en la lista local
        if (_favoriteItems.value.any { it.id == product.id }) {
            Log.d("FavoritesViewModel", "Product already in favorites")
            return false
        }
        
        // Agregar en el backend
        viewModelScope.launch {
            favoriteRepository.addFavorite(userId, product.id).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        // Recargar la lista desde el backend para obtener datos actualizados
                        loadUserFavorites(userId)
                        Log.d("FavoritesViewModel", "Added product ${product.id} to favorites")
                    }
                    is Resource.Error -> {
                        Log.e("FavoritesViewModel", "Error adding favorite: ${resource.message}")
                    }
                    is Resource.Loading -> {
                        // No hacer nada
                    }
                }
            }
        }
        return true
    }
    
    /**
     * Elimina un producto de favoritos en el backend.
     */
    fun removeFromFavorites(productId: Int) {
        val userId = currentUserId
        if (userId == null) {
            Log.e("FavoritesViewModel", "Cannot remove favorite: userId is null")
            return
        }
        
        viewModelScope.launch {
            favoriteRepository.removeFavorite(userId, productId).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        // Eliminar de la lista local
                        _favoriteItems.value = _favoriteItems.value.filter { it.id != productId }
                        Log.d("FavoritesViewModel", "Removed product $productId from favorites")
                    }
                    is Resource.Error -> {
                        Log.e("FavoritesViewModel", "Error removing favorite: ${resource.message}")
                    }
                    is Resource.Loading -> {
                        // No hacer nada
                    }
                }
            }
        }
    }
    
    /**
     * Vacía la lista de favoritos completamente (solo local).
     */
    fun clearFavorites() {
        _favoriteItems.value = emptyList()
    }
    
    /**
     * Verifica si un producto está en favoritos (local).
     */
    fun isFavorite(productId: Int): Boolean {
        return _favoriteItems.value.any { it.id == productId }
    }
    
    /**
     * Calcula el total de todos los productos favoritos (sin cantidades).
     */
    fun calculateTotal(): Double {
        return _favoriteItems.value.sumOf { it.price }
    }
}
