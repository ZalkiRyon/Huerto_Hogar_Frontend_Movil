package com.example.huerto_hogar.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.huerto_hogar.AppScreens.AppScreens
import com.example.huerto_hogar.R
import com.example.huerto_hogar.model.Product
import com.example.huerto_hogar.viewmodel.ProductUiState
import com.example.huerto_hogar.viewmodel.ProductViewModel
import java.text.NumberFormat
import java.util.Locale

data class Category(
    val name: String,
    val route: String,
    val iconResId: Int
)

val categories = listOf(
    Category("Frutas", AppScreens.FrutasScreen.route, R.drawable.orange),
    Category("Verduras", AppScreens.VerdurasScreen.route, R.drawable.carrot),
    Category("Organicos", AppScreens.OrganicosScreen.route, R.drawable.plant)
)

/**
 * Pantalla principal REFACTORIZADA con integración de API
 * 
 * Cambios:
 * - Eliminado MockProducts
 * - Agregado ProductViewModel para consumir API
 * - Agregado manejo de estados (Loading, Success, Error)
 * - Usa Flow y StateFlow para reactividad
 */
@Composable
fun HomeScreen(
    navController: NavController,
    productViewModel: ProductViewModel = viewModel()
) {
    // Observar estados del ViewModel
    val productsState by productViewModel.productsState.collectAsState()
    val products by productViewModel.products.collectAsState()
    
    // Cargar productos al iniciar la pantalla
    LaunchedEffect(Unit) {
        productViewModel.getAllProducts()
    }
    
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(MaterialTheme.colorScheme.background)
    ) {
        HeaderBanner(navController = navController)

        Spacer(modifier = Modifier.height(24.dp))

        TitleWithDivider(title = "Explorar Categorías")
        CategorySection(categories = categories, navController = navController)

        Spacer(modifier = Modifier.height(24.dp))

        TitleWithDivider(title = "Productos Destacados")
        
        // Renderizar según el estado de la petición
        when (val state = productsState) {
            is ProductUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Cargando productos...")
                    }
                }
            }
            
            is ProductUiState.Success -> {
                val featuredProducts = products.take(5)
                if (featuredProducts.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No hay productos disponibles")
                    }
                } else {
                    FeaturedProductsCarousel(products = featuredProducts)
                }
            }
            
            is ProductUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Error al cargar productos",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.error
                        )
                        Text(
                            text = state.message,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { productViewModel.getAllProducts() }
                        ) {
                            Text("Reintentar")
                        }
                    }
                }
            }
            
            ProductUiState.Idle -> {
                // Estado inicial
            }
        }
    }
}


@Composable
fun HeaderBanner(navController: NavController) {
    val screenBackground = MaterialTheme.colorScheme.background
    val brandPrimary = MaterialTheme.colorScheme.primary
    val onBackground = MaterialTheme.colorScheme.onBackground
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)

    ) {
        Image(
            painter = painterResource(id = R.drawable.fondonuevobgd),
            contentDescription = "Fondo de frutas y verduras",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            screenBackground.copy(alpha = 0.3f),
                            screenBackground.copy(alpha = 0.6f),
                            screenBackground.copy(alpha = 0.85f),
                            screenBackground
                        ),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 44.dp, vertical = 40.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Huerto Hogar",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Black,
                    color = onBackground
                ),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Frescura",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Normal
                ),
                color = onBackground
            )

            Text(
                text = "Directa del",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Normal
                ),
                color = onBackground
            )

            Text(
                text = "Campo",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Normal
                ),
                color = onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Productos 100% orgánicos entregados",
                style = MaterialTheme.typography.bodyLarge,
                color = onBackground
            )

            Text(
                text = "en tu puerta",
                style = MaterialTheme.typography.bodyLarge,
                color = onBackground
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { navController.navigate(AppScreens.CatalogoScreen.route) },
                colors = ButtonDefaults.buttonColors(containerColor = brandPrimary),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 8.dp
                ),
            ) {
                Text(
                    "EXPLORAR PRODUCTOS",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

        }
    }
}

@Composable
fun TitleWithDivider(title: String) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp),
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun CategorySection(categories: List<Category>, navController: NavController) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        items(categories.withIndex().toList()) { (index, category) ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .width(80.dp)
                    .clickable { navController.navigate(category.route) }
            ) {

                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = category.iconResId),
                        contentDescription = category.name,
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}


@Composable
fun FeaturedProductsCarousel(products: List<Product>) {
    val featuredProductsList: List<Product> = products.take(3)
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(featuredProductsList.withIndex().toList()) { (index, product) ->
            Card(
                modifier = Modifier
                    .width(140.dp)
                    .height(200.dp)
                    .clickable { /* TODO: navigate to detail or modal */ },
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {

                    AsyncImage(
                        model = product.imageUrl,
                        contentDescription = product.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 12.dp),
                        horizontalAlignment = Alignment.Start,
                    ) {

                        Text(
                            text = product.name,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))


                        fun formatAmount(amount: Double, locale: Locale): String {
                            val formatter = NumberFormat.getCurrencyInstance(locale)
                            formatter.maximumFractionDigits = 0
                            return formatter.format(amount)
                        }

                        val deviceLocale = Locale.getDefault()
                        val formattedPrice = formatAmount(product.price.toDouble(), deviceLocale)


                        Text(
                            text = formattedPrice,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold
                            ),
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
            }
        }
    }
}



