package com.example.huerto_hogar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.huerto_hogar.model.Order
import com.example.huerto_hogar.model.OrderStats
import com.example.huerto_hogar.repository.OrderRepository
import com.example.huerto_hogar.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * ViewModel para gestionar las órdenes y estadísticas de ventas.
 */
class SalesViewModel(
    private val orderRepository: OrderRepository = OrderRepository()
) : ViewModel() {

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()
    
    private val _selectedOrder = MutableStateFlow<Order?>(null)
    val selectedOrder: StateFlow<Order?> = _selectedOrder.asStateFlow()
    
    private val _orderStats = MutableStateFlow(OrderStats())
    val orderStats: StateFlow<OrderStats> = _orderStats.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Mantener compatibilidad con código anterior
    val dailySales: StateFlow<Double> = MutableStateFlow(0.0).apply {
        viewModelScope.launch {
            orderStats.collect { stats ->
                value = stats.todaySales
            }
        }
    }.asStateFlow()

    init {
        loadOrders()
    }

    /**
     * Carga todas las órdenes desde el backend
     */
    fun loadOrders() {
        viewModelScope.launch {
            orderRepository.getAllOrders().collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _isLoading.value = true
                        _errorMessage.value = null
                    }
                    is Resource.Success -> {
                        _isLoading.value = false
                        resource.data?.let { ordersList ->
                            _orders.value = ordersList
                            calculateStats(ordersList)
                        }
                    }
                    is Resource.Error -> {
                        _isLoading.value = false
                        _errorMessage.value = resource.message
                        // Usar datos placeholder si falla la carga
                        calculateStats(emptyList())
                    }
                }
            }
        }
    }

    /**
     * Calcula estadísticas basadas en las órdenes
     */
    private fun calculateStats(orders: List<Order>) {
        val today = try {
            LocalDate.now().toString()
        } catch (e: Exception) {
            ""
        }
        
        val todayOrders = orders.filter { order ->
            order.createdDate.startsWith(today)
        }
        
        val totalSales = orders.sumOf { (it.totalProducts + it.shippingCost).toDouble() }
        val todaySales = todayOrders.sumOf { (it.totalProducts + it.shippingCost).toDouble() }
        
        val pendingOrders = orders.count { it.status.name.contains("pendiente", ignoreCase = true) }
        val completedOrders = orders.count { it.status.name.contains("entregado", ignoreCase = true) }
        
        _orderStats.value = OrderStats(
            totalOrders = orders.size,
            pendingOrders = pendingOrders,
            completedOrders = completedOrders,
            totalSales = totalSales,
            todaySales = todaySales
        )
    }

    /**
     * Carga una orden específica por ID (incluye detalles completos con productos)
     */
    fun loadOrderById(orderId: Int) {
        viewModelScope.launch {
            orderRepository.getOrderById(orderId).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _isLoading.value = true
                    }
                    is Resource.Success -> {
                        _isLoading.value = false
                        resource.data?.let { order ->
                            _selectedOrder.value = order
                        }
                    }
                    is Resource.Error -> {
                        _isLoading.value = false
                        _errorMessage.value = resource.message
                    }
                }
            }
        }
    }

    /**
     * Agrega una venta al total diario (método legacy para compatibilidad).
     */
    fun addSale(amount: Double) {
        // Recargar órdenes para obtener datos actualizados
        loadOrders()
    }
}

