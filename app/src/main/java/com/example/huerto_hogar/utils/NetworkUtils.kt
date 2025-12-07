package com.example.huerto_hogar.utils

import retrofit2.HttpException
import java.io.IOException

/**
 * Utilidades para manejo de errores de red
 */
object NetworkUtils {
    
    /**
     * Maneja excepciones de red y devuelve mensajes de error amigables
     */
    fun handleNetworkException(exception: Exception): String {
        return when (exception) {
            is HttpException -> {
                when (exception.code()) {
                    400 -> "Solicitud incorrecta"
                    401 -> "No autorizado. Por favor inicia sesión"
                    403 -> "Acceso prohibido"
                    404 -> "Recurso no encontrado"
                    500 -> "Error del servidor"
                    503 -> "Servicio no disponible"
                    else -> "Error de red: ${exception.code()}"
                }
            }
            is IOException -> "Error de conexión. Verifica tu internet"
            else -> exception.message ?: "Error desconocido"
        }
    }
    
    /**
     * Verifica si hay conexión a internet
     * Nota: Requiere implementación adicional con ConnectivityManager
     */
    fun isNetworkAvailable(): Boolean {
        // TODO: Implementar verificación de red real
        return true
    }
}
