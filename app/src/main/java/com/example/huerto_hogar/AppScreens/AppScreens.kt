package com.example.huerto_hogar.AppScreens

sealed class AppScreens(val route: String) {
    object HomeScreen : AppScreens(route = "home_screen")
    object LoginScreen : AppScreens(route = "login_screen")
    object RegistroScreen : AppScreens(route = "registro_screen")
    object CartScreen : AppScreens(route = "cart_screen")
    object FavScreen : AppScreens(route = "fav_screen")
    object CatalogScreen : AppScreens(route = "catalog_screen")
    object UsSettScreen : AppScreens(route = "ussett_screen")
    object blogScreen : AppScreens(route = "blog_screen")

}