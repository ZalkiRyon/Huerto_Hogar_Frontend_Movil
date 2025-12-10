package com.example.huerto_hogar.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.huerto_hogar.MainActivity
import com.example.huerto_hogar.model.CartItem
import com.example.huerto_hogar.model.User
import com.example.huerto_hogar.ui.theme.components.Header
import com.example.huerto_hogar.ui.theme.components.dialogs.Receipt
import com.example.huerto_hogar.ui.theme.components.dialogs.ReceiptDialog
import com.example.huerto_hogar.ui.theme.components.dialogs.generateReceiptNumber
import com.example.huerto_hogar.ui.theme.components.dialogs.getCurrentDateTime
import com.example.huerto_hogar.ui.theme.components.animations.bounceInEffect
import com.example.huerto_hogar.ui.theme.components.animations.pressClickEffect
import com.example.huerto_hogar.viewmodel.CartViewModel

import com.example.huerto_hogar.viewmodel.SalesViewModel
import com.example.huerto_hogar.repository.OrderRepository
import com.example.huerto_hogar.model.OrderRequest
import com.example.huerto_hogar.model.OrderDetailRequest
import com.example.huerto_hogar.utils.Resource
import kotlinx.coroutines.launch
import android.util.Log
import com.example.huerto_hogar.viewmodel.UserViewModel

