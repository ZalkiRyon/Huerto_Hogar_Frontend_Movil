package com.example.huerto_hogar.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.huerto_hogar.model.MockProducts
import com.example.huerto_hogar.model.Product
import com.example.huerto_hogar.model.ProductCategory
import com.example.huerto_hogar.ui.theme.Huerto_HogarTheme
import com.example.huerto_hogar.ui.theme.components.Header
import com.example.huerto_hogar.ui.theme.components.ModalDetailProduct
import com.example.huerto_hogar.ui.theme.components.ProductCard
import com.example.huerto_hogar.viewmodel.CartViewModel
import com.example.huerto_hogar.viewmodel.FavoritesViewModel
import kotlinx.coroutines.launch

@Composable
fun OrganicosScreen(
    navController: NavHostController,
    cartViewModel: CartViewModel = viewModel(),
    favoritesViewModel: FavoritesViewModel = viewModel()
) {
    // Filtrar solo productos de categoría ORGANICOS
    val organicos = remember {
        MockProducts.products.filter { it.category == ProductCategory.ORGANICOS }
    }

    val favoriteItems by favoritesViewModel.favoriteItems.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // state modal detail product
    var showModal by remember { mutableStateOf(false) }
    // selected product for detail
    var selectedProduct by remember { mutableStateOf<Product?>(null) }

    Scaffold(
        topBar = {
            Header(
                navController = navController,
                title = "Productos Orgánicos"
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        if (showModal && selectedProduct != null) {
            val isProductFavorite = favoriteItems.any { it.id == selectedProduct!!.id }
            ModalDetailProduct(
                product = selectedProduct!!,
                onClose = {
                    showModal = false
                    selectedProduct = null
                },
                cart = cartViewModel,
                isFavorite = isProductFavorite,
                onToggleFavorito = { product ->
                    val wasAdded = favoritesViewModel.addToFavorites(product)
                    coroutineScope.launch {
                        if (wasAdded) {
                            snackbarHostState.showSnackbar(
                                message = "✓ ${product.name} agregado a favoritos",
                                duration = SnackbarDuration.Short
                            )
                        } else {
                            favoritesViewModel.removeFromFavorites(product.id)
                            snackbarHostState.showSnackbar(
                                message = "X ${product.name} eliminado de favoritos",
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                },
            )
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            items(
                organicos.withIndex().toList(),
                key = { (_, product) -> product.id }
            ) { (index, producto) ->
                ProductCard(
                    producto = producto,
                    onProductClick = { product ->
                        selectedProduct = product
                        showModal = true
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
                                favoritesViewModel.removeFromFavorites(product.id)
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OrganicosScreenPreview() {
    Huerto_HogarTheme {
        val navController = rememberNavController()
        OrganicosScreen(navController = navController)
    }
}