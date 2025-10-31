package com.example.huerto_hogar.AppScreens

sealed class AppScreens(val route: String) {
    object HomeScreen: AppScreens(route = "home_screen")
    object LoginScreen: AppScreens(route = "login_screen")
    object RegistroScreen: AppScreens(route = "registro_screen")
    object CartScreen: AppScreens(route = "cart_screen")
    object FavScreen: AppScreens(route = "fav_screen")
    object UsSettScreen: AppScreens(route = "ussett_screen")
    object blogScreen: AppScreens(route = "blog_screen")
    object VerdurasScreen: AppScreens(route = "verduras_screen")
    object FrutasScreen: AppScreens(route = "frutas_screen")
    object OrganicosScreen: AppScreens(route = "organicos_screen")
}