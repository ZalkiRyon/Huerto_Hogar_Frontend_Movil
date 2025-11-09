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
    ORGANICOS("Organic.")
}

/**
 * Datos mock para desarrollo - inventario inicial
 */
object MockProducts {
    val products = listOf(
        Product(1, "Manzanas Fuji", ProductCategory.FRUTAS, 1200.0, 45, com.example.huerto_hogar.R.drawable.manzana_fuji, "Manzanas frescas importadas"),
        Product(2, "Naranjas Valencia", ProductCategory.FRUTAS, 1000.0, 60, com.example.huerto_hogar.R.drawable.naranja_valencia, "Naranjas dulces de temporada"),
        Product(3, "Plátanos Cavendish", ProductCategory.FRUTAS, 800.0, 80, com.example.huerto_hogar.R.drawable.platano, "Plátanos maduros"),
        Product(4, "Pimientos Rojos", ProductCategory.VERDURAS, 1300.0, 35, com.example.huerto_hogar.R.drawable.pimientos, "Pimientos frescos rojos"),
        Product(5, "Espinacas Orgánicas", ProductCategory.ORGANICOS, 1100.0, 20, com.example.huerto_hogar.R.drawable.espinaca, "100% orgánicas"),
        Product(6, "Zanahorias", ProductCategory.VERDURAS, 700.0, 50, com.example.huerto_hogar.R.drawable.zanahorias, "Zanahorias frescas"),
        Product(7, "Naranjas Orgánicas", ProductCategory.ORGANICOS, 1500.0, 25, com.example.huerto_hogar.R.drawable.orange, "Cultivo orgánico"),
        Product(8, "Zanahorias Premium", ProductCategory.VERDURAS, 900.0, 30, com.example.huerto_hogar.R.drawable.carrot, "Zanahorias seleccionadas")
    )
}
