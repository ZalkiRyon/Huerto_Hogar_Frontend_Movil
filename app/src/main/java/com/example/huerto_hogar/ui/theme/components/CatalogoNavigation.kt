package com.example.huerto_hogar.ui.theme.components

import com.example.huerto_hogar.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.huerto_hogar.AppScreens.AppScreens


enum class CategoriaCatalogo(
    val route: String,
    val label: String
){
    VERDURAS (AppScreens.VerdurasScreen.route, "Verduras"),
    FRUTAS (AppScreens.FrutasScreen.route, "Frutas"),
    ORGANICOS (AppScreens.OrganicosScreen.route, "Orgánicos")
}

@Composable
fun CatalogoNavigation(
    navController: NavHostController,
    onCloseMenu: () -> Unit ={},
) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

//    NavigationBar (
//        modifier = Modifier.fillMaxWidth(),
//        containerColor = MaterialTheme.colorScheme.primaryContainer
    Surface(
        tonalElevation = 8.dp,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CategoriaCatalogo.values().forEach { categoria ->
                val isSelected = currentRoute == categoria.route
                //Aquí parte la idea de D
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .padding(4.dp)
                        .clip(MaterialTheme.shapes.small)
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.primaryContainer
                            else Color.Transparent
                        )
                        .clickable {
                            navController.navigate(categoria.route)
                            onCloseMenu()
//                            onCategorySelected(categoria)
//                            navController.navigate(categoria.route)
//                            onCategoriaSelected()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        //Tamaño del ícono
                        Icon(
                            painter = painterResource(id = iconoCategoriaDrawawa(categoria)),
                            contentDescription = categoria.label,
                            modifier = Modifier.size(18.dp),
                            tint = if (isSelected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        //Texto pequeño
                        Text(
                            text = categoria.label,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isSelected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

private fun iconoCategoriaDrawawa(categoria: CategoriaCatalogo): Int {
    return when (categoria) {
        CategoriaCatalogo.VERDURAS -> R.drawable.carrot
        CategoriaCatalogo.FRUTAS -> R.drawable.orange
        CategoriaCatalogo.ORGANICOS -> R.drawable.plant
    }
}


//                NavigationBarItem(
//                    selected = currentRoute == categoria.route,
//                    onClick = {
//                        navController.navigate(categoria.route)
//                        onCloseMenu()
//                    },
//                    icon = {},
//                    label = {
//                        Text(
//                            text = categoria.label,
//                            modifier = Modifier.padding(horizontal = 8.dp)
//                        )
//                    },
//                    modifier = Modifier
//                )
//            }
//        }
//    }
//}
