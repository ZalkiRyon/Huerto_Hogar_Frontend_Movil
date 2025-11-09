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

@Composable
fun OrganicosScreen(navController: NavHostController) {
    // Filtrar solo productos de categoría ORGANICOS
    val organicos = remember {
        MockProducts.products.filter { it.category == ProductCategory.ORGANICOS }
    }

    Scaffold(
        topBar = {
            Header(
                navController = navController,
                title = "Productos Orgánicos"
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            items(
                organicos,
                key = { it.id }
            ) { producto ->
                ProductoOrganicosCard(
                    producto = producto,
                    onAgregarCarrito = { productoAgregado ->
                        // TODO: Implementar lógica del carrito
                    }
                )
            }
        }
    }
}

@Composable
fun ProductoOrganicosCard(
    producto: Product,
    onAgregarCarrito: (Product) -> Unit
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                Image(
                    painter = painterResource(id = producto.imageUrl ?: R.drawable.imagen_no_found),
                    contentDescription = producto.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.width(16.dp))

            // Información Producto
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = producto.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Precio moneda
                    Text(
                        text = "$${producto.price.toInt()}",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = { onAgregarCarrito(producto) },
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OrganicosScreenPreview() {
    Huerto_HogarTheme {
        val navController = rememberNavController()
        OrganicosScreen(navController = navController)
    }
}