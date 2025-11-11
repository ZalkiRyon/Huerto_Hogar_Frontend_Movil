package com.example.huerto_hogar.screen

import androidx.compose.foundation.background
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
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.huerto_hogar.AppScreens.AppScreens
import com.example.huerto_hogar.MainActivity
import com.example.huerto_hogar.manager.NFCManager
import com.example.huerto_hogar.model.CartItem
import com.example.huerto_hogar.model.User
import com.example.huerto_hogar.viewmodel.CartViewModel
import com.example.huerto_hogar.viewmodel.NFCState
import com.example.huerto_hogar.viewmodel.NFCViewModel

@Composable
fun CartScreen(
    navController: NavController,
    cartViewModel: CartViewModel = viewModel(),
    nfcViewModel: NFCViewModel = viewModel(),
    currentUser: User? = viewModel()
) {
    val context = LocalContext.current
    val cartItems by cartViewModel.cartItems.collectAsState()
    val studentDiscount by cartViewModel.studentDiscount.collectAsState()
    val nfcState by nfcViewModel.nfcState.collectAsState()

    // Recalcular cuando cambian cartItems o studentDiscount
    val subtotal = remember(cartItems) {
        cartViewModel.calculateSubtotal()
    }
    val discount = remember(studentDiscount, cartItems) {
        cartViewModel.calculateDiscount()
    }
    val total = remember(studentDiscount, cartItems) {
        cartViewModel.calculateTotal()
    }

    var showClearDialog by remember { mutableStateOf(false) }
    var showNFCDialog by remember { mutableStateOf(false) }

    val nfcManager = remember { NFCManager(context as MainActivity) }

    // Observar tags NFC
    DisposableEffect(Unit) {
        MainActivity.onNFCTagDiscovered = { tag ->
            val isValid = nfcViewModel.processNFCTag(tag, nfcManager)
            if (isValid && !studentDiscount) {
                cartViewModel.toggleStudentDiscount()
            }
        }
        onDispose {
            MainActivity.onNFCTagDiscovered = null
        }
    }

    // Manejar estados NFC
    LaunchedEffect(nfcState) {
        when (val state = nfcState) {
            is NFCState.Success -> {
                showNFCDialog = false
                // El descuento ya se aplicó en el callback
            }

            is NFCState.Error -> {
                // Mostrar error por 2 segundos y cerrar
                kotlinx.coroutines.delay(2000)
                nfcViewModel.resetState()
                showNFCDialog = false
            }

            else -> { /* Otros estados */
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.primaryContainer,
            tonalElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Mi Carrito",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "${cartItems.size} producto(s)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                }

                if (cartItems.isNotEmpty()) {
                    IconButton(onClick = { showClearDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Vaciar carrito",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
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
                        modifier = Modifier.size(80.dp),
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                    )
                    Text(
                        text = "Tu carrito está vacío",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                    Text(
                        text = "Agrega productos desde el catálogo",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                        textAlign = TextAlign.Center
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
                        // Student discount button (NFC)
                        OutlinedButton(
                            onClick = {
                                if (!studentDiscount) {
                                    // Verificar disponibilidad de NFC
                                    when {
                                        !nfcManager.isNFCAvailable() -> {
                                            nfcViewModel.handleNFCNotAvailable()
                                            showNFCDialog = true
                                        }

                                        !nfcManager.isNFCEnabled() -> {
                                            nfcViewModel.handleNFCDisabled()
                                            showNFCDialog = true
                                        }

                                        else -> {
                                            nfcViewModel.startScanning()
                                            showNFCDialog = true
                                        }
                                    }
                                }
                                // No hacer nada si ya está aplicado (no se puede desactivar)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !studentDiscount, // Deshabilitar cuando está aplicado
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (studentDiscount)
                                    MaterialTheme.colorScheme.primaryContainer
                                else
                                    MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Icon(
                                imageVector = if (studentDiscount) Icons.Default.Check else Icons.Default.AccountCircle,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                if (studentDiscount)
                                    "Descuento Estudiante Aplicado (10%)"
                                else
                                    "Escanear Tarjeta Estudiante"
                            )
                        }

                        Divider()

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

                        if (currentUser == null) {
                            Button(
                                onClick = { navController.navigate(AppScreens.LoginScreen.route) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Text(
                                    text = "Iniciar sesión",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        } else {

                            Button(
                                onClick = { /* TODO: Implement checkout */ },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Text(
                                    text = "Terminar de Pagar",
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

    // NFC Scanning Dialog
    if (showNFCDialog) {
        AlertDialog(
            onDismissRequest = {
                showNFCDialog = false
                nfcViewModel.cancelScanning()
            },
            title = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "NFC",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text("Descuento Estudiante")
                }
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    when (nfcState) {
                        is NFCState.Scanning -> {
                            CircularProgressIndicator()
                            Text(
                                "Acerca tu tarjeta estudiantil al lector NFC",
                                textAlign = TextAlign.Center
                            )
                            Text(
                                "(Parte trasera del teléfono)",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }

                        is NFCState.Processing -> {
                            CircularProgressIndicator()
                            Text("Procesando tarjeta...", textAlign = TextAlign.Center)
                        }

                        is NFCState.Success -> {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Éxito",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(48.dp)
                            )
                            Text(
                                "¡Descuento aplicado correctamente!",
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        is NFCState.Error -> {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Error",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(48.dp)
                            )
                            Text(
                                (nfcState as NFCState.Error).message,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.error
                            )
                        }

                        else -> {
                            Text("Preparando lector NFC...", textAlign = TextAlign.Center)
                        }
                    }
                }
            },
            confirmButton = {
                if (nfcState is NFCState.Success || nfcState is NFCState.Error) {
                    TextButton(onClick = {
                        showNFCDialog = false
                        nfcViewModel.resetState()
                    }) {
                        Text("Cerrar")
                    }
                }
            },
            dismissButton = {
                if (nfcState is NFCState.Scanning || nfcState is NFCState.Idle) {
                    TextButton(onClick = {
                        showNFCDialog = false
                        nfcViewModel.cancelScanning()
                    }) {
                        Text("Cancelar")
                    }
                }
            }
        )
    }

    // Clear cart confirmation dialog
    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text("Vaciar Carrito") },
            text = { Text("¿Estás seguro de que deseas vaciar el carrito? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        cartViewModel.clearCart()
                        showClearDialog = false
                    }
                ) {
                    Text("Vaciar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text("Cancelar")
                }
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