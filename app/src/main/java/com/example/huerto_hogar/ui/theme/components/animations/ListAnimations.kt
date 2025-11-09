package com.example.huerto_hogar.ui.theme.components.animations

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

/**
 * Animación de aparición escalonada para items de lista.
 * 
 * @param itemIndex Índice del item en la lista
 * @param delayPerItem Delay en ms por cada item
 */
@Composable
fun Modifier.staggeredListItemAnimation(
    itemIndex: Int,
    delayPerItem: Int = 50
): Modifier {
    var visible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        delay((itemIndex * delayPerItem).toLong())
        visible = true
    }

    val offsetX by animateIntAsState(
        targetValue = if (visible) 0 else 100,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "list_item_offset_$itemIndex"
    )

    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(300),
        label = "list_item_alpha_$itemIndex"
    )

    return this
        .offset { IntOffset(offsetX, 0) }
        .alpha(alpha)
}

/**
 * Animación de swipe para acciones en items de lista.
 * 
 * @param onSwipeLeft Callback al hacer swipe izquierda
 * @param onSwipeRight Callback al hacer swipe derecha
 * @param threshold Distancia mínima para activar swipe
 */
@Composable
fun Modifier.swipeableListItem(
    onSwipeLeft: (() -> Unit)? = null,
    onSwipeRight: (() -> Unit)? = null,
    threshold: Float = 200f
): Modifier {
    var offsetX by remember { mutableStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }

    val animatedOffsetX by animateFloatAsState(
        targetValue = if (isDragging) offsetX else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "swipe_offset",
        finishedListener = {
            if (!isDragging && offsetX < -threshold) {
                onSwipeLeft?.invoke()
            } else if (!isDragging && offsetX > threshold) {
                onSwipeRight?.invoke()
            }
            offsetX = 0f
        }
    )

    return this
        .offset { IntOffset(animatedOffsetX.roundToInt(), 0) }
        .pointerInput(Unit) {
            detectHorizontalDragGestures(
                onDragStart = { isDragging = true },
                onDragEnd = { isDragging = false },
                onDragCancel = { 
                    isDragging = false
                    offsetX = 0f
                },
                onHorizontalDrag = { _, dragAmount ->
                    offsetX += dragAmount
                }
            )
        }
}

/**
 * Animación de "añadir al carrito" con escala y desvanecimiento.
 * 
 * @param trigger Estado que activa la animación
 */
@Composable
fun Modifier.addToCartAnimation(
    trigger: Boolean
): Modifier {
    var isAnimating by remember { mutableStateOf(false) }
    
    LaunchedEffect(trigger) {
        if (trigger) {
            isAnimating = true
            delay(600)
            isAnimating = false
        }
    }

    val scale by animateFloatAsState(
        targetValue = if (isAnimating) 1.5f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "cart_scale"
    )

    val alpha by animateFloatAsState(
        targetValue = if (isAnimating) 0f else 1f,
        animationSpec = tween(400, delayMillis = 200),
        label = "cart_alpha"
    )

    return this
        .scale(scale)
        .alpha(alpha)
}

/**
 * Animación de expansión/colapso para item de lista.
 * 
 * @param isExpanded Estado de expansión
 */
@Composable
fun Modifier.expandableListItem(
    isExpanded: Boolean
): Modifier {
    val scale by animateFloatAsState(
        targetValue = if (isExpanded) 1.02f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "expand_scale"
    )

    return this.scale(scaleY = scale, scaleX = 1f)
}

/**
 * Animación de eliminación de item con deslizamiento y fade.
 * 
 * @param isDeleted Estado de eliminación
 * @param onAnimationFinished Callback al terminar animación
 */
