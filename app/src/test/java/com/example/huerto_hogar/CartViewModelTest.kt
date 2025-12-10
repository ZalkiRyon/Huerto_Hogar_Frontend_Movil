package com.example.huerto_hogar

import com.example.huerto_hogar.model.Product
import com.example.huerto_hogar.viewmodel.CartViewModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Test Unitario #1: Gestión del Carrito de Compras (Mock de datos)
 * 
 * Verifica la funcionalidad básica del carrito:
 * - Agregar productos
 * - Incrementar/decrementar cantidades
 * - Calcular subtotales y totales
 */
class CartViewModelTest {

    private lateinit var cartViewModel: CartViewModel

    // Mock de productos de prueba
    private val mockProducto1 = Product(
        id = 1,
        name = "FR001 - Manzanas Fuji",
        category = "Frutas frescas",
        price = 1200.0,
        stock = 150,
        description = "Manzanas frescas",
        imageUrl = null
    )

    private val mockProducto2 = Product(
        id = 2,
        name = "VR001 - Zanahorias Organicas",
        category = "Verduras organicas",
        price = 900.0,
        stock = 100,
        description = "Zanahorias orgánicas",
        imageUrl = null
    )

    @Before
    fun setup() {
        cartViewModel = CartViewModel()
    }

    @Test
    fun agregar_producto_al_carrito_incrementa_cantidad() {
        // Agregar producto por primera vez
        cartViewModel.addToCart(mockProducto1)
        
        // Verificar que el carrito tiene 1 item
        assertEquals(1, cartViewModel.cartItems.value.size)
        assertEquals(1, cartViewModel.cartItems.value[0].quantity)
        
        // Agregar el mismo producto nuevamente
        cartViewModel.addToCart(mockProducto1)
        
        // Verificar que la cantidad se incrementó
        assertEquals(1, cartViewModel.cartItems.value.size)
        assertEquals(2, cartViewModel.cartItems.value[0].quantity)
    }

    @Test
    fun calcular_subtotal_con_multiples_productos() {
        // Agregar productos al carrito
        cartViewModel.addToCart(mockProducto1) // 1200 x 1
        cartViewModel.addToCart(mockProducto1) // 1200 x 2
        cartViewModel.addToCart(mockProducto2) // 900 x 1
        
        // Calcular subtotal esperado: (1200 * 2) + (900 * 1) = 3300
        val subtotalEsperado = 3300.0
        val subtotalActual = cartViewModel.calculateSubtotal()
        
        assertEquals(subtotalEsperado, subtotalActual, 0.01)
    }

    @Test
    fun decrementar_cantidad_elimina_item_si_llega_a_cero() {
        // Agregar producto
        cartViewModel.addToCart(mockProducto1)
        
        // Verificar que está en el carrito
        assertEquals(1, cartViewModel.cartItems.value.size)
        
        // Decrementar cantidad (de 1 a 0 = eliminar)
        cartViewModel.decrementQuantity(mockProducto1.id)
        
        // Verificar que el carrito está vacío
        assertEquals(0, cartViewModel.cartItems.value.size)
    }

    @Test
    fun limpiar_carrito_elimina_todos_los_items() {
        // Agregar varios productos
        cartViewModel.addToCart(mockProducto1)
        cartViewModel.addToCart(mockProducto2)
        
        // Verificar que hay items
        assertTrue(cartViewModel.cartItems.value.isNotEmpty())
        
        // Limpiar carrito
        cartViewModel.clearCart()
        
        // Verificar que está vacío
        assertEquals(0, cartViewModel.cartItems.value.size)
    }
}
