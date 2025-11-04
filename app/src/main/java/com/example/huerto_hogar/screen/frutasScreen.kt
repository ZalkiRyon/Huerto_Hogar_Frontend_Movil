package com.example.huerto_hogar.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.huerto_hogar.AppScreens.AppScreens
import com.example.huerto_hogar.ui.theme.Huerto_HogarTheme
import com.example.huerto_hogar.ui.theme.components.Header

@Composable
fun FrutasScreen(
    navController: NavHostController
) {
    Scaffold(
        topBar = {
            Header(
                navController = navController,
                title = "Frutas"
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)  // ← Este padding ya incluye el espacio del topBar
                .padding(16.dp)  // ← Padding adicional para el contenido
        ) {
            items(10) { index ->
                Text(
                    text = "Fruta ${index + 1}",
                    modifier = Modifier.padding(vertical = 8.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Preview (showBackground = true, showSystemUi = true)
@Composable
fun FrutasScreenPrevew(){
    Huerto_HogarTheme {
        val navController = rememberNavController()

        FrutasScreen(navController = navController)
    }
}