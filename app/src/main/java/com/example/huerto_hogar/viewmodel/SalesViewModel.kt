package com.example.huerto_hogar.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel para gestionar las ventas diarias.
 * Mantiene el total de ventas acumuladas durante la sesión de la app.
 */
class SalesViewModel : ViewModel() {

    private val _dailySales = MutableStateFlow(0.0)
    val dailySales: StateFlow<Double> = _dailySales.asStateFlow()

    /**
     * Agrega una venta al total diario.
     * El total se reinicia automáticamente al cerrar la app.
     */
    fun addSale(amount: Double) {
        _dailySales.value += amount
    }
}

