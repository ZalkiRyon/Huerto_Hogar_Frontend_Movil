package com.example.huerto_hogar.data.di

import com.example.huerto_hogar.data.api.BlogApiService
import com.example.huerto_hogar.data.api.ProductApiService
import com.example.huerto_hogar.data.api.UserApiService
import com.example.huerto_hogar.utils.Constants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Módulo de inyección de dependencias para configurar Retrofit y servicios de API
 * 
 * Este objeto singleton proporciona instancias configuradas de Retrofit y los servicios de API
 * Implementa el patrón Singleton para garantizar una única instancia de cada servicio
 */
object NetworkModule {
    
    /**
     * Configuración de Gson para serialización/deserialización JSON
     * - setLenient: Permite JSON con formato relajado
     * - serializeNulls: Incluye campos nulos en JSON
     */
    private val gson: Gson by lazy {
        GsonBuilder()
            .setLenient()
            .serializeNulls()
            .create()
    }
    
    /**
     * Interceptor de logging para debugging de peticiones HTTP
     * Solo activo en modo debug según Constants.ENABLE_LOGGING
     */
    private val loggingInterceptor: HttpLoggingInterceptor by lazy {
        HttpLoggingInterceptor().apply {
            level = if (Constants.ENABLE_LOGGING) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }
    
    /**
     * Cliente HTTP configurado con timeouts e interceptors
     * - Timeouts personalizados para conexión, lectura y escritura
     * - Logging interceptor para debugging
     * - Retry automático en caso de fallo
     */
    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(Constants.CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(Constants.READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(Constants.WRITE_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .retryOnConnectionFailure(true)
            .build()
    }
    
    /**
     * Instancia principal de Retrofit
     * - Base URL configurada desde Constants
     * - Convertidor Gson para JSON
     * - Cliente HTTP personalizado
     */
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
    
    /**
     * Servicio de API para operaciones con productos
     */
    val productApiService: ProductApiService by lazy {
        retrofit.create(ProductApiService::class.java)
    }
    
    /**
     * Servicio de API para operaciones con usuarios
     */
    val userApiService: UserApiService by lazy {
        retrofit.create(UserApiService::class.java)
    }
    
    /**
     * Servicio de API para operaciones con blogs
     */
    val blogApiService: BlogApiService by lazy {
        retrofit.create(BlogApiService::class.java)
    }
}