@Suppress("ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE")
@Composable
fun CartScreen(
    navController: NavHostController,
    cartViewModel: CartViewModel = viewModel(),

    salesViewModel: SalesViewModel = viewModel(),
    userManager: UserViewModel = viewModel(),
    user: User?
) {
    val context = LocalContext.current
    val cartItems by cartViewModel.cartItems.collectAsState()
    val studentDiscount by cartViewModel.studentDiscount.collectAsState()

    // Costo de envío fijo
    val shippingCost = 3000.0
    
    // Repositorio de órdenes y coroutine scope
    val orderRepository = remember { OrderRepository() }
    val scope = rememberCoroutineScope()

    // Recalcular cuando cambian cartItems o studentDiscount
    val subtotal = remember(cartItems) {
        cartViewModel.calculateSubtotal()
    }
    val discount = remember(studentDiscount, cartItems) {
        cartViewModel.calculateDiscount()
    }
    val total = remember(studentDiscount, cartItems) {
        cartViewModel.calculateTotal() + estimatedShippingCost
    }

    var showClearDialog by remember { mutableStateOf(false) }
    var showNFCDialog by remember { mutableStateOf(false) }
    var showReceiptDialog by remember { mutableStateOf(false) }
    var currentReceipt by remember { mutableStateOf<Receipt?>(null) }
    var isCreatingOrder by remember { mutableStateOf(false) }





    /**
     * MANEJO DE ESTADOS NFC
     * 
     * Observa cambios en el estado del proceso NFC y ejecuta acciones según estado:
     * - Success: Cierra diálogo (descuento ya aplicado en callback)
     * - Error: Muestra error por 2 segundos y cierra diálogo
     * - Otros: Sin acción (Idle, Scanning, Processing se manejan en UI del diálogo)
     */

    Scaffold(
        topBar = {
            Header(
                navController = navController,
                title = "Mi Carrito",
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())

        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically

            ) {

                Text(
                    text = "${cartItems.size} producto(s)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
                if (cartItems.isNotEmpty()) {
                    IconButton(
                        onClick = { showClearDialog = true },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = androidx.compose.ui.graphics.Color(0xFFD32F2F)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Vaciar carrito",
                            tint = androidx.compose.ui.graphics.Color.White
                        )
                    }
                }

            }

            if (cartItems.isEmpty()) {
                // Empty cart state
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Carrito vacío",
                            modifier = Modifier
                                .size(80.dp)
                                .bounceInEffect(delay = 0),
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                        )
                        Text(
                            text = "Tu carrito está vacío",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                            modifier = Modifier.bounceInEffect(delay = 100)
                        )
                        Text(
                            text = "Agrega productos desde el catálogo",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.bounceInEffect(delay = 200)
                        )
                    }
                }
            } else {
                // Cart content
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // List of items
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(cartItems) { cartItem ->
                            CartItemCard(
                                cartItem = cartItem,
                                onIncrement = { cartViewModel.incrementQuantity(cartItem.product.id) },
                                onDecrement = { cartViewModel.decrementQuantity(cartItem.product.id) }
                            )
                        }
                    }

                    // Summary section
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.surface,
                        tonalElevation = 4.dp,
                        shadowElevation = 8.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            /**
                             * BOTÓN DE DESCUENTO ESTUDIANTIL (NFC)
                             * 
                             * Permite aplicar descuento del 10% mediante escaneo de tarjeta NFC
                             * 
                             * Estados:
                             * - No aplicado: Botón activo, muestra "Escanear Tarjeta Estudiante"
                             * - Aplicado: Botón deshabilitado, muestra "Descuento Aplicado (10%)"
                             * 
                             * Flujo de validación:
                             * 1. Verificar si dispositivo tiene hardware NFC
                             * 2. Verificar si NFC está habilitado
                             * 3. Iniciar proceso de escaneo
                             * 4. Mostrar diálogo con instrucciones
                             * 
                             * Nota: El descuento NO se puede desactivar una vez aplicado
                             * (evita uso indebido de tarjeta compartida)
                             */


                            HorizontalDivider()

                            // Price breakdown
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Subtotal:",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = "$${subtotal.toInt()}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            if (studentDiscount) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Descuento:",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = "-$${discount.toInt()}",
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Costo Envío:",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = "$${shippingCost.toInt()}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            HorizontalDivider(thickness = 1.dp)

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Total:",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "$${total.toInt()}",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            if (user != null) {
                                // Checkout button
                                Button(
                                    onClick = {
                                        if (isCreatingOrder) return@Button
                                        
                                        isCreatingOrder = true
                                        scope.launch {
                                            try {
                                                // Obtener user ID y token - usar el parámetro user primero, luego userManager
                                                val userId = user?.id ?: userManager.currentUser.value?.id
                                                val token = userManager.getAuthToken()
                                                
                                                Log.d("CartScreen", "UserId: $userId, Token: ${token?.take(20)}...")
                                                Log.d("CartScreen", "User from param: ${user?.id}, User from manager: ${userManager.currentUser.value?.id}")
                                                
                                                if (userId == null || token == null) {
                                                    Log.e("CartScreen", "No user ID or token available - UserId=$userId, Token=$token")
                                                    // Si no hay usuario o token, mostrar el recibo de todos modos (modo offline)
                                                    val receipt = Receipt(
                                                        receiptNumber = generateReceiptNumber(),
                                                        orderNumber = null,
                                                        date = getCurrentDateTime(),
                                                        items = cartItems,
                                                        subtotal = subtotal,
                                                        discount = discount,
                                                        shippingCost = shippingCost,
                                                        total = total,
                                                        hasStudentDiscount = studentDiscount
                                                    )
                                                    
                                                    salesViewModel.addSale(total)
                                                    currentReceipt = receipt
                                                    showReceiptDialog = true
                                                    cartViewModel.clearCart()
                                                    isCreatingOrder = false
                                                    return@launch
                                                }
                                                
                                                // Preparar detalles de la orden
                                                val detalles = cartItems.map { item ->
                                                    OrderDetailRequest(
                                                        productoId = item.product.id,
                                                        cantidad = item.quantity
                                                    )
                                                }
                                                
                                                // Crear request de orden
                                                val orderRequest = OrderRequest(
                                                    clienteId = userId,
                                                    comentario = if (studentDiscount) "Descuento estudiantil aplicado" else null,
                                                    detalles = detalles
                                                )
                                                
                                                Log.d("CartScreen", "Order request: clienteId=$userId, detalles=${detalles.size} items")
                                                
                                                var orderCreated = false
                                                
                                                // Enviar orden al backend
                                                Log.d("CartScreen", "Calling backend API...")
                                                orderRepository.createOrder(orderRequest, token).collect { resource ->
                                                    Log.d("CartScreen", "Resource state: ${resource.javaClass.simpleName}")
                                                    when (resource) {
                                                        is Resource.Success -> {
                                                            Log.d("CartScreen", "Order created successfully: ${resource.data}")
                                                            if (!orderCreated) {
                                                                orderCreated = true
                                                                // Generar boleta con número de orden del backend
                                                                val actualShippingCost = resource.data?.shippingCost?.toDouble() ?: shippingCost
                                                                val actualTotal = subtotal - discount + actualShippingCost
                                                                val receipt = Receipt(
                                                                    receiptNumber = generateReceiptNumber(),
                                                                    orderNumber = resource.data?.orderNumber,
                                                                    date = getCurrentDateTime(),
                                                                    items = cartItems,
                                                                    subtotal = subtotal,
                                                                    discount = discount,
                                                                    shippingCost = actualShippingCost,
                                                                    total = actualTotal,
                                                                    hasStudentDiscount = studentDiscount
                                                                )

                                                                // Registrar venta en el sistema
                                                                salesViewModel.addSale(total)

                                                                // Guardar boleta y mostrar modal
                                                                currentReceipt = receipt
                                                                showReceiptDialog = true

                                                                // Limpiar carrito
                                                                cartViewModel.clearCart()
                                                                
                                                                isCreatingOrder = false
                                                            }
                                                        }
                                                        is Resource.Error -> {
                                                            Log.e("CartScreen", "Error creating order: ${resource.message}")
                                                            if (!orderCreated) {
                                                                // Mostrar recibo de todos modos aunque falle el backend
                                                                val receipt = Receipt(
                                                                    receiptNumber = generateReceiptNumber(),
                                                                    orderNumber = null,
                                                                    date = getCurrentDateTime(),
                                                                    items = cartItems,
                                                                    subtotal = subtotal,
                                                                    discount = discount,
                                                                    shippingCost = shippingCost,
                                                                    total = total,
                                                                    hasStudentDiscount = studentDiscount
                                                                )
                                                                
                                                                salesViewModel.addSale(total)
                                                                currentReceipt = receipt
                                                                showReceiptDialog = true
                                                                cartViewModel.clearCart()
                                                                isCreatingOrder = false
                                                            }
                                                        }
                                                        is Resource.Loading -> {
                                                            // Mostrar indicador de carga
                                                        }
                                                    }
                                                }
                                            } catch (e: Exception) {
                                                Log.e("CartScreen", "Exception during order creation", e)
                                                // En caso de excepción, mostrar el recibo de todos modos
                                                val receipt = Receipt(
                                                    receiptNumber = generateReceiptNumber(),
                                                    orderNumber = null,
                                                    date = getCurrentDateTime(),
                                                    items = cartItems,
                                                    subtotal = subtotal,
                                                    discount = discount,
                                                    shippingCost = shippingCost,
                                                    total = total,
                                                    hasStudentDiscount = studentDiscount
                                                )
                                                
                                                salesViewModel.addSale(total)
                                                currentReceipt = receipt
                                                showReceiptDialog = true
                                                cartViewModel.clearCart()
                                                isCreatingOrder = false
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp)                                        
                                        .bounceInEffect(delay = 100)
                                        .pressClickEffect(),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary
                                    ),
                                    enabled = !isCreatingOrder
                                ) {
                                    if (isCreatingOrder) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(24.dp),
                                            color = MaterialTheme.colorScheme.onPrimary
                                        )
                                    } else {
                                        Text(
                                            text = "Terminar de Pagar",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            } else {
                                // Checkout button
                                Button(
                                    onClick = {
                                        navController.navigate("login_screen")
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp)
                                        .bounceInEffect(delay = 100)
                                        .pressClickEffect(),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary
                                    )
                                ) {
                                    Text(
                                        text = "Iniciar sesión para proceder",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                        }
                    }
                }
            }
        }
    }






    // Clear cart confirmation dialog
    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text("Vaciar Carrito") },
            text = { Text("¿Estás seguro de que deseas vaciar el carrito? Esta acción no se puede deshacer.") },
            confirmButton = {
                Button(
                    onClick = {
                        cartViewModel.clearCart()
                        showClearDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = androidx.compose.ui.graphics.Color(0xFFD32F2F)
                    )
                ) {
                    Text("Vaciar", color = androidx.compose.ui.graphics.Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Receipt Dialog
    if (showReceiptDialog && currentReceipt != null) {
        ReceiptDialog(
            receipt = currentReceipt!!,
            onDismiss = {
                showReceiptDialog = false
                currentReceipt = null
            }
        )
    }
}

@Composable
fun CartItemCard(
    cartItem: CartItem,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Product info
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = cartItem.product.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2
                )
                Text(
                    text = "$${cartItem.product.price.toInt()} c/u",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Total: $${(cartItem.product.price * cartItem.quantity).toInt()}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Quantity controls
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onDecrement,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Reducir cantidad",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Text(
                    text = "${cartItem.quantity}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.widthIn(min = 32.dp),
                    textAlign = TextAlign.Center
                )

                IconButton(
                    onClick = onIncrement,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "Aumentar cantidad",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}