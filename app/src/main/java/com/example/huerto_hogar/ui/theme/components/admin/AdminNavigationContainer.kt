package com.example.huerto_hogar.ui.theme.components.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.huerto_hogar.AppScreens.AppScreens
import com.example.huerto_hogar.manager.UserManagerViewModel
import com.example.huerto_hogar.viewmodel.SalesViewModel
import com.example.huerto_hogar.screen.admin.AdminDashboardScreen
import com.example.huerto_hogar.screen.admin.AdminInventoryScreen
import com.example.huerto_hogar.screen.admin.AdminUsersScreen
import com.example.huerto_hogar.screen.admin.AdminOrdersScreen
import com.example.huerto_hogar.screen.admin.CreateProductScreen
import com.example.huerto_hogar.screen.admin.CreateUserScreen
import com.example.huerto_hogar.screen.admin.EditUserScreen
import com.example.huerto_hogar.screen.admin.OrderDetailScreen
import com.example.huerto_hogar.ui.theme.components.ConfirmationDialog
import com.example.huerto_hogar.ui.theme.components.animations.*
import androidx.navigation.NavType
import androidx.navigation.navArgument
import kotlinx.coroutines.launch

/**
 * Contenedor de navegación para el panel administrativo.
 * Incluye rutas exclusivas para administradores.
 */
