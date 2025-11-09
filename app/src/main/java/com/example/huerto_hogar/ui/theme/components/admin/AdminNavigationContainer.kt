package com.example.huerto_hogar.ui.theme.components.admin

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.huerto_hogar.AppScreens.AppScreens
import com.example.huerto_hogar.manager.UserManagerViewModel
import com.example.huerto_hogar.screen.admin.AdminDashboardScreen
import com.example.huerto_hogar.screen.admin.AdminInventoryScreen
import com.example.huerto_hogar.screen.admin.AdminUsersScreen
import com.example.huerto_hogar.ui.theme.components.animations.*

/**
 * Contenedor de navegación para el panel administrativo.
 * Incluye rutas exclusivas para administradores.
 */
@Composable
fun AdminNavigationContainer(
    userManager: UserManagerViewModel,
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
                    userManager = userManager
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
        }
    }
}
