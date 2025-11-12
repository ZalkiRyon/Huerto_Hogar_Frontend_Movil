package com.example.huerto_hogar.ui.theme.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

/**
 * Barra de navegación inferior con animaciones de escala en íconos seleccionados.
 */
@Composable
fun MainBottomBar(
    navController: NavHostController,
    onMenuClick: () -> Unit
) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                val leftItems = listOf(BottomNavViews.HOME, BottomNavViews.CATALOGO)
                leftItems.forEach { destination ->
                    val isSelected = currentRoute == destination.route
                    val scale by animateFloatAsState(
                        targetValue = if (isSelected) 1.15f else 1f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMedium
                        ),
                        label = "icon_scale_${destination.label}"
                    )
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            if (destination.route == null) {
                                when (destination) {
                                    BottomNavViews.MENU -> onMenuClick()
                                    else -> {}
                                }
                            } else {
                                destination.route?.let { route -> navController.navigate(route) }
                            }
                        },
                        icon = {
                            Icon(
                                destination.icon,
                                contentDescription = destination.contentDescription,
                                modifier = Modifier.scale(scale)
                            )
                        },
                        label = { Text(destination.label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                        )
                    )
                }

                val cartRoute = BottomNavViews.CARRITO
                val isSelectCart = currentRoute == cartRoute.route
                val cartScale by animateFloatAsState(
                    targetValue = if (isSelectCart) 1.15f else 1f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    ),
                    label = "cart_icon_scale"
                )

                NavigationBarItem(
                    selected = isSelectCart,
                    onClick = { cartRoute.route?.let { route -> navController.navigate(route) } },
                    icon = {
                        Icon(
                            cartRoute.icon,
                            contentDescription = cartRoute.contentDescription,
                            modifier = Modifier.scale(cartScale)
                        )
                    },
                    label = { Text(cartRoute.label) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                    )
                )

                val rightItems = listOf(BottomNavViews.FAVORITOS, BottomNavViews.MENU)
                rightItems.forEach { destination ->
                    val isSelected = currentRoute == destination.route
                    val scale by animateFloatAsState(
                        targetValue = if (isSelected) 1.15f else 1f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMedium
                        ),
                        label = "icon_scale_${destination.label}"
                    )

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
                                contentDescription = destination.contentDescription,
                                modifier = Modifier.scale(scale)
                            )
                        },
                        label = { Text(destination.label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                        )
                    )
                }
            }
        }
    )
}

