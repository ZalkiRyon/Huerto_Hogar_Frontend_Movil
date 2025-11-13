package com.example.huerto_hogar.ui.theme.components.animations

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

/**
 * Animaciones para campos de entrada de texto.
 */

// ============================================
// FOCUS ANIMATION
// ============================================

@Composable
fun Modifier.inputFocusAnimation(
    focusedColor: Color = MaterialTheme.colorScheme.primary,
    unfocusedColor: Color = Color.Gray
): Modifier = composed {
    var isFocused by remember { mutableStateOf(false) }
    
    val scale by animateFloatAsState(
        targetValue = if (isFocused) 1.02f else 1f,
        animationSpec = tween(
            durationMillis = AnimationSpecs.DURATION_SHORT,
            easing = FastOutSlowInEasing
        ),
        label = "inputFocusScale"
    )
    
    val borderColor by animateColorAsState(
        targetValue = if (isFocused) focusedColor else unfocusedColor,
        animationSpec = tween(
            durationMillis = AnimationSpecs.DURATION_SHORT
        ),
        label = "borderColor"
    )
    
    this
        .scale(scale)
        .onFocusChanged { focusState ->
            isFocused = focusState.isFocused
        }
}

// ============================================
// ERROR SHAKE
// ============================================

@Composable
fun Modifier.inputErrorShake(errorMessage: String?): Modifier {
    var shake by remember { mutableStateOf(0) }
    
    LaunchedEffect(errorMessage) {
        if (errorMessage != null) {
            shake++
        }
    }
    
    val offsetX by animateFloatAsState(
        targetValue = 0f,
        animationSpec = if (shake > 0) {
            keyframes {
                durationMillis = 400
                0f at 0
                -15f at 50
                15f at 100
                -15f at 150
                15f at 200
                -10f at 250
                10f at 300
                0f at 400
            }
        } else {
            tween(0)
        },
        label = "errorShake"
    )
    
    return this.graphicsLayer {
        translationX = offsetX
    }
}


