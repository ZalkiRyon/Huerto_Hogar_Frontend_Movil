package com.example.huerto_hogar.screen.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.huerto_hogar.model.Order
import com.example.huerto_hogar.repository.OrderRepository
import com.example.huerto_hogar.ui.theme.components.ConfirmationDialog
import com.example.huerto_hogar.utils.Resource
import com.example.huerto_hogar.viewmodel.SalesViewModel
import kotlinx.coroutines.launch

/**
 * Pantalla de gestión de órdenes para administradores
 */
@Composable
fun AdminOrdersScreen(
    navController: NavController,
    salesViewModel: SalesViewModel = viewModel()
) {
    var searchQuery by remember { mutableStateOf("") }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var orderToDelete by remember { mutableStateOf<Order?>(null) }
    var isDeleting by remember { mutableStateOf(false) }
    
    val orderRepository = remember { OrderRepository() }
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    
    // Obtener órdenes del viewmodel
    val orders by salesViewModel.orders.collectAsState()
    
    // Cargar órdenes al iniciar
    LaunchedEffect(Unit) {
        salesViewModel.loadOrders()
    }
    
    // Filtrar órdenes por nombre de cliente
    val filteredOrders = remember(searchQuery, orders) {
        if (searchQuery.isEmpty()) {
            orders
        } else {
            orders.filter { order ->
                order.customerName.contains(searchQuery, ignoreCase = true) ||
                order.orderNumber.contains(searchQuery, ignoreCase = true)
            }
        }
    }
    
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Gestión de Órdenes",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground,
                        maxLines = 2
                    )
                    Text(
                        text = "${filteredOrders.size} órdenes",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Buscar por cliente o número de orden...") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Buscar")
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Limpiar")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Orders List
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredOrders) { order ->
                    OrderManagementCard(
                        order = order,
                        onViewDetails = {
                            navController.navigate("orderDetail/${order.id}")
                        },
                        onDeleteClick = {
                            orderToDelete = order
                            showDeleteDialog = true
                        }
                    )
                }
                
                if (filteredOrders.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (searchQuery.isEmpty()) "No hay órdenes" else "No se encontraron órdenes",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            }
        }
        
        // Modal de confirmación de eliminación
        ConfirmationDialog(
            showDialog = showDeleteDialog,
            onDismiss = {
                showDeleteDialog = false
                orderToDelete = null
            },
            onConfirm = {
                orderToDelete?.let { order ->
                    isDeleting = true
                    coroutineScope.launch {
                        orderRepository.deleteOrder(order.id, "").collect { resource ->
                            when (resource) {
                                is Resource.Loading -> {
                                    // Mantener isDeleting = true
                                }
                                is Resource.Success -> {
                                    isDeleting = false
                                    showDeleteDialog = false
                                    snackbarHostState.showSnackbar(
                                        message = "Orden ${order.orderNumber} eliminada exitosamente",
                                        duration = SnackbarDuration.Short
                                    )
                                    // Recargar órdenes
                                    salesViewModel.loadOrders()
                                    orderToDelete = null
                                }
                                is Resource.Error -> {
                                    isDeleting = false
                                    showDeleteDialog = false
                                    snackbarHostState.showSnackbar(
                                        message = resource.message ?: "Error al eliminar orden",
                                        duration = SnackbarDuration.Long
                                    )
                                    orderToDelete = null
                                }
                            }
                        }
                    }
                }
            },
            title = "Eliminar Orden",
            message = "¿Estás seguro de que deseas eliminar la orden ${orderToDelete?.orderNumber}? Esta acción no se puede deshacer.",
            confirmButtonText = if (isDeleting) "Eliminando..." else "Sí, eliminar",
            dismissButtonText = "Cancelar"
        )
    }
}

@Composable
fun OrderManagementCard(
    order: Order,
    onViewDetails: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Order Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = order.orderNumber,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                    Text(
                        text = order.customerName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Fecha
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                        Text(
                            text = order.createdDate.take(10), // Solo la fecha
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                    }
                    
                    // Monto
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            Icons.Default.ShoppingCart,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Color(0xFF4CAF50)
                        )
                        Text(
                            text = "$${order.montoTotal}",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFF4CAF50)
                        )
                    }
                }
            }
            
            // Action Buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Ver detalles (Ojo azul)
                IconButton(
                    onClick = onViewDetails,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color(0xFF2196F3).copy(alpha = 0.2f)
                    )
                ) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = "Ver detalles",
                        tint = Color(0xFF2196F3)
                    )
                }
                
                // Eliminar (Basurero rojo)
                IconButton(
                    onClick = onDeleteClick,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color.Red.copy(alpha = 0.1f)
                    )
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = Color.Red
                    )
                }
            }
        }
    }
}
