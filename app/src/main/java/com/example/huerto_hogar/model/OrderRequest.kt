package com.example.huerto_hogar.model

import com.google.gson.annotations.SerializedName

/**
 * DTO para crear una orden - coincide con OrdenRequestDTO del backend
 */
data class OrderRequest(
    @SerializedName("clienteId")
    val clienteId: Int,
    
    @SerializedName("comentario")
    val comentario: String? = null,
    
    @SerializedName("detalles")
    val detalles: List<OrderDetailRequest>
)

/**
 * DTO para detalle de orden - coincide con DetalleOrdenRequestDTO del backend
 */
data class OrderDetailRequest(
    @SerializedName("productoId")
    val productoId: Int,
    
    @SerializedName("cantidad")
    val cantidad: Int
)
