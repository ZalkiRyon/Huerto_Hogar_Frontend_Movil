package com.example.huerto_hogar.ui.theme.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar as MaterialNavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.huerto_hogar.AppScreens.AppScreens
import com.example.huerto_hogar.screen.CartScreen
import com.example.huerto_hogar.screen.CatalogScreen
import com.example.huerto_hogar.screen.FavScreen
import com.example.huerto_hogar.screen.HomeScreen
import kotlinx.coroutines.launch

//Clase para definir las vistas de la barra
enum class BottomNavViews(
    val route: String?,
    val icon: ImageVector,
    val label: String,
    val contentDescription: String

) {
    MENU(null, Icons.Default.Menu, "Menú", "Menu"),
    FAVORITOS(AppScreens.FavScreen.route, Icons.Default.Favorite, "Favoritos", "Favoritos"),
    CARRITO(AppScreens.CartScreen.route, Icons.Default.ShoppingCart, "Carrito", "Carrito"),
    CATALOGO(AppScreens.CatalogScreen.route, Icons.Default.Search, "Catalogo", "Catalogo"),
    HOME(AppScreens.HomeScreen.route, Icons.Default.Home, "Home", "Home")

}

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: BottomNavViews,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination.route ?: ""
    ) {
        BottomNavViews.entries.forEach { destination ->
            destination.route?.let { route ->
                composable(destination.route) {
                    when (destination) {
                        BottomNavViews.FAVORITOS -> FavScreen(navController)
                        BottomNavViews.CARRITO -> CartScreen(navController)
                        BottomNavViews.CATALOGO -> CatalogScreen(navController)
                        BottomNavViews.HOME -> HomeScreen(navController)
                    }
                }
            }
        }
    }
}

@Composable
fun MainNavigationBar(
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit
){
    val navController = rememberNavController()
    val startDestination = BottomNavViews.HOME
    var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }

    Scaffold (
        modifier = modifier,
        bottomBar = {
            MaterialNavigationBar(windowInsets = NavigationBarDefaults.windowInsets){
                BottomNavViews.entries.forEachIndexed { index, destination ->
                    NavigationBarItem(
                        selected = selectedDestination == index,
                        onClick = {
                            if (destination== BottomNavViews.MENU){
                                onMenuClick()
                            } else {
                                navController.navigate(route = destination.route ?: "")
                                selectedDestination = index
                            }
                        },
                        icon = {
                            Icon(
                                destination.icon,
                                contentDescription = destination.contentDescription
                            )
                        },
                        label = { Text(destination.label) }
                    )
                }
            }
        }
    ){contentPadding ->
        AppNavHost(navController, startDestination,modifier= Modifier.padding(contentPadding))

    }

}
