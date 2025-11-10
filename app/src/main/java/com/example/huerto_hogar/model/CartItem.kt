package com.example.huerto_hogar.model

/**
 * Representa un item en el carrito de compras.
 */
data class CartItem(
    val product: Product,
    val quantity: Int
)
