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
import com.example.huerto_hogar.ui.theme.components.ProductCard
import kotlinx.coroutines.launch

@Composable
fun AllProductsScreen(
    navController: NavHostController,
    cartViewModel: CartViewModel = viewModel()
) {
    // Mostrar todos los productos sin filtrar
    val allProducts = remember {
        MockProducts.products
    }
    
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            Header(
                navController = navController,
                title = "Todos los Productos"
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
                allProducts,
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
fun AllProductsScreenPreview() {
    Huerto_HogarTheme {
        val navController = rememberNavController()
        AllProductsScreen(navController = navController)
    }
}
