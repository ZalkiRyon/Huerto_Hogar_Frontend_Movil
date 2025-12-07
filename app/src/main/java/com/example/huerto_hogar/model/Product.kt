package com.example.huerto_hogar.model

import com.google.gson.annotations.SerializedName

/**
 * Modelo de producto (DTO) que coincide con el backend
 * 
 * @SerializedName mapea los nombres de campos del JSON a las propiedades de Kotlin
 * Backend envía: nombre, categoria (String), precio, stock, descripcion, imagen
 */
data class Product(
    @SerializedName("id")
    val id: Int = 0,
    
    @SerializedName("nombre")
    val name: String,
    
    @SerializedName("categoria")
    val category: String, // Backend envía String, no enum
    
    @SerializedName("precio")
    val price: Double,
    
    @SerializedName("stock")
    val stock: Int,
    
    @SerializedName("imagen")
    val imageUrl: String? = null, // Nombre del archivo de imagen
    
    @SerializedName("descripcion")
    val description: String = ""
)

enum class ProductCategory(val displayName: String) {
    @SerializedName("VERDURAS")
    VERDURAS("Verduras"),
    
    @SerializedName("FRUTAS")
    FRUTAS("Frutas"),
    
    @SerializedName("ORGANICOS")
    ORGANICOS("Organic.")
}
