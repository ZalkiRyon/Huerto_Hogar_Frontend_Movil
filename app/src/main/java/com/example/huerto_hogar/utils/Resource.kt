package com.example.huerto_hogar.utils

/**
 * Clase sellada para manejar el estado de las peticiones de red
 * Facilita el manejo de estados de carga, éxito y error en la UI
 */
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T>(data: T? = null) : Resource<T>(data)
}
