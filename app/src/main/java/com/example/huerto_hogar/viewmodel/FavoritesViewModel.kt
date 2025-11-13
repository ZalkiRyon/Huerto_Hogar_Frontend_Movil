package com.example.huerto_hogar.viewmodel

import androidx.lifecycle.ViewModel
import com.example.huerto_hogar.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel para gestionar productos favoritos.
 * Reutiliza la estructura del CartViewModel pero sin cantidades.
 */
class FavoritesViewModel : ViewModel() {
    
    private val _favoriteItems = MutableStateFlow<List<Product>>(emptyList())
    val favoriteItems: StateFlow<List<Product>> = _favoriteItems.asStateFlow()
    
    /**
     * Agrega un producto a favoritos.
     * Retorna true si se agregó correctamente, false si ya existía.
     */
    fun addToFavorites(product: Product): Boolean {
        val currentItems = _favoriteItems.value
        
        // Verificar si el producto ya está en favoritos
        if (currentItems.any { it.id == product.id }) {
            return false // Producto ya existe
        }
        
        // Agregar nuevo producto
        _favoriteItems.value = currentItems + product
        return true
    }
    
    /**
     * Elimina un producto de favoritos.
     */
    fun removeFromFavorites(productId: Int) {
        _favoriteItems.value = _favoriteItems.value.filter { it.id != productId }
    }
    
    /**
     * Vacía la lista de favoritos completamente.
     */
    fun clearFavorites() {
        _favoriteItems.value = emptyList()
    }
    
    /**
     * Verifica si un producto está en favoritos.
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
