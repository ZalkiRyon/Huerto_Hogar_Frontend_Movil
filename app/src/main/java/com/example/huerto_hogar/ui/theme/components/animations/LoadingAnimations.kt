package com.example.huerto_hogar.ui.theme.components.animations

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Animación de shimmer para estados de carga con efecto de barrido horizontal.
 * 
 * @param isLoading Estado de carga activo
 * @param brush Gradiente personalizado para el efecto shimmer
 * @param durationMillis Duración del ciclo de animación
 */
@Composable
fun Modifier.shimmerEffect(
    isLoading: Boolean = true,
    brush: Brush = Brush.linearGradient(
        colors = listOf(
            Color.LightGray.copy(alpha = 0.6f),
            Color.LightGray.copy(alpha = 0.2f),
            Color.LightGray.copy(alpha = 0.6f),
        )
    ),
    durationMillis: Int = 1300
): Modifier {
    if (!isLoading) return this

    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_translate"
    )

    return this.background(
        Brush.linearGradient(
            colors = listOf(
                Color.LightGray.copy(alpha = 0.6f),
                Color.LightGray.copy(alpha = 0.2f),
                Color.LightGray.copy(alpha = 0.6f),
            ),
            start = Offset(translateAnim - 1000f, translateAnim - 1000f),
            end = Offset(translateAnim, translateAnim)
        )
    )
}

/**
 * Indicador de carga con puntos animados.
 * 
 * @param dotCount Número de puntos (3 por defecto)
 * @param dotSize Tamaño de cada punto
 * @param color Color de los puntos
 * @param spacing Espaciado entre puntos
 */
@Composable
fun DotsLoadingIndicator(
    modifier: Modifier = Modifier,
    dotCount: Int = 3,
    dotSize: Dp = 12.dp,
    color: Color = MaterialTheme.colorScheme.primary,
    spacing: Dp = 8.dp
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(dotCount) { index ->
            val infiniteTransition = rememberInfiniteTransition(label = "dot_$index")
            
            val scale by infiniteTransition.animateFloat(
                initialValue = 0.6f,
                targetValue = 1.2f,
                animationSpec = infiniteRepeatable(
                    animation = tween(600, easing = FastOutSlowInEasing, delayMillis = index * 150),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "dot_scale_$index"
            )
            
            val alpha by infiniteTransition.animateFloat(
                initialValue = 0.3f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(600, easing = FastOutSlowInEasing, delayMillis = index * 150),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "dot_alpha_$index"
            )

            Box(
                modifier = Modifier
                    .size(dotSize)
                    .scale(scale)
                    .alpha(alpha)
                    .background(color, CircleShape)
            )
        }
    }
}

/**
 * Indicador de progreso circular con escala pulsante.
 * 
 * @param isLoading Estado de carga
 * @param size Tamaño del indicador
 * @param color Color del progreso
 * @param strokeWidth Grosor de la línea circular
 */
@Composable
fun PulsingCircularProgress(
    modifier: Modifier = Modifier,
    isLoading: Boolean = true,
    size: Dp = 48.dp,
    color: Color = MaterialTheme.colorScheme.primary,
    strokeWidth: Dp = 4.dp
) {
    if (!isLoading) return

    val infiniteTransition = rememberInfiniteTransition(label = "pulsing_progress")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "progress_scale"
    )

    CircularProgressIndicator(
        modifier = modifier
            .size(size)
            .scale(scale),
        color = color,
        strokeWidth = strokeWidth
    )
}

/**
 * Skeleton screen para carga de elementos de lista.
 * 
 * @param count Número de items skeleton
 * @param itemHeight Altura de cada item
 * @param spacing Espaciado entre items
 */
@Composable
fun SkeletonListLoader(
    modifier: Modifier = Modifier,
    count: Int = 5,
    itemHeight: Dp = 80.dp,
    spacing: Dp = 12.dp
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(spacing)
    ) {
        repeat(count) { index ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(itemHeight)
                    .shimmerEffect()
                    .clip(RoundedCornerShape(12.dp)),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Imagen placeholder
                Box(
                    modifier = Modifier
                        .size(itemHeight - 16.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray.copy(alpha = 0.3f))
                )
                
                // Contenido texto
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Título
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .height(16.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.LightGray.copy(alpha = 0.5f))
                    )
                    
                    // Subtítulo
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .height(12.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.LightGray.copy(alpha = 0.3f))
                    )
                }
            }
        }
    }
}

/**
 * Indicador de progreso lineal con animación de barrido.
 * 
 * @param isLoading Estado de carga
 * @param color Color de la barra de progreso
 * @param trackColor Color del fondo de la barra
 */
@Composable
fun AnimatedLinearProgress(
    modifier: Modifier = Modifier,
    isLoading: Boolean = true,
    color: Color = MaterialTheme.colorScheme.primary,
    trackColor: Color = color.copy(alpha = 0.12f)
) {
    if (!isLoading) return

    LinearProgressIndicator(
        modifier = modifier.fillMaxWidth(),
        color = color,
        trackColor = trackColor
    )
}

/**
 * Spinner de carga con rotación y pulso combinados.
 * 
 * @param size Tamaño del spinner
 * @param color Color del spinner
 */
@Composable
fun RotatingPulseSpinner(
    modifier: Modifier = Modifier,
    size: Dp = 56.dp,
    color: Color = MaterialTheme.colorScheme.primary
) {
    val infiniteTransition = rememberInfiniteTransition(label = "spinner")
    
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )
    
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Box(
        modifier = modifier
            .size(size)
            .scale(scale),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.fillMaxSize(),
            color = color,
            strokeWidth = 4.dp
        )
    }
}

/**
 * Skeleton para tarjeta de producto.
 * 
 * @param height Altura de la tarjeta
 */
@Composable
fun ProductCardSkeleton(
    modifier: Modifier = Modifier,
    height: Dp = 200.dp
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .shimmerEffect()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.LightGray.copy(alpha = 0.2f))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Imagen producto
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.LightGray.copy(alpha = 0.4f))
        )
        
        // Título
        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(16.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color.LightGray.copy(alpha = 0.5f))
        )
        
        // Precio
        Box(
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .height(20.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color.LightGray.copy(alpha = 0.6f))
        )
    }
}

/**
 * Indicador de carga tipo "bouncing balls" con física de rebote.
 * 
 * @param ballCount Número de bolas
 * @param ballSize Tamaño de cada bola
 * @param color Color de las bolas
 */
@Composable
fun BouncingBallsLoader(
    modifier: Modifier = Modifier,
    ballCount: Int = 3,
    ballSize: Dp = 16.dp,
    color: Color = MaterialTheme.colorScheme.primary
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        repeat(ballCount) { index ->
            val infiniteTransition = rememberInfiniteTransition(label = "ball_$index")
            
            val offsetY by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = -30f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 600,
                        easing = FastOutSlowInEasing,
                        delayMillis = index * 100
                    ),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "ball_bounce_$index"
            )

            Box(
                modifier = Modifier
                    .size(ballSize)
                    .offset(y = offsetY.dp)
                    .background(color, CircleShape)
            )
        }
    }
}
