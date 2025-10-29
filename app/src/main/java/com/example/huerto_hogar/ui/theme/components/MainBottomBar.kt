package com.example.huerto_hogar.ui.theme.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun MainBottomBar(
    navController: NavHostController,
    onMenuClick: () -> Unit
) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    BottomAppBar(

        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                val leftItems = listOf(BottomNavViews.HOME, BottomNavViews.CATALOGO)
                leftItems.forEach { destination ->
                    val isSelected = currentRoute == destination.route
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { destination.route?.let { route -> navController.navigate(route) } },
                        icon = {
                            Icon(
                                destination.icon,
                                contentDescription = destination.contentDescription
                            )
                        },
                        label = { Text(destination.label) }
                    )
                }

                val cartRoute = BottomNavViews.CARRITO
                val isSelectCart = currentRoute == cartRoute.route

                NavigationBarItem(
                    selected = isSelectCart,
                    onClick = { cartRoute.route?.let { route -> navController.navigate(route) } },
                    icon = {
                        Icon(
                            cartRoute.icon,
                            contentDescription = cartRoute.contentDescription
                        )
                    },
                    label = { Text(cartRoute.label) }
                )

                val rightItems = listOf(BottomNavViews.FAVORITOS, BottomNavViews.MENU)
                rightItems.forEach { destination ->
                    val isSelected = currentRoute == destination.route
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            if (destination == BottomNavViews.MENU) {
                                onMenuClick()
                            } else {
                                destination.route?.let { route -> navController.navigate(route) }
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
    )
}