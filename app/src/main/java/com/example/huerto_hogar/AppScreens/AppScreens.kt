package com.example.huerto_hogar.AppScreens

sealed class AppScreens(val route: String) {
    object HomeScreen: AppScreens(route = "home_screen")
    object LoginScreen: AppScreens(route = "login_screen")
    object RegistroScreen: AppScreens(route = "registro_screen")
    object CartScreen: AppScreens(route = "cart_screen")
    object FavScreen: AppScreens(route = "fav_screen")
    object UsSetScreen: AppScreens(route = "usset_screen")
    object BlogScreen: AppScreens(route = "blog_screen")
    object VerdurasScreen: AppScreens(route = "verduras_screen")
    object FrutasScreen: AppScreens(route = "frutas_screen")
    object OrganicosScreen: AppScreens(route = "organicos_screen")
    object CatalogoScreen: AppScreens(route = "catalogo_screen")
    object AllProductsScreen: AppScreens(route = "all_products_screen")
    
    // Admin routes
    object AdminDashboardScreen: AppScreens(route = "admin_dashboard_screen")
    object AdminInventoryScreen: AppScreens(route = "admin_inventory_screen")
    object AdminUsersScreen: AppScreens(route = "admin_users_screen")
}