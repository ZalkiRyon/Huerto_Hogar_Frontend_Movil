package com.example.huerto_hogar.utils

/**
 * Constantes globales de la aplicación
 */
object Constants {
    // Configuración de URL del backend
    // EMULADOR: usa 10.0.2.2 (alias para localhost del host)
    // DISPOSITIVO FÍSICO: usa la IP local de tu PC (ejemplo: 192.168.1.5)
    const val BASE_URL = "http://10.0.2.2:8080/api/" // Para emulador Android
    // const val BASE_URL = "http://192.168.1.5:8080/api/" // Para dispositivo físico - CAMBIA LA IP
    // const val BASE_URL = "http://localhost:8080/api/" // Para desarrollo web
    // const val BASE_URL = "https://tu-backend.com/api/" // Para producción
    
    // Timeouts para peticiones HTTP
    const val CONNECT_TIMEOUT = 30L // segundos
    const val READ_TIMEOUT = 30L // segundos
    const val WRITE_TIMEOUT = 30L // segundos
    
    // Configuración de logging
    const val ENABLE_LOGGING = true
    
    // Endpoints - Rutas del backend en español
    object Endpoints {
        const val PRODUCTS = "productos"
        const val PRODUCTS_BY_CATEGORY = "productos/category/{category}"
        const val USERS = "usuarios"
        const val USER_LOGIN = "usuarios/login"
        const val USER_REGISTER = "usuarios/register"
        const val BLOGS = "blogs"
        const val ORDERS = "ordenes"
        const val CART = "carrito"
    }
    
    // Claves de SharedPreferences
    object PrefsKeys {
        const val USER_ID = "user_id"
        const val USER_TOKEN = "user_token"
        const val IS_LOGGED_IN = "is_logged_in"
    }
}
