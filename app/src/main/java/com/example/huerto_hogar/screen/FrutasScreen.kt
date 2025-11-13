package com.example.huerto_hogar.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import com.example.huerto_hogar.R
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.huerto_hogar.ui.theme.Huerto_HogarTheme
import com.example.huerto_hogar.ui.theme.components.Header
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.example.huerto_hogar.model.Product
import com.example.huerto_hogar.model.ProductCategory
import com.example.huerto_hogar.model.MockProducts
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.huerto_hogar.viewmodel.CartViewModel
import com.example.huerto_hogar.viewmodel.FavoritesViewModel
import com.example.huerto_hogar.ui.theme.components.ProductCard
import kotlinx.coroutines.launch
import androidx.compose.runtime.collectAsState

@Composable
fun FrutasScreen(
    navController: NavHostController,
    cartViewModel: CartViewModel = viewModel(),
    favoritesViewModel: FavoritesViewModel = viewModel()
) {
    // Filtrar solo productos de categoría FRUTAS
    val frutas = remember {
        MockProducts.products.filter { it.category == ProductCategory.FRUTAS }
    }
    
    val favoriteItems by favoritesViewModel.favoriteItems.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            Header(
                navController = navController,
                title = "Frutas"
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)  // ← Este padding ya incluye el espacio del topBar
                .padding(16.dp)  // ← Padding adicional para el contenido
        ) {
            items(
                frutas,
                key = { it.id }
            ) { producto ->
                ProductCard(
                    producto = producto,
                    onProductClick = { product ->
                        // TODO: Navegar a pantalla de detalle del producto
                    },
                    onAgregarCarrito = { productoAgregado ->
                        cartViewModel.addToCart(productoAgregado)
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                message = "✓ ${productoAgregado.name} agregado al carrito",
                                duration = SnackbarDuration.Short
                            )
                        }
                    },
                    onToggleFavorito = { product ->
                        val wasAdded = favoritesViewModel.addToFavorites(product)
                        coroutineScope.launch {
                            if (wasAdded) {
                                snackbarHostState.showSnackbar(
                                    message = "✓ ${product.name} agregado a favoritos",
                                    duration = SnackbarDuration.Short
                                )
                            } else {
                                snackbarHostState.showSnackbar(
                                    message = "El producto ya se encuentra agregado",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    },
                    isFavorito = favoriteItems.any { it.id == producto.id }
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

