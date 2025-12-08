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
    val montoTotal: Int, // Total incluyendo productos y envío desde el backend
    
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
    val comments: String? = null,
    
    @SerializedName("detalles")
    val orderDetails: List<OrderDetail> = emptyList()
) {
    // Propiedad computada para compatibilidad con código existente
    val status: OrderStatus
        get() = OrderStatus(id = 0, name = statusName)
    
    // Subtotal solo de productos (montoTotal - costoEnvio)
    val totalProducts: Int
        get() = montoTotal - shippingCost
    
    // Total final (el backend ya lo calcula correctamente en montoTotal)
    val totalFinal: Int
        get() = montoTotal
}

/**
 * Detalle de producto en la orden
 */
data class OrderDetail(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("nombreProductoSnapshot")
    val productName: String,
    
    @SerializedName("precioUnitarioSnapshot")
    val unitPrice: Int,
    
    @SerializedName("cantidad")
    val quantity: Int,
    
    @SerializedName("subtotal")
    val subtotal: Int,
    
    @SerializedName("imagen")
    val image: String? = null
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
