package com.example.huerto_hogar.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.huerto_hogar.ui.theme.components.CategoriaCatalogo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FrutasScreen (navController: NavController){
    Scaffold (
        topBar = {
            Column {
                //Barra arriba, flecha y título
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Frutas",
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {navController.popBackStack()}) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Atrás"
                            )
                        }
                    }
                )

                //Opciones
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ){
                    CategoriaCatalogo.values().forEach { categoria ->
                        if (categoria != CategoriaCatalogo.FRUTAS){
                            Text(
                                text = categoria.label,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .clickable{
                                        navController.navigate(categoria.route){
                                            launchSingleTop = true
                                        }
                                    }
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            )
                        }

                    }
                }
            }
        }


    ){  paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // TU CONTENIDO ESPECÍFICO DE FRUTAS
            Text(
                "Catálogo de Frutas",
                style = MaterialTheme.typography.headlineSmall
            )
            // Aquí va tu lista de frutas...
        }
    }
}