@Composable
fun AdminNavigationContainer(
    userManager: UserManagerViewModel,
    salesViewModel: SalesViewModel,
    onLogout: () -> Unit
) {
    val navController = rememberNavController()
    val startDestination = AppScreens.AdminDashboardScreen.route
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var showLogoutDialog by remember { mutableStateOf(false) }
    
    val currentUser = userManager.currentUser.value
    
    // Drawer desde la derecha (RTL)
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            gesturesEnabled = drawerState.isOpen,
            drawerContent = {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    ModalDrawerSheet(
                        drawerContainerColor = MaterialTheme.colorScheme.surface,
                        modifier = Modifier.fillMaxWidth(0.75f)
                    ) {
                        // Cabecera con foto de perfil
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            currentUser?.let { user ->
                                val profileUrl = user.profilePictureUrl
                                Box(
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clip(CircleShape)
                                        .border(
                                            2.dp,
                                            MaterialTheme.colorScheme.primary,
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (profileUrl != null) {
                                        Image(
                                            painter = rememberAsyncImagePainter(model = profileUrl),
                                            contentDescription = "Foto de perfil de ${user.name}",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    } else {
                                        Icon(
                                            Icons.Default.AccountCircle,
                                            contentDescription = "Sin foto de perfil",
                                            modifier = Modifier.size(70.dp),
                                            tint = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "Bienvenido ${user.name} ${user.lastname}",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = "Administrador",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                        
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        
                        // Opciones del menú de administrador
                        NavigationDrawerItem(
                            label = { Text("Tienda") },
                            selected = false,
                            onClick = {
                                scope.launch { drawerState.close() }
                                userManager.toggleAdminView(true) // Cambiar a vista tienda
                            },
                            icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Tienda") }
                        )
                        
                        NavigationDrawerItem(
                            label = { Text("Reactivar Usuario") },
                            selected = false,
                            onClick = {
                                scope.launch { drawerState.close() }
                                // TODO: Implementar pantalla de reactivar usuario
                            },
                            icon = { Icon(Icons.Default.Refresh, contentDescription = "Reactivar Usuario") }
                        )
                        
                        NavigationDrawerItem(
                            label = { Text("Reactivar Producto") },
                            selected = false,
                            onClick = {
                                scope.launch { drawerState.close() }
                                // TODO: Implementar pantalla de reactivar producto
                            },
                            icon = { Icon(Icons.Default.Refresh, contentDescription = "Reactivar Producto") }
                        )
                        
                        Spacer(modifier = Modifier.weight(1f))
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        
                        // Configuración y Cerrar Sesión
                        NavigationDrawerItem(
                            label = { Text("Configuración") },
                            selected = false,
                            onClick = {
                                scope.launch { drawerState.close() }
                                // TODO: Navegar a configuración
                            },
                            icon = { Icon(Icons.Default.Settings, contentDescription = "Configuración") }
                        )
                        
                        NavigationDrawerItem(
                            label = { Text("Cerrar Sesión") },
                            selected = false,
                            onClick = {
                                scope.launch { drawerState.close() }
                                showLogoutDialog = true
                            },
                            icon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Cerrar Sesión") }
                        )
                    }
                }
            }
        ) {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                // Diálogo de confirmación de logout
                ConfirmationDialog(
                    showDialog = showLogoutDialog,
                    onDismiss = { showLogoutDialog = false },
                    onConfirm = {
                        showLogoutDialog = false
                        onLogout()
                    },
                    message = "¿Estás seguro de que deseas cerrar sesión del panel de administración?"
                )
                
                Scaffold(
                    bottomBar = {
                        AdminBottomBar(
                            navController = navController,
                            onMenuClick = {
                                scope.launch { drawerState.open() }
                            }
                        )
                    }
                ) { contentPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(contentPadding)
        ) {
            composable(
                route = AppScreens.AdminDashboardScreen.route,
                enterTransition = { fadeIn() },
                exitTransition = { fadeOut() }
            ) {
                AdminDashboardScreen(
                    navController = navController,
                    userManager = userManager,
                    salesViewModel = salesViewModel
                )
            }
            
            composable(
                route = AppScreens.AdminInventoryScreen.route,
                enterTransition = { slideInFromRightWithFade() },
                exitTransition = { slideOutToLeftWithFade() }
            ) {
                AdminInventoryScreen(navController = navController)
            }
            
            composable(
                route = AppScreens.AdminUsersScreen.route,
                enterTransition = { slideInFromRightWithFade() },
                exitTransition = { slideOutToLeftWithFade() }
            ) {
                AdminUsersScreen(
                    navController = navController,
                    userManager = userManager
                )
            }
            
            // Ruta para crear producto
            composable(
                route = "createProduct",
                enterTransition = { slideInFromRightWithFade() },
                exitTransition = { slideOutToLeftWithFade() }
            ) {
                CreateProductScreen(navController = navController)
            }
            
            // Ruta para crear usuario
            composable(
                route = "createUser",
                enterTransition = { slideInFromRightWithFade() },
                exitTransition = { slideOutToLeftWithFade() }
            ) {
                CreateUserScreen(navController = navController)
            }
            
            // Ruta para editar usuario
            composable(
                route = "editUser/{userId}",
                arguments = listOf(navArgument("userId") { type = NavType.IntType }),
                enterTransition = { slideInFromRightWithFade() },
                exitTransition = { slideOutToLeftWithFade() }
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getInt("userId") ?: 0
                EditUserScreen(
                    navController = navController,
                    userId = userId,
                    userManager = userManager
                )
            }
            
            // Ruta para gestión de órdenes
            composable(
                route = AppScreens.AdminOrdersScreen.route,
                enterTransition = { slideInFromRightWithFade() },
                exitTransition = { slideOutToLeftWithFade() }
            ) {
                AdminOrdersScreen(
                    navController = navController,
                    salesViewModel = salesViewModel
                )
            }
            
            // Ruta para detalle de orden
            composable(
                route = "orderDetail/{orderId}",
                arguments = listOf(navArgument("orderId") { type = NavType.IntType }),
                enterTransition = { slideInFromRightWithFade() },
                exitTransition = { slideOutToLeftWithFade() }
            ) { backStackEntry ->
                val orderId = backStackEntry.arguments?.getInt("orderId") ?: 0
                OrderDetailScreen(
                    navController = navController,
                    orderId = orderId,
                    salesViewModel = salesViewModel
                )
            }
        }
                }
            }
        }
    }
}
