package com.example.huerto_hogar.viewmodel

import androidx.lifecycle.ViewModel
import com.example.huerto_hogar.model.CartItem
import com.example.huerto_hogar.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel para gestionar el carrito de compras.
 * Implementa patrón Singleton para compartir estado entre pantallas.
 */
class CartViewModel : ViewModel() {
    
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()
    
    private val _studentDiscount = MutableStateFlow(false)
    val studentDiscount: StateFlow<Boolean> = _studentDiscount.asStateFlow()
    
    /**
     * Agrega un producto al carrito (incrementa cantidad si ya existe).
     */
    fun addToCart(product: Product) {
        val currentItems = _cartItems.value.toMutableList()
        val existingItem = currentItems.find { it.product.id == product.id }
        
        if (existingItem != null) {
            // Incrementar cantidad si ya existe
            val index = currentItems.indexOf(existingItem)
            currentItems[index] = existingItem.copy(quantity = existingItem.quantity + 1)
        } else {
            // Agregar nuevo item
            currentItems.add(CartItem(product, 1))
        }
        
        _cartItems.value = currentItems
    }
    
    /**
     * Incrementa la cantidad de un producto en el carrito.
     */
    fun incrementQuantity(productId: Int) {
        val currentItems = _cartItems.value.toMutableList()
        val item = currentItems.find { it.product.id == productId }
        
        if (item != null) {
            val index = currentItems.indexOf(item)
            currentItems[index] = item.copy(quantity = item.quantity + 1)
            _cartItems.value = currentItems
        }
    }
    
    /**
     * Decrementa la cantidad de un producto en el carrito.
     * Si la cantidad llega a 0, elimina el item.
     */
    fun decrementQuantity(productId: Int) {
        val currentItems = _cartItems.value.toMutableList()
        val item = currentItems.find { it.product.id == productId }
        
        if (item != null) {
            if (item.quantity > 1) {
                val index = currentItems.indexOf(item)
                currentItems[index] = item.copy(quantity = item.quantity - 1)
            } else {
                currentItems.remove(item)
            }
            _cartItems.value = currentItems
        }
    }
    
    /**
     * Elimina un producto del carrito completamente.
     */
    fun removeFromCart(productId: Int) {
        _cartItems.value = _cartItems.value.filter { it.product.id != productId }
    }
    
    /**
     * Vacía el carrito completamente.
     */
    fun clearCart() {
        _cartItems.value = emptyList()
        _studentDiscount.value = false
    }
    
    /**
     * Activa/desactiva el descuento estudiantil.
     */
    fun toggleStudentDiscount() {
        _studentDiscount.value = !_studentDiscount.value
    }
    
    /**
     * Calcula el subtotal del carrito.
     */
    fun calculateSubtotal(): Double {
        return _cartItems.value.sumOf { it.product.price * it.quantity }
    }
    
    /**
     * Calcula el descuento estudiantil (10% si está activo).
     */
    fun calculateDiscount(): Double {
        return if (_studentDiscount.value) {
            calculateSubtotal() * 0.10
        } else {
            0.0
        }
    }
    
    /**
     * Calcula el total con descuento aplicado.
     */
    fun calculateTotal(): Double {
        return calculateSubtotal() - calculateDiscount()
    }
}
