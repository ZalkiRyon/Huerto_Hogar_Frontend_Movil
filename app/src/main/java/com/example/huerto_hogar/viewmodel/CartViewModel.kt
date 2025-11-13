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
    
    // Lista de items en el carrito (producto + cantidad)
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()
    
    /**
     * Estado del descuento estudiantil (10%)
     * 
     * Se activa mediante escaneo NFC de tarjeta estudiantil
     * Una vez activado, NO puede desactivarse hasta limpiar el carrito
     *     
     */
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
     * Vacía el carrito completamente y resetea el descuento estudiantil
     * 
     * Se llama al:
     * - Completar una compra
     * - Usuario solicita vaciar carrito manualmente
     */
    fun clearCart() {
        _cartItems.value = emptyList()
        _studentDiscount.value = false  // Resetear descuento al limpiar carrito
    }
    
    /**
     * Activa el descuento estudiantil mediante escaneo NFC
     * 
     * IMPORTANTE: En la UI solo se permite activar (no desactivar) el descuento
     * Esta funci\u00f3n es toggle para flexibilidad futura, pero en CartScreen
     * solo se llama cuando !studentDiscount
     * 
     * Se activa cuando:
     * - Usuario escanea tarjeta NFC v\u00e1lida
     * - MVP: Cualquier tarjeta NFC es v\u00e1lida
     */
    fun toggleStudentDiscount() {
        _studentDiscount.value = !_studentDiscount.value
    }
    
    /**
     * Calcula el subtotal del carrito (suma de precio * cantidad de todos los items)
     * @return Subtotal antes de aplicar descuentos
     */
    fun calculateSubtotal(): Double {
        return _cartItems.value.sumOf { it.product.price * it.quantity }
    }
    
    /**
     * Calcula el monto de descuento estudiantil
     * 
     * @return 10% del subtotal si el descuento est\u00e1 activo, 0 en caso contrario
     */
    fun calculateDiscount(): Double {
        return if (_studentDiscount.value) {
            calculateSubtotal() * 0.10  // Descuento fijo del 10%
        } else {
            0.0
        }
    }
    
    /**
     * Calcula el total final a pagar
     * @return Subtotal menos descuento estudiantil (si aplica)
     */
    fun calculateTotal(): Double {
        return calculateSubtotal() - calculateDiscount()
    }
}
