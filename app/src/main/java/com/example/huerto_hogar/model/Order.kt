package com.example.huerto_hogar.model

import com.google.gson.annotations.SerializedName

/**
 * Modelo de Orden (Pedido)
 */
data class Order(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("numeroOrden")
    val orderNumber: String,
    
    @SerializedName("usuario")
    val user: User,
    
    @SerializedName("nombreCliente")
    val customerName: String,
    
    @SerializedName("emailCliente")
    val customerEmail: String,
    
    @SerializedName("direccion")
    val address: String,
    
    @SerializedName("region")
    val region: String,
    
    @SerializedName("comuna")
    val comuna: String,
    
    @SerializedName("telefono")
    val phone: String,
    
    @SerializedName("fechaCreacion")
    val createdDate: String,
    
    @SerializedName("estado")
    val status: OrderStatus,
    
    @SerializedName("totalProductos")
    val totalProducts: Int,
    
    @SerializedName("costoEnvio")
    val shippingCost: Int,
    
    @SerializedName("comentario")
    val comments: String? = null
)

/**
 * Estado de la orden
 */
data class OrderStatus(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("nombre")
    val name: String
)

/**
 * Estadísticas de órdenes para el dashboard
 */
data class OrderStats(
    val totalOrders: Int = 0,
    val pendingOrders: Int = 0,
    val completedOrders: Int = 0,
    val totalSales: Double = 0.0,
    val todaySales: Double = 0.0
)
