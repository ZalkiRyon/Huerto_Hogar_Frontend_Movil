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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.huerto_hogar.AppScreens.AppScreens
import com.example.huerto_hogar.R
import java.text.NumberFormat
import java.util.Locale

// We have to change this one time is done list of products and categories
data class Product(
    val id: Int,
    val name: String,
    val price: Int,
    val imageResId: Int
)

data class Category(
    val name: String,
    val route: String,
    val iconResId: Int
)

val featuredProducts = listOf(
    Product(1, "Manzanas Fuji", 1200, R.drawable.manzana_fuji),
    Product(2, "Naranjas Valencia", 1000, R.drawable.naranja_valencia),
    Product(3, "Plátanos Cavendish", 800, R.drawable.platano),
    Product(4, "Pimentón Rojo", 1500, R.drawable.pimientos),
    Product(5, "Espinaca Orgánica", 2500, R.drawable.espinaca)
)

val categories = listOf(
    Category("Frutas", AppScreens.FrutasScreen.route, R.drawable.orange),
    Category("Verduras", AppScreens.VerdurasScreen.route, R.drawable.carrot),
    Category("Productos orgánicos", AppScreens.OrganicosScreen.route, R.drawable.plant)
)

@Composable
fun HomeScreen(navController: NavController) {
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
        FeaturedProductsCarousel(products = featuredProducts)
    }
}


@Composable
fun HeaderBanner(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(Color(0xFFF4F5F7))
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
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
                            Color.Transparent,
                            Color(0xFFF4F5F7)
                        ),
                        startY = 0.0f,
                        endY = 250.dp.value * 0.9f
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 60.dp, bottom = 24.dp, end = 24.dp, start = 24.dp)
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Huerto Hogar",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Black,
                    color = Color.DarkGray
                ),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "¡Descubre la frescura del campo con HuertoHogar!",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = Color.DarkGray
            )
            Text(
                text = "Conéctate con la naturaleza y lleva lo mejor del campo a tu mesa.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = Color.DarkGray
            )
            Text(
                text = "¡Únete a nosotros y disfruta de productos frescos y saludables, directo a tu hogar!",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { /* TODO: Navegar a la ruta principal del catálogo */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E8B57)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("VER PRODUCTOS", color = Color.White, fontWeight = FontWeight.Bold)
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
            modifier = Modifier.padding(vertical = 8.dp)
        )

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp),
            color = Color(0xFF4CAF50)
        )
    }
}

@Composable
fun CategorySection(categories: List<Category>, navController: NavController) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        items(categories) { category ->
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
                        .background(Color(0xFFDCEDC8)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = category.iconResId),
                        contentDescription = category.name,
                        modifier = Modifier.size(32.dp),
                        tint = Color(0xFF4CAF50)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1
                )
            }
        }
    }
}


@Composable
fun FeaturedProductsCarousel(products: List<Product>) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(products) { product ->
            Card(
                modifier = Modifier
                    .width(160.dp)
                    .height(250.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {

                    Image(
                        painter = painterResource(id = product.imageResId),
                        contentDescription = product.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 8.dp),

                        horizontalAlignment = Alignment.End,
                    ) {

                        Text(
                            text = product.name,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 2,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,

                            )
                        Spacer(modifier = Modifier.height(4.dp))


                        fun formatAmount(amount: Double, locale: Locale): String {
                            val formatter = NumberFormat.getCurrencyInstance(locale)
                            return formatter.format(amount)
                        }

                        val deviceLocale = Locale.getDefault()
                        val formattedPrice = formatAmount(product.price.toDouble(), deviceLocale)

                        Text(
                            text = formattedPrice,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                            color = Color.Black,
                        )
                    }
                }
            }
        }
    }
}

