package com.example.huerto_hogar.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.huerto_hogar.model.Product
import com.example.huerto_hogar.ui.theme.Huerto_HogarTheme
import com.example.huerto_hogar.ui.theme.components.Header
import com.example.huerto_hogar.ui.theme.components.ModalDetailProduct
import com.example.huerto_hogar.ui.theme.components.ProductCard
import com.example.huerto_hogar.viewmodel.CartViewModel
import com.example.huerto_hogar.viewmodel.FavoritesViewModel
import com.example.huerto_hogar.viewmodel.ProductUiState
import com.example.huerto_hogar.viewmodel.ProductViewModel
import com.example.huerto_hogar.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun OrganicosScreen(
    navController: NavHostController,
    productViewModel: ProductViewModel = viewModel(),
    cartViewModel: CartViewModel = viewModel(),
    favoritesViewModel: FavoritesViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel()
) {
    // Observar estados del ViewModel
    val productsState by productViewModel.productsState.collectAsState()
    val products by productViewModel.products.collectAsState()
    val currentUser by userViewModel.currentUser.collectAsState()


    // Cargar todos los productos para filtrar múltiples categorías
    LaunchedEffect(Unit) {
        productViewModel.getAllProducts()
    }

    // Filtrar orgánicos y lácteos del estado
    val organicos = products.filter {
        it.category.equals("Productos organicos", ignoreCase = true) ||
                it.category.equals("Productos lacteos", ignoreCase = true)
    }

    val favoriteItems by favoritesViewModel.favoriteItems.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val onLoginRequiredHandler: () -> Unit = {
        coroutineScope.launch {
            snackbarHostState.showSnackbar(
                message = "Necesitas iniciar sesión para agregar favoritos.",
                duration = SnackbarDuration.Long,

                )
        }
    }

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
                    if (currentUser == null) {
                        onLoginRequiredHandler()
                    } else {
                        val wasAdded = favoritesViewModel.addToFavorites(product)
                        coroutineScope.launch {
                            if (wasAdded) {
                                snackbarHostState.showSnackbar(
                                    message = "${product.name} agregado a favoritos",
                                    duration = SnackbarDuration.Short
                                )
                            } else {
                                favoritesViewModel.removeFromFavorites(product.id)
                                snackbarHostState.showSnackbar(
                                    message = "${product.name} eliminado de favoritos",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    }
                },
            )
        }

        // Manejo de estados de carga y error
        when (productsState) {
            is ProductUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is ProductUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = (productsState as ProductUiState.Error).message,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { productViewModel.getProductsByCategory("Orgánicos") }) {
                            Text("Reintentar")
                        }
                    }
                }
            }

            is ProductUiState.Success, ProductUiState.Idle -> {
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
                                        message = "${productoAgregado.name} agregado al carrito",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            },

                            onToggleFavorito = { product ->
                                if (currentUser == null) {
                                    onLoginRequiredHandler()
                                } else {
                                    val wasAdded = favoritesViewModel.addToFavorites(product)
                                    coroutineScope.launch {
                                        if (wasAdded) {
                                            snackbarHostState.showSnackbar(
                                                message = "${product.name} agregado a favoritos",
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
                                }


                            },
                            isFavorito = favoriteItems.any { it.id == producto.id }
                        )
                    }
                }
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