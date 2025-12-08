package com.example.huerto_hogar.ui.theme.components.admin

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
import com.example.huerto_hogar.ui.theme.components.animations.*
import androidx.navigation.NavType
import androidx.navigation.navArgument

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
    
    Scaffold(
        bottomBar = {
            AdminBottomBar(
                navController = navController,
                onLogout = onLogout
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
