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
import kotlinx.coroutines.launch

@Composable
fun FrutasScreen(
    navController: NavHostController,
    cartViewModel: CartViewModel = viewModel()
) {
    // Filtrar solo productos de categoría FRUTAS
    val frutas = remember {
        MockProducts.products.filter { it.category == ProductCategory.FRUTAS }
    }
    
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
            ){producto ->
                ProductoCard(
                    producto = producto,
                    onAgregarCarrito = { productoAgregado ->
                        cartViewModel.addToCart(productoAgregado)
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                message = "✓ ${productoAgregado.name} agregado al carrito",
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ProductoCard(
    producto: Product,
    onAgregarCarrito: (Product) -> Unit
){
    ElevatedCard (
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        shape = RoundedCornerShape(12.dp)
    ){
        Row (
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ){
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
            ){
                Image(
                    painter = painterResource(id = producto.imageUrl ?: R.drawable.imagen_no_found),
                    contentDescription = producto.name,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.width(16.dp))

            //Información Producto
            Column (
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ){
                Column {
                    Text(
                        text = producto.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    //Precio moneda
                    Text(
                        text = "$${producto.price.toInt()}",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = {onAgregarCarrito(producto)},
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Agregar al carrito",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Agregar al carrito")
                }


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

@Preview(showBackground = true)
@Composable
fun ProductoCardPreview(){
    Huerto_HogarTheme {
        val productoEjemplo = Product(
            id = 1,
            name = "Manzanas Fuji",
            category = ProductCategory.FRUTAS,
            price = 1200.0,
            stock = 45,
            imageUrl = R.drawable.manzana_fuji,
            description = "Manzanas frescas importadas"
        )
        ProductoCard(
            producto = productoEjemplo,
            onAgregarCarrito = {}
        )
    }
}