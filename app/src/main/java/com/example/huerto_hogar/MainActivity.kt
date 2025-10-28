package com.example.huerto_hogar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.huerto_hogar.AppScreens.AppScreens
import com.example.huerto_hogar.screen.HomeScreen
import com.example.huerto_hogar.screen.BlogScreen
import com.example.huerto_hogar.screen.CartScreen
import com.example.huerto_hogar.screen.UsSettScreen
import com.example.huerto_hogar.screen.CatalogScreen
import com.example.huerto_hogar.screen.FavScreen
import com.example.huerto_hogar.screen.LoginScreen
import com.example.huerto_hogar.screen.RegistroScreen

import com.example.huerto_hogar.ui.theme.Huerto_HogarTheme
import com.example.huerto_hogar.ui.theme.components.MainNavigationBar
import kotlinx.coroutines.MainScope

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Huerto_HogarTheme {
                MainScreenWithDrawer()
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = AppScreens.HomeScreen.route
                ){
                    composable(route = AppScreens.HomeScreen.route){
                        HomeScreen(navController = navController)
                    }
                    composable(route = AppScreens.LoginScreen.route){
                        LoginScreen(navController = navController)
                    }
                    composable(route = AppScreens.RegistroScreen.route){
                        RegistroScreen(navController = navController)
                    }
                    composable(route = AppScreens.FavScreen.route){
                        FavScreen(navController = navController)
                    }
                    composable(route = AppScreens.CartScreen.route){
                        CartScreen(navController = navController)
                    }
                    composable(route = AppScreens.CatalogScreen.route){
                        CatalogScreen(navController = navController)
                    }
                    composable(route = AppScreens.UsSettScreen.route){
                        UsSettScreen(navController = navController)
                    }
                    composable(route = AppScreens.blogScreen.route){
                        BlogScreen(navController = navController)
                    }
                }
            }
        }
    }
}
