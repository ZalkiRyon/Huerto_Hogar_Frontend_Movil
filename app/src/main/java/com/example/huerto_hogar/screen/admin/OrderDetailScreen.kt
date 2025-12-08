package com.example.huerto_hogar.screen.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.huerto_hogar.viewmodel.SalesViewModel

/**
 * Pantalla de detalle de una orden específica
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(
    navController: NavController,
    orderId: Int,
    salesViewModel: SalesViewModel = viewModel()
) {
    val orders by salesViewModel.orders.collectAsState()
    val order = remember(orders, orderId) {
        orders.find { it.id == orderId }
    }
    
    val scrollState = rememberScrollState()
    
    // Si no se encuentra la orden
    if (order == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.error
                )
                Text(
                    text = "Orden no encontrada",
                    style = MaterialTheme.typography.titleLarge
                )
                Button(onClick = { navController.navigateUp() }) {
                    Text("Volver")
                }
            }
        }
        return
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Orden") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Información General
            DetailSection(
                title = "Información General",
                icon = Icons.Default.Info
            ) {
                DetailRow("Número de Orden", order.orderNumber)
                DetailRow("Fecha", order.createdDate)
                DetailRow("Estado", order.statusName)
            }
            
            // Información del Cliente
            DetailSection(
                title = "Cliente",
                icon = Icons.Default.Person
            ) {
                DetailRow("Nombre", order.customerName)
                DetailRow("Email", order.customerEmail)
                DetailRow("Teléfono", order.phone)
            }
            
            // Dirección de Envío
            DetailSection(
                title = "Dirección de Envío",
                icon = Icons.Default.LocationOn
            ) {
                DetailRow("Dirección", order.address)
                DetailRow("Región", order.region)
                DetailRow("Comuna", order.comuna)
            }
            
            // Detalles Financieros
            DetailSection(
                title = "Detalles Financieros",
                icon = Icons.Default.ShoppingCart
            ) {
                DetailRow("Monto Total", "$${order.totalProducts}", isHighlight = true)
                DetailRow("Costo de Envío", "$${order.shippingCost}")
                val total = order.totalProducts + order.shippingCost
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                DetailRow("Total Final", "$$total", isHighlight = true, isBold = true)
            }
            
            // Comentarios (si existen)
            if (!order.comments.isNullOrBlank()) {
                DetailSection(
                    title = "Comentarios",
                    icon = Icons.Default.Comment
                ) {
                    Text(
                        text = order.comments,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
fun DetailSection(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            
            content()
        }
    }
}

@Composable
fun DetailRow(
    label: String,
    value: String,
    isHighlight: Boolean = false,
    isBold: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            style = if (isBold) {
                MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
            } else {
                MaterialTheme.typography.bodyMedium
            },
            color = if (isHighlight) {
                Color(0xFF4CAF50)
            } else {
                MaterialTheme.colorScheme.onSurface
            },
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
        )
    }
}
