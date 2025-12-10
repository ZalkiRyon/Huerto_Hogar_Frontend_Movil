package com.example.huerto_hogar.ui.theme.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.huerto_hogar.R
import com.example.huerto_hogar.ui.theme.Huerto_HogarTheme
import com.example.huerto_hogar.ui.theme.components.animations.pressClickEffect

/**
 * Botón de categoría reutilizable con imagen de fondo y efecto de animación
 * @param text Texto a mostrar en el botón
 * @param imageRes ID del recurso drawable de la imagen de fondo
 * @param onClick Acción al hacer clic en el botón
 * @param modifier Modificador opcional para personalizar el componente
 */
@Composable
fun CategoryButton(
    text: String,
    @DrawableRes imageRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .pressClickEffect()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp,
            pressedElevation = 2.dp
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Imagen de fondo con transparencia
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = text,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.85f), // Transparencia ligera
                contentScale = ContentScale.Crop
            )
            
            // Overlay oscuro para mejorar legibilidad del texto
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.4f)
            ) {
                androidx.compose.foundation.Canvas(
                    modifier = Modifier.fillMaxSize()
                ) {
                    drawRect(color = Color.Black)
                }
            }
            
            // Texto sobre la imagen
            Text(
                text = text,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryButtonPreview() {
    Huerto_HogarTheme {
        CategoryButton(
            text = "Verduras",
            imageRes = R.drawable.carrot,
            onClick = {}
        )
    }
}