@Composable
fun Modifier.deleteItemAnimation(
    isDeleted: Boolean,
    onAnimationFinished: () -> Unit = {}
): Modifier {
    var isAnimating by remember { mutableStateOf(false) }

    LaunchedEffect(isDeleted) {
        if (isDeleted) {
            isAnimating = true
            delay(300)
            onAnimationFinished()
        }
    }

    val offsetX by animateIntAsState(
        targetValue = if (isAnimating) -1000 else 0,
        animationSpec = tween(300, easing = FastOutLinearInEasing),
        label = "delete_offset"
    )

    val alpha by animateFloatAsState(
        targetValue = if (isAnimating) 0f else 1f,
        animationSpec = tween(300),
        label = "delete_alpha"
    )

    val scale by animateFloatAsState(
        targetValue = if (isAnimating) 0.8f else 1f,
        animationSpec = tween(200),
        label = "delete_scale"
    )

    return this
        .offset { IntOffset(offsetX, 0) }
        .alpha(alpha)
        .scale(scale)
}

/**
 * Animación de reordenamiento de items con transición suave.
 * 
 * @param position Posición actual del item
 */
@Composable
fun Modifier.reorderableItemAnimation(
    position: Int
): Modifier {
    val offsetY by animateIntAsState(
        targetValue = position * 80,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "reorder_offset_$position"
    )

    return this.offset { IntOffset(0, offsetY) }
}

/**
 * Animación de "pull to refresh" para listas.
 * 
 * @param pullDistance Distancia de arrastre actual
 * @param threshold Distancia mínima para activar refresh
 */
@Composable
fun Modifier.pullToRefreshAnimation(
    pullDistance: Float,
    threshold: Float = 150f
): Modifier {
    val progress = (pullDistance / threshold).coerceIn(0f, 1f)
    
    val scale by animateFloatAsState(
        targetValue = 0.8f + (progress * 0.4f),
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "pull_scale"
    )

    val rotation by animateFloatAsState(
        targetValue = progress * 360f,
        animationSpec = tween(300),
        label = "pull_rotation"
    )

    return this.scale(scale)
}

/**
 * Animación de "favorito" con rebote al activar/desactivar.
 * 
 * @param isFavorite Estado de favorito
 */
@Composable
fun Modifier.favoriteToggleAnimation(
    isFavorite: Boolean
): Modifier {
    val scale by animateFloatAsState(
        targetValue = if (isFavorite) 1.2f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "favorite_scale"
    )

    return this.scale(scale)
}

/**
 * Animación de cantidad en carrito con bounce al incrementar.
 * 
 * @param quantity Cantidad actual
 */
@Composable
fun Modifier.quantityChangeAnimation(
    quantity: Int
): Modifier {
    var previousQuantity by remember { mutableStateOf(quantity) }
    var shouldAnimate by remember { mutableStateOf(false) }

    LaunchedEffect(quantity) {
        if (quantity != previousQuantity) {
            shouldAnimate = true
            delay(300)
            shouldAnimate = false
            previousQuantity = quantity
        }
    }

    val scale by animateFloatAsState(
        targetValue = if (shouldAnimate) 1.3f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "quantity_scale"
    )

    return this.scale(scale)
}

/**
 * Animación de hover/resaltado para items.
 * 
 * @param isHovered Estado de hover
 */
@Composable
fun Modifier.hoverHighlightAnimation(
    isHovered: Boolean
): Modifier {
    val scale by animateFloatAsState(
        targetValue = if (isHovered) 1.05f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "hover_scale"
    )

    val elevation by animateDpAsState(
        targetValue = if (isHovered) 8.dp else 0.dp,
        animationSpec = tween(200),
        label = "hover_elevation"
    )

    return this.scale(scale)
}

/**
 * Wrapper composable para lista con animaciones automáticas de entrada.
 * 
 * @param items Lista de items a animar
 * @param content Contenido del item individual
 */
@Composable
fun <T> AnimatedLazyList(
    items: List<T>,
    delayPerItem: Int = 50,
    content: @Composable (index: Int, item: T) -> Unit
) {
    items.forEachIndexed { index, item ->
        Box(
            modifier = Modifier.staggeredListItemAnimation(
                itemIndex = index,
                delayPerItem = delayPerItem
            )
        ) {
            content(index, item)
        }
    }
}
