package com.example.huerto_hogar.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.huerto_hogar.AppScreens.AppScreens
import com.example.huerto_hogar.R
import com.example.huerto_hogar.ui.theme.Huerto_HogarTheme
import com.example.huerto_hogar.ui.theme.components.CategoryButton
import com.example.huerto_hogar.ui.theme.components.Header

/**
 * Pantalla principal del catálogo que muestra botones para navegar
 * a las diferentes categorías de productos
 */
@Composable
fun CatalogoScreen(
    navController: NavHostController
) {
    Scaffold(
        topBar = {
            Header(
                navController = navController,
                title = "Catálogo"
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(vertical = 8.dp)
        ) {
            // Botón Verduras
            CategoryButton(
                text = "Verduras",
                imageRes = R.drawable.zanahorias,
                onClick = {
                    navController.navigate(AppScreens.VerdurasScreen.route)
                },
                modifier = Modifier.weight(1f)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Botón Frutas
            CategoryButton(
                text = "Frutas",
                imageRes = R.drawable.manzana_fuji,
                onClick = {
                    navController.navigate(AppScreens.FrutasScreen.route)
                },
                modifier = Modifier.weight(1f)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Botón Orgánicos
            CategoryButton(
                text = "Orgánicos",
                imageRes = R.drawable.espinaca,
                onClick = {
                    navController.navigate(AppScreens.OrganicosScreen.route)
                },
                modifier = Modifier.weight(1f)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Botón Todos los Productos
            CategoryButton(
                text = "Todos los Productos",
                imageRes = R.drawable.naranja_valencia,
                onClick = {
                    navController.navigate(AppScreens.AllProductsScreen.route)
                },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CatalogoScreenPreview() {
    Huerto_HogarTheme {
        val navController = rememberNavController()
        CatalogoScreen(navController = navController)
    }
}
