package com.example.huerto_hogar.ui.theme.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.example.huerto_hogar.R
import com.example.huerto_hogar.manager.UserManagerViewModel
import com.example.huerto_hogar.model.Role
import com.example.huerto_hogar.screen.BlogScreen
import com.example.huerto_hogar.screen.CartScreen
import com.example.huerto_hogar.screen.FavScreen
import com.example.huerto_hogar.screen.FrutasScreen
import com.example.huerto_hogar.screen.HomeScreen
import com.example.huerto_hogar.screen.LoginScreen
import com.example.huerto_hogar.screen.OrganicosScreen
import com.example.huerto_hogar.screen.RegistroScreen
import com.example.huerto_hogar.screen.UsSetScreen
import com.example.huerto_hogar.screen.VerdurasScreen
import com.example.huerto_hogar.viewmodel.LoginViewModel
import com.example.huerto_hogar.viewmodel.RegisterUserViewModel
import com.example.huerto_hogar.viewmodel.UserSettingsViewModel
import com.example.huerto_hogar.viewmodel.CartViewModel
import com.example.huerto_hogar.viewmodel.SalesViewModel
import com.example.huerto_hogar.ui.theme.components.animations.*
import com.example.huerto_hogar.ui.theme.components.admin.AdminNavigationContainer
import kotlinx.coroutines.launch


@Suppress("ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE")
@Composable
fun AppNavigationContainer() {
    val userManager: UserManagerViewModel = viewModel()
    val cartViewModel: CartViewModel = viewModel()
    val salesViewModel: SalesViewModel = viewModel()

    val currentUser by userManager.currentUser.collectAsState()
    
    // Si el usuario es admin, mostrar panel de administración
    if (currentUser?.role == Role.ADMIN) {
        AdminNavigationContainer(
            userManager = userManager,
            salesViewModel = salesViewModel,
            onLogout = {
                userManager.setCurrentUser(null)
            }
        )
        return
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val showBarraCatalogo = remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val navController = rememberNavController()

    val startDestination = AppScreens.HomeScreen.route

    // El famoso sidebar
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                //Logo cabecero//
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_huerto),
                        contentDescription = "Logo Huerto Hogar",
                        modifier = Modifier
                            .size(160.dp)
                            .padding(bottom = 2.dp),
                        contentScale = ContentScale.Fit
                    )

                    currentUser?.let { user ->
                        Text(
                            text = "Bienvenido ${user.name} ${user.lastname}",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                Spacer(modifier = Modifier.padding(vertical = 4.dp))

                if (currentUser == null) {
                    NavigationDrawerItem(
                        label = { Text("Iniciar Sesión") },
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
                        label = { Text("Registrarse") },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(AppScreens.RegistroScreen.route)
                        },
                        icon = {
                            Icon(
                                Icons.Default.Create,
                                contentDescription = "Registrarse"
                            )
                        }
                    )
                } else {
                    NavigationDrawerItem(
                        label = { Text("Configuración") },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(AppScreens.UsSetScreen.route)
                        },
                        icon = {
                            Icon(
                                Icons.Default.Settings,
                                contentDescription = "Configuración"
                            )
                        })

                    NavigationDrawerItem(
                        label = { Text("Cerrar Sesión") },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            showLogoutDialog = true
                        },
                        icon = {
                            Icon(
                                Icons.Default.AccountCircle,
                                contentDescription = "Cerrar Sesión"
                            )
                        }
                    )
                }
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

            }
        },
        gesturesEnabled = drawerState.isOpen,
    ) {
        // Diálogo de confirmación de logout
        LogoutConfirmationDialog(
            showDialog = showLogoutDialog,
            onDismiss = { showLogoutDialog = false },
            onConfirm = {
                showLogoutDialog = false
                userManager.setCurrentUser(null)
                navController.navigate(AppScreens.HomeScreen.route) {
                    popUpTo(AppScreens.HomeScreen.route) { inclusive = true }
                }
            }
        )
        
        Scaffold(
            // Menu de navegacion inferioor
            bottomBar = {
                Column {
                    if (showBarraCatalogo.value) {
                        CatalogoNavigation(
                            navController = navController,
                            onCloseMenu = { showBarraCatalogo.value = false })
                    }
                    MainBottomBar(navController = navController, onMenuClick = {
                        scope.launch { drawerState.open() }
                    }, onCatalogoClick = {
                        showBarraCatalogo.value = !showBarraCatalogo.value
                    })
                }
            }) { contentPadding ->
            // La logica del router/enrutamiento
            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier.padding(contentPadding)
            ) {
                composable(
                    route = AppScreens.HomeScreen.route,
                    enterTransition = { fadeIn() },
                    exitTransition = { fadeOut() }
                ) { 
                    HomeScreen(navController = navController) 
                }
                
                composable(
                    route = AppScreens.LoginScreen.route,
                    enterTransition = { scaleInWithFade() },
                    exitTransition = { scaleOutWithFade() }
                ) {
                    val loginVM: LoginViewModel = viewModel()
                    loginVM.userManager = userManager
                    LoginScreen(navController = navController, loginVM)
                }
                
                composable(
                    route = AppScreens.RegistroScreen.route,
                    enterTransition = { slideInFromBottomWithFade() },
                    exitTransition = { slideOutToBottomWithFade() }
                ) {
                    val registerVM: RegisterUserViewModel = viewModel()
                    registerVM.userManager = userManager
                    RegistroScreen(navController, registerVM)
                }
                
                composable(
                    route = AppScreens.FavScreen.route,
                    enterTransition = { slideInFromRightWithFade() },
                    exitTransition = { slideOutToLeftWithFade() }
                ) { 
                    FavScreen(navController = navController) 
                }
                
                composable(
                    route = AppScreens.CartScreen.route,
                    enterTransition = { slideInFromRightWithFade() },
                    exitTransition = { slideOutToLeftWithFade() }
                ) { 
                    CartScreen(
                        cartViewModel = cartViewModel,
                        salesViewModel = salesViewModel
                    )
                }
                
                composable(
                    route = AppScreens.UsSetScreen.route,
                    enterTransition = { slideInFromBottomWithFade() },
                    exitTransition = { slideOutToBottomWithFade() }
                ) {
                    val settingsVM: UserSettingsViewModel = viewModel()
                    settingsVM.userManager = userManager
                    UsSetScreen(navController = navController, viewModel = settingsVM)
                }
                
                composable(
                    route = AppScreens.BlogScreen.route,
                    enterTransition = { fadeIn() },
                    exitTransition = { fadeOut() }
                ) { 
                    BlogScreen(navController = navController) 
                }
                
                composable(
                    route = AppScreens.FrutasScreen.route,
                    enterTransition = { fadeIn() },
                    exitTransition = { fadeOut() }
                ) { 
                    FrutasScreen(
                        navController = navController,
                        cartViewModel = cartViewModel
                    ) 
                }
                
                composable(
                    route = AppScreens.OrganicosScreen.route,
                    enterTransition = { fadeIn() },
                    exitTransition = { fadeOut() }
                ) { 
                    OrganicosScreen(
                        navController = navController,
                        cartViewModel = cartViewModel
                    ) 
                }
                
                composable(
                    route = AppScreens.VerdurasScreen.route,
                    enterTransition = { fadeIn() },
                    exitTransition = { fadeOut() }
                ) { 
                    VerdurasScreen(
                        navController = navController,
                        cartViewModel = cartViewModel
                    ) 
                }
            }
        }
    }
}