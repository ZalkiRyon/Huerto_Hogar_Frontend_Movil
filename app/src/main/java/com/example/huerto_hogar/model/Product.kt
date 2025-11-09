package com.example.huerto_hogar.model

/**
 * Modelo de producto para el inventario de la tienda.
 */
data class Product(
    val id: Int,
    val name: String,
    val category: ProductCategory,
    val price: Double,
    val stock: Int,
    val imageUrl: Int? = null,
    val description: String = ""
)

enum class ProductCategory(val displayName: String) {
    VERDURAS("Verduras"),
    FRUTAS("Frutas"),
    ORGANICOS("Orgánicos")
}

/**
 * Datos mock para desarrollo - inventario inicial
 */
object MockProducts {
    val products = listOf(
        Product(1, "Manzanas Fuji", ProductCategory.FRUTAS, 1200.0, 45, description = "Manzanas frescas importadas"),
        Product(2, "Naranjas Valencia", ProductCategory.FRUTAS, 1000.0, 60, description = "Naranjas dulces de temporada"),
        Product(3, "Plátanos Cavendish", ProductCategory.FRUTAS, 800.0, 80, description = "Plátanos maduros"),
        Product(4, "Tomates Cherry", ProductCategory.VERDURAS, 1500.0, 30, description = "Tomates orgánicos"),
        Product(5, "Lechugas Hidropónicas", ProductCategory.ORGANICOS, 900.0, 25, description = "Cultivo hidropónico"),
        Product(6, "Zanahorias", ProductCategory.VERDURAS, 700.0, 50, description = "Zanahorias frescas"),
        Product(7, "Espinacas Orgánicas", ProductCategory.ORGANICOS, 1100.0, 20, description = "100% orgánicas"),
        Product(8, "Pimientos Rojos", ProductCategory.VERDURAS, 1300.0, 35, description = "Pimientos frescos")
    )
}
