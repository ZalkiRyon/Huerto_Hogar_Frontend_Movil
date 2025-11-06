package com.example.huerto_hogar.ui.theme.components

import com.example.huerto_hogar.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.huerto_hogar.AppScreens.AppScreens
import com.example.huerto_hogar.manager.UserManagerViewModel
import com.example.huerto_hogar.screen.BlogScreen
import com.example.huerto_hogar.screen.CartScreen
import com.example.huerto_hogar.screen.FavScreen
import com.example.huerto_hogar.screen.FrutasScreen
import com.example.huerto_hogar.screen.HomeScreen
import com.example.huerto_hogar.screen.LoginScreen
import com.example.huerto_hogar.screen.OrganicosScreen
import com.example.huerto_hogar.screen.RegistroScreen
import com.example.huerto_hogar.screen.UsSettScreen
import com.example.huerto_hogar.screen.VerdurasScreen
import com.example.huerto_hogar.viewmodel.RegisterUserViewModel
import kotlinx.coroutines.launch


@Composable
fun AppNavigationContainer() {
    val userManager: UserManagerViewModel = viewModel()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val showBarraCatalogo = remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val navController = rememberNavController()

    val startDestination = AppScreens.HomeScreen.route

    // El famoso sidebar
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                //Logo cabecero//
                Column (modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Image(
                        painter = painterResource(id= R.drawable.logo_huerto),
                        contentDescription = "Logo Huerto Hogar",
                        modifier = Modifier.size(160.dp).padding(bottom = 2.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                Spacer(modifier = Modifier.padding(vertical = 4.dp))

                NavigationDrawerItem(
                    label = {Text("Iniciar Sesión")},
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(AppScreens.LoginScreen.route)
                    },
                    icon = {
                        Icon(
                            Icons.Default.AccountCircle,
                            contentDescription = "Iniciar Sesión"
                        )
                    }
                )

                NavigationDrawerItem(
                    label = {Text("Registrarse")},
                    selected = false,
                    onClick = {
                        scope.launch {drawerState.close()}
                        navController.navigate(AppScreens.RegistroScreen.route)
                    },
                    icon ={
                        Icon(
                            Icons.Default.Create,
                            contentDescription = "Registrarse"
                        )
                    }
                )

//                NavigationDrawerItem(
//                    label = { Text("Inicio") },
//                    selected = false,
//                    onClick = {
//                        scope.launch { drawerState.close() }
//                        navController.navigate(AppScreens.HomeScreen.route)
//                    },
//                    icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") }
//                )
//                NavigationDrawerItem(
//                    label = { Text("Catálogo") },
//                    selected = false,
//                    onClick = {
//                        scope.launch { drawerState.close() }
//                        navController.navigate(AppScreens.CatalogScreen.route)
//                    },
//                    icon = { Icon(Icons.Default.Search, contentDescription = "Catálogo") }
//                )
//                NavigationDrawerItem(
//                    label = { Text("Carrito") },
//                    selected = false,
//                    onClick = {
//                        scope.launch { drawerState.close() }
//                        navController.navigate(AppScreens.CartScreen.route)
//                    },
//                    icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito") }
//                )
//                NavigationDrawerItem(
//                    label = { Text("Favoritos") },
//                    selected = false,
//                    onClick = {
//                        scope.launch { drawerState.close() }
//                        navController.navigate(AppScreens.FavScreen.route)
//                    },
//                    icon = { Icon(Icons.Default.Favorite, contentDescription = "Favoritos") }
//                )
//
                Spacer(modifier = Modifier.weight(1f))
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

            }
        },
        gesturesEnabled = drawerState.isOpen,
    ) {
        Scaffold(
            // Menu de navegacion inferioor
            bottomBar = {
                Column {
                    if (showBarraCatalogo.value) {
                        CatalogoNavigation(
                            navController = navController,
                            onCloseMenu = { showBarraCatalogo.value = false }
                        )
                    }
                    MainBottomBar(
                        navController = navController,
                        onMenuClick = {
                            scope.launch { drawerState.open() }
                        },
                        onCatalogoClick = {
                            showBarraCatalogo.value = !showBarraCatalogo.value
                        }
                    )
                }
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
                composable(route = AppScreens.RegistroScreen.route) {
                    val registerVM: RegisterUserViewModel = viewModel()
                    registerVM.userManager = userManager
                    RegistroScreen(navController,registerVM ) }
                composable(route = AppScreens.FavScreen.route) { FavScreen(navController = navController) }
                composable(route = AppScreens.CartScreen.route) { CartScreen(navController = navController) }
                composable(route = AppScreens.UsSettScreen.route) { UsSettScreen(navController = navController) }
                composable(route = AppScreens.blogScreen.route) { BlogScreen(navController = navController) }
                composable(route = AppScreens.FrutasScreen.route) { FrutasScreen(navController = navController) }
                composable(route = AppScreens.OrganicosScreen.route) { OrganicosScreen(navController = navController) }
                composable(route = AppScreens.VerdurasScreen.route) { VerdurasScreen(navController = navController) }
            }
        }
    }
}