package com.example.huerto_hogar.ui.theme.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.huerto_hogar.AppScreens.AppScreens
import com.example.huerto_hogar.screen.BlogScreen
import com.example.huerto_hogar.screen.CartScreen
import com.example.huerto_hogar.screen.CatalogScreen
import com.example.huerto_hogar.screen.FavScreen
import com.example.huerto_hogar.screen.HomeScreen
import com.example.huerto_hogar.screen.LoginScreen
import com.example.huerto_hogar.screen.RegistroScreen
import com.example.huerto_hogar.screen.UsSettScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigationContainer() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()

    val startDestination = AppScreens.HomeScreen.route

    // El famoso sidebar
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    "Holaaaaaaaaaaa Mundo",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleLarge
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                Spacer(modifier = Modifier.padding(vertical = 4.dp))
                NavigationDrawerItem(
                    label = { Text("Inicio") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(AppScreens.HomeScreen.route)
                    },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") }
                )
                NavigationDrawerItem(
                    label = { Text("Catálogo") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(AppScreens.CatalogScreen.route)
                    },
                    icon = { Icon(Icons.Default.Search, contentDescription = "Catálogo") }
                )
                NavigationDrawerItem(
                    label = { Text("Carrito") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(AppScreens.CartScreen.route)
                    },
                    icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito") }
                )
                NavigationDrawerItem(
                    label = { Text("Favoritos") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(AppScreens.FavScreen.route)
                    },
                    icon = { Icon(Icons.Default.Favorite, contentDescription = "Favoritos") }
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                NavigationDrawerItem(
                    label = { Text("Configuración") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(AppScreens.UsSettScreen.route)
                    },
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Configuración") }
                )
                // Todo: poner aqui para el cierrre de sesión
            }
        },
        gesturesEnabled = drawerState.isOpen
    ) {
        Scaffold(
            // Menu de navegacion inferioor
            bottomBar = {
                MainBottomBar(
                    navController = navController,
                    onMenuClick = {
                        scope.launch { drawerState.open() }
                    }
                )
            }
        ) { contentPadding ->
            // La logica del router/enrutamiento
            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier.padding(contentPadding)
            ) {
                composable(route = AppScreens.HomeScreen.route) { HomeScreen(navController = navController) }
                composable(route = AppScreens.LoginScreen.route) { LoginScreen(navController = navController) }
                composable(route = AppScreens.RegistroScreen.route) { RegistroScreen(navController = navController) }
                composable(route = AppScreens.FavScreen.route) { FavScreen(navController = navController) }
                composable(route = AppScreens.CartScreen.route) { CartScreen(navController = navController) }
                composable(route = AppScreens.CatalogScreen.route) { CatalogScreen(navController = navController) }
                composable(route = AppScreens.UsSettScreen.route) { UsSettScreen(navController = navController) }
                composable(route = AppScreens.blogScreen.route) { BlogScreen(navController = navController) }
            }
        }
    }
}