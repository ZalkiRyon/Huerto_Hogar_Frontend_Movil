package com.example.huerto_hogar.data.api

import com.example.huerto_hogar.model.Favorite
import retrofit2.Response
import retrofit2.http.*

/**
 * Interfaz de Retrofit para operaciones con favoritos
 */
interface FavoriteApiService {
    
    /**
     * Obtiene todos los favoritos de un usuario
     */
    @GET("favoritos/usuario/{usuarioId}")
    suspend fun getUserFavorites(@Path("usuarioId") usuarioId: Int): Response<List<Favorite>>
    
    /**
     * Agrega un producto a favoritos
     * Body: { "usuarioId": 1, "productoId": 5 }
     */
    @POST("favoritos")
    suspend fun addFavorite(@Body request: Map<String, Int>): Response<Favorite>
    
    /**
     * Elimina un producto de favoritos
     * Body: { "usuarioId": 1, "productoId": 5 }
     */
    @HTTP(method = "DELETE", path = "favoritos", hasBody = true)
    suspend fun removeFavorite(@Body request: Map<String, Int>): Response<Unit>
    
    /**
     * Verifica si un producto es favorito del usuario
     */
    @GET("favoritos/check")
    suspend fun isFavorite(
        @Query("usuarioId") usuarioId: Int,
        @Query("productoId") productoId: Int
    ): Response<Boolean>
}
