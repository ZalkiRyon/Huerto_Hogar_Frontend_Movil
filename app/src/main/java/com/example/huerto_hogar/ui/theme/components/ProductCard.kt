package com.example.huerto_hogar.ui.theme.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.huerto_hogar.model.Product
import com.example.huerto_hogar.ui.theme.Huerto_HogarTheme
import java.text.NumberFormat
import java.util.Locale

/**
 * Tarjeta de producto reutilizable con opciones de agregar al carrito y favoritos
 * @param producto El producto a mostrar
 * @param onProductClick Callback cuando se hace clic en la imagen o nombre del producto
 * @param onAgregarCarrito Callback cuando se agrega el producto al carrito
 * @param onToggleFavorito Callback cuando se agrega/quita el producto de favoritos
 * @param isFavorito Indica si el producto está en favoritos
 */
@Composable
fun ProductCard(
    producto: Product,
    onProductClick: (Product) -> Unit = {},
    onAgregarCarrito: (Product) -> Unit,
    onToggleFavorito: (Product) -> Unit = {},
    isFavorito: Boolean = false,
    onLoginRequired: () -> Unit = {},
    isUserLoggedIn: Boolean = false
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(bottom = 16.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Imagen del producto - clickeable
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onProductClick(producto) }
            ) {
                AsyncImage(
                    model = producto.imageUrl,
                    contentDescription = producto.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Información del producto
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Nombre y precio - clickeable
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .clickable { onProductClick(producto) }
                ) {
                    Text(
                        text = producto.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    fun formatAmount(amount: Double, locale: Locale): String {
                        val formatter = NumberFormat.getCurrencyInstance(locale)
                        formatter.maximumFractionDigits = 0
                        return formatter.format(amount)
                    }

                    val deviceLocale = Locale.getDefault()
                    val formattedPrice = formatAmount(producto.price, deviceLocale)

                    Text(
                        text = formattedPrice,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Botones de acción - Carrito y Favoritos en la misma fila
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Botón agregar al carrito
                    Button(
                        onClick = { onAgregarCarrito(producto) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Agregar al carrito",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Carrito", maxLines = 1)
                    }

                    // Botón favoritos
                    IconButton(
                        onClick = {
                            if (isUserLoggedIn) {
                                onToggleFavorito(producto)
                            } else {
                                onLoginRequired()
                            }
                        },
                        modifier = Modifier.size(40.dp),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = if (isFavorito) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Icon(
                            imageVector = if (isFavorito) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = if (isFavorito) "Quitar de favoritos" else "Agregar a favoritos",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductCardPreview() {
    Huerto_HogarTheme {
        val productoEjemplo = Product(
            id = 1,
            name = "Manzanas Fuji",
            category = "Frutas frescas",
            price = 1200.0,
            stock = 45,
            imageUrl = "https://example.com/manzana.jpg",
            description = "Manzanas frescas importadas"
        )
        ProductCard(
            producto = productoEjemplo,
            onProductClick = {},
            onAgregarCarrito = {},
            onToggleFavorito = {},
            isFavorito = false
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProductCardFavoritoPreview() {
    Huerto_HogarTheme {
        val productoEjemplo = Product(
            id = 1,
            name = "Manzanas Fuji Premium Seleccionadas",
            category = "Frutas frescas",
            price = 1200.0,
            stock = 45,
            imageUrl = "https://example.com/manzana.jpg",
            description = "Manzanas frescas importadas"
        )
        ProductCard(
            producto = productoEjemplo,
            onProductClick = {},
            onAgregarCarrito = {},
            onToggleFavorito = {},
            isFavorito = true
        )
    }
}
