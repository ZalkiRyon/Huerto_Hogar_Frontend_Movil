package com.example.huerto_hogar.model

import com.google.gson.annotations.SerializedName

/**
 * Modelo de producto (DTO) que coincide con el backend
 * 
 * @SerializedName mapea los nombres de campos del JSON a las propiedades de Kotlin
 * imageUrl ahora es String para URLs remotas (antes era Int para recursos drawable)
 */
data class Product(
    @SerializedName("id")
    val id: Int = 0,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("category")
    val category: ProductCategory,
    
    @SerializedName("price")
    val price: Double,
    
    @SerializedName("stock")
    val stock: Int,
    
    @SerializedName("imageUrl")
    val imageUrl: String? = null, // Ahora es String para URLs remotas
    
    @SerializedName("description")
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
