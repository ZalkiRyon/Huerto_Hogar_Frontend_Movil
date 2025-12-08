package com.example.huerto_hogar.ui.theme.components

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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.ThumbUp
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
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.huerto_hogar.AppScreens.AppScreens
import com.example.huerto_hogar.R
import com.example.huerto_hogar.manager.UserManagerViewModel
import com.example.huerto_hogar.model.Role
import com.example.huerto_hogar.screen.AllProductsScreen
import com.example.huerto_hogar.screen.BlogScreen
import com.example.huerto_hogar.screen.CartScreen
import com.example.huerto_hogar.screen.CatalogoScreen
import com.example.huerto_hogar.screen.FavScreen
import com.example.huerto_hogar.screen.FrutasScreen
import com.example.huerto_hogar.screen.HomeScreen
import com.example.huerto_hogar.screen.LoginScreen
import com.example.huerto_hogar.screen.OrganicosScreen
import com.example.huerto_hogar.screen.RegistroScreen
import com.example.huerto_hogar.screen.UsSetScreen
import com.example.huerto_hogar.screen.VerdurasScreen
import com.example.huerto_hogar.ui.theme.components.admin.AdminNavigationContainer
import com.example.huerto_hogar.ui.theme.components.animations.fadeIn
import com.example.huerto_hogar.ui.theme.components.animations.fadeOut
import com.example.huerto_hogar.ui.theme.components.animations.scaleInWithFade
import com.example.huerto_hogar.ui.theme.components.animations.scaleOutWithFade
import com.example.huerto_hogar.ui.theme.components.animations.slideInFromBottomWithFade
import com.example.huerto_hogar.ui.theme.components.animations.slideInFromRightWithFade
import com.example.huerto_hogar.ui.theme.components.animations.slideOutToBottomWithFade
import com.example.huerto_hogar.ui.theme.components.animations.slideOutToLeftWithFade
import com.example.huerto_hogar.viewmodel.CartViewModel
import com.example.huerto_hogar.viewmodel.FavoritesViewModel
import com.example.huerto_hogar.viewmodel.LoginViewModel
import com.example.huerto_hogar.viewmodel.RegisterUserViewModel
import com.example.huerto_hogar.viewmodel.SalesViewModel
import com.example.huerto_hogar.viewmodel.UserSettingsViewModel
import kotlinx.coroutines.launch


