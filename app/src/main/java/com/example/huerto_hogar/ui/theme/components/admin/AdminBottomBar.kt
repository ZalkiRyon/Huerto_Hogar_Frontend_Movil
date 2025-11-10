package com.example.huerto_hogar.ui.theme.components.admin

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.huerto_hogar.AppScreens.AppScreens
import com.example.huerto_hogar.ui.theme.components.ConfirmationDialog

/**
 * Navegación inferior para el panel de administración.
 */

sealed class AdminBottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    object Dashboard : AdminBottomNavItem(
        AppScreens.AdminDashboardScreen.route,
        Icons.Default.Home,
        "Dashboard"
    )
    
    object Inventory : AdminBottomNavItem(
        AppScreens.AdminInventoryScreen.route,
        Icons.Default.List,
        "Inventario"
    )
    
    object Users : AdminBottomNavItem(
        AppScreens.AdminUsersScreen.route,
        Icons.Default.Person,
        "Usuarios"
    )
    
    object Logout : AdminBottomNavItem(
        "logout",
        Icons.Default.ExitToApp,
        "Salir"
    )
}

@Composable
fun AdminBottomBar(
    navController: NavHostController,
    onLogout: () -> Unit
) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    var showLogoutDialog by remember { mutableStateOf(false) }
    
    // Diálogo de confirmación de logout usando componente reutilizable
    ConfirmationDialog(
        showDialog = showLogoutDialog,
        onDismiss = { showLogoutDialog = false },
        onConfirm = {
            showLogoutDialog = false
            onLogout()
        },
        message = "¿Estás seguro de que deseas cerrar sesión del panel de administración?"
    )
    
    BottomAppBar(
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val items = listOf(
                    AdminBottomNavItem.Dashboard,
                    AdminBottomNavItem.Inventory,
                    AdminBottomNavItem.Users,
                    AdminBottomNavItem.Logout
                )
                
                items.forEach { item ->
                    val isSelected = currentRoute == item.route
                    val scale by animateFloatAsState(
                        targetValue = if (isSelected) 1.15f else 1f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMedium
                        ),
                        label = "admin_icon_scale_${item.label}"
                    )
                    
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            if (item == AdminBottomNavItem.Logout) {
                                showLogoutDialog = true
                            } else {
                                navController.navigate(item.route) {
                                    popUpTo(AppScreens.AdminDashboardScreen.route) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label,
                                modifier = Modifier.scale(scale)
                            )
                        },
                        label = { Text(item.label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }
    )
}
