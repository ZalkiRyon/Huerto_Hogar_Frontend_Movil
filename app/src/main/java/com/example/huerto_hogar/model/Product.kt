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
        Product(1, "Manzanas Fuji", ProductCategory.FRUTAS, 1200.0, 45, com.example.huerto_hogar.R.drawable.manzana, "Manzanas Fuji crujientes y dulces, cultivadas en el Valle del Maule. Perfectas para meriendas saludables o como ingrediente en postres. Estas manzanas son conocidas por su textura firme y su sabor equilibrado entre dulce y ácido."),
        Product(2, "Naranjas Valencia", ProductCategory.FRUTAS, 1000.0, 60, com.example.huerto_hogar.R.drawable.naranja, "Jugosas y ricas en vitamina C, estas naranjas Valencia son ideales para zumos frescos y refrescantes. Cultivadas en condiciones climáticas óptimas que aseguran su dulzura y jugosidad."),
        Product(3, "Plátanos Cavendish", ProductCategory.FRUTAS, 800.0, 80, com.example.huerto_hogar.R.drawable.platano, "Plátanos maduros y dulces, perfectos para el desayuno o como snack energético. Estos plátanos son ricos en potasio y vitaminas, ideales para mantener una dieta equilibrada."),
        Product(4, "Zanahorias Orgánicas", ProductCategory.VERDURAS, 1300.0, 35, com.example.huerto_hogar.R.drawable.zanahoria, "Zanahorias crujientes cultivadas sin pesticidas en la Región de O'Higgins. Excelente fuente de vitamina A y fibra, ideales para ensaladas, jugos o como snack saludable."),
        Product(5, "Espinacas Frescas", ProductCategory.VERDURAS, 1100.0, 20, com.example.huerto_hogar.R.drawable.espinaca, "Espinacas frescas y nutritivas, perfectas para ensaladas y batidos verdes. Estas espinacas son cultivadas bajo prácticas orgánicas que garantizan su calidad y valor nutricional."),
        Product(6, "Pimentones Tricolores", ProductCategory.VERDURAS, 700.0, 50, com.example.huerto_hogar.R.drawable.pimenton, "Pimientos rojos, amarillos y verdes, ideales para salteados y platos coloridos. Ricos en antioxidantes y vitaminas, estos pimientos añaden un toque vibrante y saludable a cualquier receta."),
        Product(7, "Quinua Orgánica", ProductCategory.ORGANICOS, 1500.0, 25, com.example.huerto_hogar.R.drawable.quinoa, "Grano andino altamente nutritivo, ideal para ensaladas, sopas y guarniciones. La quinua es una excelente fuente de proteínas completas y fibra, cultivada sin el uso de pesticidas."),
        Product(8, "Leche Entera", ProductCategory.ORGANICOS, 900.0, 30, com.example.huerto_hogar.R.drawable.leche, "Leche fresca y pasteurizada, rica en calcio y vitaminas. Ideal para el consumo diario, ya sea sola o como ingrediente en diversas recetas."),
        Product(9, "Miel Orgánica", ProductCategory.ORGANICOS, 900.0, 30, com.example.huerto_hogar.R.drawable.miel, "Miel pura y orgánica producida por apicultores locales. Rica en antioxidantes y con un sabor inigualable, perfecta para endulzar de manera natural tus comidas y bebidas.")
    )
}
