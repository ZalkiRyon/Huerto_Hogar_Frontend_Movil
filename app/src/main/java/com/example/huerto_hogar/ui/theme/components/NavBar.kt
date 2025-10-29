package com.example.huerto_hogar.ui.theme.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart

import androidx.compose.ui.graphics.vector.ImageVector
import com.example.huerto_hogar.AppScreens.AppScreens

//Clase para definir las vistas de la barra
enum class BottomNavViews(
    val route: String?, val icon: ImageVector, val label: String, val contentDescription: String
) {
    // Por orden de izquierda a derecha
    HOME(
        AppScreens.HomeScreen.route,
        Icons.Default.Home,
        "Home",
        "Home"
    ),
    FAVORITOS(
        AppScreens.FavScreen.route,
        Icons.Default.Favorite,
        "Favoritos",
        "Favoritos"
    ),
    CARRITO(AppScreens.CartScreen.route, Icons.Default.ShoppingCart, "Carrito", "Carrito"),
    CATALOGO(AppScreens.CatalogScreen.route, Icons.Default.Search, "Catálogo", "Catálogo"),
    MENU(null, Icons.Default.Menu, "Menú", "Menú")
}
