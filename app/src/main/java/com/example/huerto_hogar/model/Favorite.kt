package com.example.huerto_hogar.model

import com.google.gson.annotations.SerializedName

/**
 * Modelo de Favorito - mapea respuesta del backend FavoritoResponseDTO
 */
data class Favorite(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("productoId")
    val productoId: Int,
    
    @SerializedName("nombreProducto")
    val nombreProducto: String,
    
    @SerializedName("precioProducto")
    val precioProducto: Int,
    
    @SerializedName("imagenProducto")
    val imagenProducto: String?
) {
    // Convertir a Product para compatibilidad con UI existente
    fun toProduct(): Product {
        return Product(
            id = productoId,
            name = nombreProducto,
            category = "", // No viene en el DTO
            price = precioProducto.toDouble(),
            stock = 0, // No viene en el DTO
            description = "",
            imageUrl = imagenProducto,
            activo = true
        )
    }
}
