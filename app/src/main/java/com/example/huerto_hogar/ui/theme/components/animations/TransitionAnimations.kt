package com.example.huerto_hogar.ui.theme.components.animations

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavBackStackEntry
import kotlin.math.roundToInt

/**
 * Transiciones animadas para navegación entre pantallas.
 * Proporciona efectos de entrada y salida compatibles con Navigation Compose.
 */

// ============================================
// FUNCIONES BASE - Slide y Fade
// ============================================

private fun slideInFromRight(): EnterTransition {
    return slideInHorizontally(
        initialOffsetX = { fullWidth -> fullWidth },
        animationSpec = tween(
            durationMillis = AnimationSpecs.DURATION_MEDIUM,
            easing = AnimationSpecs.EaseInOutQuad
        )
    )
}

private fun slideOutToLeft(): ExitTransition {
    return slideOutHorizontally(
        targetOffsetX = { fullWidth -> -fullWidth },
        animationSpec = tween(
            durationMillis = AnimationSpecs.DURATION_MEDIUM,
            easing = AnimationSpecs.EaseInOutQuad
        )
    )
}

private fun slideInFromBottom(): EnterTransition {
    return slideInVertically(
        initialOffsetY = { fullHeight -> fullHeight },
        animationSpec = tween(
            durationMillis = AnimationSpecs.DURATION_MEDIUM,
            easing = AnimationSpecs.EaseInOutQuad
        )
    )
}

private fun slideOutToBottom(): ExitTransition {
    return slideOutVertically(
        targetOffsetY = { fullHeight -> fullHeight },
        animationSpec = tween(
            durationMillis = AnimationSpecs.DURATION_MEDIUM,
            easing = AnimationSpecs.EaseInOutQuad
        )
    )
}

fun fadeIn(): EnterTransition {
    return fadeIn(
        animationSpec = tween(
            durationMillis = AnimationSpecs.DURATION_SHORT,
            easing = LinearEasing
        )
    )
}

fun fadeOut(): ExitTransition {
    return fadeOut(
        animationSpec = tween(
            durationMillis = AnimationSpecs.DURATION_SHORT,
            easing = LinearEasing
        )
    )
}

fun scaleInWithFade(): EnterTransition {
    return scaleIn(
        initialScale = 0.8f,
        animationSpec = tween(
            durationMillis = AnimationSpecs.DURATION_MEDIUM,
            easing = AnimationSpecs.EaseOutBack
        )
    ) + fadeIn(
        animationSpec = tween(
            durationMillis = AnimationSpecs.DURATION_MEDIUM
        )
    )
}

fun scaleOutWithFade(): ExitTransition {
    return scaleOut(
        targetScale = 0.8f,
        animationSpec = tween(
            durationMillis = AnimationSpecs.DURATION_SHORT,
            easing = FastOutLinearInEasing
        )
    ) + fadeOut(
        animationSpec = tween(
            durationMillis = AnimationSpecs.DURATION_SHORT
        )
    )
}

// ============================================
// TRANSICIONES COMBINADAS (USADAS)
// ============================================

fun slideInFromRightWithFade(): EnterTransition {
    return slideInFromRight() + fadeIn()
}

fun slideOutToLeftWithFade(): ExitTransition {
    return slideOutToLeft() + fadeOut()
}

fun slideInFromBottomWithFade(): EnterTransition {
    return slideInFromBottom() + fadeIn()
}

fun slideOutToBottomWithFade(): ExitTransition {
    return slideOutToBottom() + fadeOut()
}