@Suppress("ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE")
@Composable
fun AppNavigationContainer() {
    val userManager: UserManagerViewModel = viewModel()
    val cartViewModel: CartViewModel = viewModel()
    val favoritesViewModel: FavoritesViewModel = viewModel()
    val salesViewModel: SalesViewModel = viewModel()

    val currentUser by userManager.currentUser.collectAsState()
    val showAdminStoreView by userManager.showAdminStoreView.collectAsState()

    // Si el usuario es admin y NO está en modo tienda, mostrar panel de administración
    if (currentUser?.role == "admin" && !showAdminStoreView) {
        AdminNavigationContainer(
            userManager = userManager,
            salesViewModel = salesViewModel,
            onLogout = {
                cartViewModel.clearCart()
                favoritesViewModel.clearFavorites()
                userManager.setCurrentUser(null)
            }
        )
        return
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var showLogoutDialog by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val navController = rememberNavController()

    val startDestination = AppScreens.HomeScreen.route

    // El famoso sidebar - Cambiado a RTL para abrir desde la derecha
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
                        //Logo cabecero//
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (currentUser == null) {
                                Image(
                                    painter = painterResource(id = R.drawable.logo_huerto),
                                    contentDescription = "Logo Huerto Hogar",
                                    modifier = Modifier
                                        .size(160.dp)
                                        .padding(bottom = 2.dp),
                                    contentScale = ContentScale.Fit
                                )
                            } else {
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
                                }

                            }


                        }



                        if (currentUser == null) {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                            Spacer(modifier = Modifier.padding(vertical = 4.dp))
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
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                        // Navegación principal
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
                            label = { Text("Blogs") },
                            selected = false,
                            onClick = {
                                scope.launch { drawerState.close() }
                                navController.navigate(AppScreens.BlogScreen.route)
                            },
                            icon = { Icon(Icons.Default.ThumbUp, contentDescription = "Blogs") }
                        )
                        NavigationDrawerItem(
                            label = { Text("Catálogo") },
                            selected = false,
                            onClick = {
                                scope.launch { drawerState.close() }
                                navController.navigate(AppScreens.CatalogoScreen.route)
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
                            icon = {
                                Icon(
                                    Icons.Default.ShoppingCart,
                                    contentDescription = "Carrito"
                                )
                            }
                        )
                        NavigationDrawerItem(
                            label = { Text("Favoritos") },
                            selected = false,
                            onClick = {
                                scope.launch { drawerState.close() }
                                navController.navigate(AppScreens.FavScreen.route)
                            },
                            icon = {
                                Icon(
                                    Icons.Default.Favorite,
                                    contentDescription = "Favoritos"
                                )
                            }
                        )
                        
                        // Si el usuario es admin, mostrar acceso al Dashboard
                        if (currentUser?.role == "admin") {
                            NavigationDrawerItem(
                                label = { Text("Dashboard Administración") },
                                selected = false,
                                onClick = {
                                    scope.launch { drawerState.close() }
                                    userManager.toggleAdminView(false) // Cambiar a vista admin
                                },
                                icon = {
                                    Icon(
                                        Icons.Default.Home,
                                        contentDescription = "Dashboard Administración"
                                    )
                                }
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        if (currentUser != null) {
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
                    }
                }
            }
        ) {
            // Restaurar dirección LTR para el contenido
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                // Diálogo de confirmación de logout
                ConfirmationDialog(
                    showDialog = showLogoutDialog,
                    onDismiss = { showLogoutDialog = false },
                    onConfirm = {
                        showLogoutDialog = false
                        cartViewModel.clearCart()
                        favoritesViewModel.clearFavorites()
                        userManager.setCurrentUser(null)
                        navController.navigate(AppScreens.HomeScreen.route) {
                            popUpTo(AppScreens.HomeScreen.route) { inclusive = true }
                        }
                    }
                )

                Scaffold(
                    // Menu de navegacion inferior
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

                            FavScreen(
                                navController = navController,
                                favoritesViewModel = favoritesViewModel,
                                cartViewModel = cartViewModel,
                                user = currentUser
                            )
                        }

                        composable(
                            route = AppScreens.CartScreen.route,
                            enterTransition = { slideInFromRightWithFade() },
                            exitTransition = { slideOutToLeftWithFade() }
                        ) {
                            CartScreen(
                                navController = navController,
                                cartViewModel = cartViewModel,
                                salesViewModel = salesViewModel,
                                user = currentUser
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
                                cartViewModel = cartViewModel,
                                favoritesViewModel = favoritesViewModel
                            )
                        }

                        composable(
                            route = AppScreens.OrganicosScreen.route,
                            enterTransition = { fadeIn() },
                            exitTransition = { fadeOut() }
                        ) {
                            OrganicosScreen(
                                navController = navController,
                                cartViewModel = cartViewModel,
                                favoritesViewModel = favoritesViewModel
                            )
                        }

                        composable(
                            route = AppScreens.VerdurasScreen.route,
                            enterTransition = { fadeIn() },
                            exitTransition = { fadeOut() }
                        ) {
                            VerdurasScreen(
                                navController = navController,
                                cartViewModel = cartViewModel,
                                favoritesViewModel = favoritesViewModel
                            )
                        }

                        composable(
                            route = AppScreens.CatalogoScreen.route,
                            enterTransition = { fadeIn() },
                            exitTransition = { fadeOut() }
                        ) {
                            CatalogoScreen(navController = navController)
                        }

                        composable(
                            route = AppScreens.AllProductsScreen.route,
                            enterTransition = { fadeIn() },
                            exitTransition = { fadeOut() }
                        ) {
                            AllProductsScreen(
                                navController = navController,
                                cartViewModel = cartViewModel,
                                favoritesViewModel = favoritesViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}