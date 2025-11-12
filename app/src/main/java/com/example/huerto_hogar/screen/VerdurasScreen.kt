package com.example.huerto_hogar.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.huerto_hogar.R
import com.example.huerto_hogar.model.Product
import com.example.huerto_hogar.model.ProductCategory
import com.example.huerto_hogar.model.MockProducts
import com.example.huerto_hogar.ui.theme.Huerto_HogarTheme
import com.example.huerto_hogar.ui.theme.components.Header
import com.example.huerto_hogar.ui.theme.components.ProductCard
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.huerto_hogar.viewmodel.CartViewModel
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope

@Composable
fun VerdurasScreen(
    navController: NavHostController,
    cartViewModel: CartViewModel = viewModel()
) {
    // Filtrar solo productos de categoría VERDURAS
    val verduras = remember {
        MockProducts.products.filter { it.category == ProductCategory.VERDURAS }
    }
    
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            Header(
                navController = navController,
                title = "Verduras"
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            items(
                verduras,
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
                        // TODO: Implementar lógica de favoritos
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Funcionalidad de favoritos próximamente",
                                duration = SnackbarDuration.Short
                            )
                        }
                    },
                    isFavorito = false // TODO: Obtener estado real de favoritos
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun VerdurasScreenPreview() {
    Huerto_HogarTheme {
        val navController = rememberNavController()
        VerdurasScreen(navController = navController)
    }
}