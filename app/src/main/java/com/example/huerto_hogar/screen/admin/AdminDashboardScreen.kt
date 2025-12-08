package com.example.huerto_hogar.screen.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.huerto_hogar.manager.UserManagerViewModel
import com.example.huerto_hogar.ui.theme.components.animations.pressClickEffect
import com.example.huerto_hogar.ui.theme.components.animations.pressClickEffectWithInteraction
import com.example.huerto_hogar.viewmodel.ProductViewModel
import com.example.huerto_hogar.viewmodel.SalesViewModel
import androidx.compose.runtime.LaunchedEffect

/**
 * Dashboard administrativo con estadísticas de la tienda (datos dinámicos desde el backend).
 */
@Composable
fun AdminDashboardScreen(
    navController: NavController,
    userManager: UserManagerViewModel = viewModel(),
    productViewModel: ProductViewModel = viewModel(),
    salesViewModel: SalesViewModel = viewModel()
) {
    val scrollState = rememberScrollState()
    
    // Obtener datos dinámicos del backend
    val userList by userManager.userList.collectAsState()
    val products by productViewModel.products.collectAsState()
    val totalProducts = products.size
    val clientsCount = userList.count { it.role == "cliente" }
    
    // Obtener estadísticas de órdenes
    val orderStats by salesViewModel.orderStats.collectAsState()
    val orders by salesViewModel.orders.collectAsState()
    val dailySales = orderStats.todaySales
    val totalOrders = orderStats.totalOrders
    
    // Cargar datos al iniciar
    LaunchedEffect(Unit) {
        productViewModel.getAllProducts()
        salesViewModel.loadOrders()
        // Cargar usuarios desde backend (sin token ya que el endpoint es público por ahora)
        // En producción, deberías pasar el token del admin aquí
        userManager.loadUsersFromBackend()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Text(
            text = "Panel de Administración",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary,
            maxLines = 2
        )
        
        Text(
            text = "Estadísticas de la Tienda",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Stats Grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                title = "Ventas Hoy",
                value = "$${dailySales.toInt()}",
                icon = Icons.Default.ShoppingCart,
                color = Color(0xFF4CAF50),
                modifier = Modifier.weight(1f)
            )
            
            StatCard(
                title = "Productos",
                value = totalProducts.toString(),
                icon = Icons.AutoMirrored.Filled.List,
                color = Color(0xFF2196F3),
                modifier = Modifier.weight(1f)
            )
        }
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                title = "Clientes",
                value = clientsCount.toString(),
                icon = Icons.Default.Person,
                color = Color(0xFFFF9800),
                modifier = Modifier.weight(1f)
            )
            
            StatCard(
                title = "Órdenes",
                value = totalOrders.toString(),
                icon = Icons.Default.Star,
                color = Color(0xFF9C27B0),
                modifier = Modifier.weight(1f)
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Recent Activity Section
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
                Text(
                    text = "Actividad Reciente",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                
                // Mostrar últimas 3 órdenes
                val recentOrders = orders.take(3)
                if (recentOrders.isNotEmpty()) {
                    recentOrders.forEachIndexed { index, order ->
                        ActivityItem(
                            title = "Pedido ${order.orderNumber}",
                            subtitle = "Cliente: ${order.customerName}",
                            time = order.createdDate
                        )
                        if (index < recentOrders.size - 1) {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        }
                    }
                } else {
                    // Mostrar productos con stock bajo
                    val lowStockProducts = products.filter { it.stock < 20 }.take(3)
                    if (lowStockProducts.isNotEmpty()) {
                        lowStockProducts.forEachIndexed { index, product ->
                            ActivityItem(
                                title = "Stock bajo: ${product.name}",
                                subtitle = "Stock actual: ${product.stock} unidades",
                                time = "Reponer pronto"
                            )
                            if (index < lowStockProducts.size - 1) {
                                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                            }
                        }
                    } else {
                        ActivityItem(
                            title = "Sin actividad reciente",
                            subtitle = "No hay pedidos pendientes",
                            time = "---"
                        )
                    }
                }
            }
        }
        
        // Quick Actions
        Text(
            text = "Acciones Rápidas",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(top = 8.dp)
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickActionCard(
                title = "Agregar Producto",
                icon = Icons.Default.Add,
                modifier = Modifier.weight(1f),
                onClick = { navController.navigate("createProduct") }
            )
            
            QuickActionCard(
                title = "Crear Usuario",
                icon = Icons.Default.Person,
                modifier = Modifier.weight(1f),
                onClick = { navController.navigate("createUser") }
            )
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(120.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(32.dp)
            )
            
            Column {
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = color
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
fun ActivityItem(
    title: String,
    subtitle: String,
    time: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        }
        
        Text(
            text = time,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun QuickActionCard(
    title: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .height(80.dp)
            .pressClickEffect(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = onClick)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )
            
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}
