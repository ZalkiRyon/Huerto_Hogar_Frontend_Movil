package com.example.huerto_hogar.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.huerto_hogar.R
import com.example.huerto_hogar.model.Product
import com.example.huerto_hogar.model.User
import com.example.huerto_hogar.ui.theme.components.Header
import com.example.huerto_hogar.ui.theme.components.animations.bounceInEffect
import com.example.huerto_hogar.ui.theme.components.animations.pressClickEffect
import com.example.huerto_hogar.viewmodel.CartViewModel
import com.example.huerto_hogar.viewmodel.FavoritesViewModel

@Composable
fun FavScreen(
    navController: NavHostController,
    favoritesViewModel: FavoritesViewModel = viewModel(),
    cartViewModel: CartViewModel = viewModel(),
    user: User?
) {
    val favoriteItems by favoritesViewModel.favoriteItems.collectAsState()
    var showClearDialog by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf<String?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }

    // Reload favorites every time the screen is displayed
    LaunchedEffect(Unit) {
        user?.id?.let { userId ->
            favoritesViewModel.reloadFavorites()
        }
    }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let { message ->
            snackbarHostState.showSnackbar(
                message = message, duration = SnackbarDuration.Short
            )
            snackbarMessage = null
        }
    }

    Scaffold(topBar = {
        Header(
            navController = navController,
            title = "Mis Favoritos",
        )
    }, snackbarHost = {
        SnackbarHost(
            hostState = snackbarHostState, modifier = Modifier.padding(16.dp)
        )
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
                .background(MaterialTheme.colorScheme.background)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${favoriteItems.size} producto(s) en favoritos",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )

                if (favoriteItems.isNotEmpty()) {
                    IconButton(
                        onClick = { showClearDialog = true },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = androidx.compose.ui.graphics.Color(0xFFD32F2F)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Vaciar favoritos",
                            tint = androidx.compose.ui.graphics.Color.White
                        )
                    }
                }
            }

            if (favoriteItems.isEmpty()) {
                // Empty favorites state
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = "Sin favoritos",
                            modifier = Modifier
                                .size(80.dp)
                                .bounceInEffect(delay = 0),
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                        )
                        Text(
                            text = "No tienes favoritos",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                            modifier = Modifier.bounceInEffect(delay = 100)
                        )
                        Text(
                            text = "Agrega productos desde el catálogo",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.bounceInEffect(delay = 200)
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentPadding = PaddingValues(
                            horizontal = 16.dp, vertical = 8.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(favoriteItems) { product ->
                            FavoriteItemCard(
                                product = product, onRemove = {
                                    favoritesViewModel.removeFromFavorites(product.id)
                                    snackbarMessage = "Producto eliminado de favoritos"
                                }, onAddToCart = {
                                    cartViewModel.addToCart(product)
                                    snackbarMessage = "Producto agregado al carrito"
                                }, user = user
                            )
                        }
                    }
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.surface,
                        tonalElevation = 4.dp,
                        shadowElevation = 8.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Verify if user is logged
                            if (user != null) {
                                // if user is logged button add cart
                                Button(
                                    onClick = {
                                        favoriteItems.forEach { product ->
                                            cartViewModel.addToCart(product)
                                        }
                                        snackbarMessage =
                                            "${favoriteItems.size} producto(s) agregado(s) al carrito"
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp)
                                        .bounceInEffect(delay = 0)
                                        .pressClickEffect(),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ShoppingCart,
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Agregar Todos al Carrito",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            } else {
                                // if user is not logged button go to login
                                Button(
                                    onClick = { navController.navigate("login_screen") },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp)
                                        .bounceInEffect(delay = 0)
                                        .pressClickEffect(),
                                ) {
                                    Text(
                                        text = "Iniciar sesión para proceder",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text("Vaciar Favoritos") },
            text = { Text("¿Estás seguro de que deseas vaciar tu lista de favoritos? Esta acción no se puede deshacer.") },
            confirmButton = {
                Button(
                    onClick = {
                        favoritesViewModel.clearFavorites()
                        showClearDialog = false
                        snackbarMessage = "Lista de favoritos vaciada"
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = androidx.compose.ui.graphics.Color(0xFFD32F2F)
                    )
                ) {
                    Text("Vaciar", color = androidx.compose.ui.graphics.Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text("Cancelar")
                }
            })
    }
}

@Composable
fun FavoriteItemCard(
    product: Product, onRemove: () -> Unit, onAddToCart: () -> Unit, user: User?
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Product image
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            // Product info
            Column(
                modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = product.category,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
                Text(
                    text = "$${product.price.toInt()}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Action buttons
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Add to cart button
                IconButton(
                    onClick = onAddToCart, colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Agregar al carrito",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }

                // Remove from favorites button
                IconButton(
                    onClick = onRemove, colors = IconButtonDefaults.iconButtonColors(
                        containerColor = androidx.compose.ui.graphics.Color(0xFFD32F2F)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar de favoritos",
                        tint = androidx.compose.ui.graphics.Color.White
                    )
                }


            }
        }
    }
}
