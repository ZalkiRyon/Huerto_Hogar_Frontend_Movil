package com.example.huerto_hogar.model

import com.google.gson.annotations.SerializedName

/**
 * Modelo de Orden (Pedido) - mapea respuesta del backend
 */
data class Order(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("numeroOrden")
    val orderNumber: String,
    
    @SerializedName("fecha")
    val createdDate: String,
    
    @SerializedName("estado")
    val statusName: String, // Backend devuelve String directamente, no objeto
    
    @SerializedName("montoTotal")
    val totalProducts: Int,
    
    @SerializedName("costoEnvio")
    val shippingCost: Int,
    
    @SerializedName("nombreClienteSnapshot")
    val customerName: String,
    
    @SerializedName("emailClienteSnapshot")
    val customerEmail: String,
    
    @SerializedName("direccionEnvio")
    val address: String,
    
    @SerializedName("regionEnvio")
    val region: String,
    
    @SerializedName("comunaEnvio")
    val comuna: String,
    
    @SerializedName("telefonoContacto")
    val phone: String,
    
    @SerializedName("comentario")
    val comments: String? = null
) {
    // Propiedad computada para compatibilidad con código existente
    val status: OrderStatus
        get() = OrderStatus(id = 0, name = statusName)
}

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
