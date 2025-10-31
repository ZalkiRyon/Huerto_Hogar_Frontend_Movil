package com.example.huerto_hogar.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.huerto_hogar.ui.theme.components.CatalogoNavigation

@Composable
fun VerdurasScreen (navController: NavHostController){
    Scaffold(
        topBar = {
            CatalogoNavigation(navController = navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text("Catálogo de Vegetales", style = MaterialTheme.typography.headlineMedium)
            // Aquí tu contenido específico de vegetales
        }
    }
}