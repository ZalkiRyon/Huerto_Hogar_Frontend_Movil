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
    
    @SerializedName("categoriaProducto")
    val categoriaProducto: String,
    
    @SerializedName("precioProducto")
    val precioProducto: Int,
    
    @SerializedName("stockProducto")
    val stockProducto: Int,
    
    @SerializedName("descripcionProducto")
    val descripcionProducto: String,
    
    @SerializedName("imagenProducto")
    val imagenProducto: String?
) {
    // Convertir a Product para compatibilidad con UI existente
    fun toProduct(): Product {
        return Product(
            id = productoId,
            name = nombreProducto,
            category = categoriaProducto,
            price = precioProducto.toDouble(),
            stock = stockProducto,
            description = descripcionProducto,
            imageUrl = imagenProducto,
            activo = true // Los favoritos solo devuelven productos activos
        )
    }
}
