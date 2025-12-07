package com.example.huerto_hogar.utils

/**
 * Constantes globales de la aplicación
 */
object Constants {
    // TODO: Reemplazar con la URL real de tu backend
    const val BASE_URL = "http://10.0.2.2:8080/api/" // Para emulador Android
    // const val BASE_URL = "http://localhost:8080/api/" // Para desarrollo web
    // const val BASE_URL = "https://tu-backend.com/api/" // Para producción
    
    // Timeouts para peticiones HTTP
    const val CONNECT_TIMEOUT = 30L // segundos
    const val READ_TIMEOUT = 30L // segundos
    const val WRITE_TIMEOUT = 30L // segundos
    
    // Configuración de logging
    const val ENABLE_LOGGING = true
    
    // Endpoints
    object Endpoints {
        const val PRODUCTS = "products"
        const val PRODUCTS_BY_CATEGORY = "products/category/{category}"
        const val USERS = "users"
        const val USER_LOGIN = "users/login"
        const val USER_REGISTER = "users/register"
        const val BLOGS = "blogs"
        const val ORDERS = "orders"
        const val CART = "cart"
    }
    
    // Claves de SharedPreferences
    object PrefsKeys {
        const val USER_ID = "user_id"
        const val USER_TOKEN = "user_token"
        const val IS_LOGGED_IN = "is_logged_in"
    }
}
