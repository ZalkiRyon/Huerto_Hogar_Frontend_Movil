package com.example.huerto_hogar.ui.theme.components.animations

import androidx.compose.animation.core.*
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

/**
 * Animaciones para componentes de tipo botón.
 * Implementa efectos visuales de interacción, estados de carga y feedback táctil.
 */

// ============================================
// 1. PRESS/CLICK ANIMATION - Scale Down
// ============================================

/**
 * Modifier que aplica efecto de escala durante la interacción de presión.
 * Reduce el tamaño del componente mientras está siendo presionado.
 * 
 * @param pressedScale Factor de escala durante el estado presionado (default 0.95)
 */
fun Modifier.pressClickEffect(
    pressedScale: Float = AnimationSpecs.SCALE_PRESSED
) = composed {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) pressedScale else AnimationSpecs.SCALE_NORMAL,
        animationSpec = AnimationSpecs.buttonPressSpec(),
        label = "pressScale"
    )
    
    this
        .scale(scale)
        .pointerInput(Unit) {
            coroutineScope {
                while (true) {
                    awaitPointerEventScope {
                        awaitFirstDown(requireUnconsumed = false)
                        isPressed = true
                        
                        waitForUpOrCancellation()
                        isPressed = false
                    }
                }
            }
        }
}

/**
 * Variant que utiliza MutableInteractionSource para componentes Material3.
 * Compatible con botones que ya manejan estados de interacción.
 */
@Composable
fun Modifier.pressClickEffectWithInteraction(
    interactionSource: MutableInteractionSource
): Modifier {
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) AnimationSpecs.SCALE_PRESSED else AnimationSpecs.SCALE_NORMAL,
        animationSpec = AnimationSpecs.buttonPressSpec(),
        label = "pressScaleInteraction"
    )
    
    return this.scale(scale)
}

// ============================================
// 2. BOUNCE ANIMATION - Aparición con rebote
// ============================================

/**
 * Modifier que anima la aparición del componente con efecto de rebote elástico.
 * Se ejecuta una vez al momento de la composición.
 * 
 * @param delay Retraso en milisegundos antes de iniciar la animación
 */
@Composable
fun Modifier.bounceInEffect(
    delay: Int = 0
): Modifier {
    var hasAnimated by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (hasAnimated) AnimationSpecs.SCALE_NORMAL else AnimationSpecs.SCALE_SMALL,
        animationSpec = AnimationSpecs.bounceSpec(),
        label = "bounceIn"
    )
    
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(delay.toLong())
        hasAnimated = true
    }
    
    return this.scale(scale)
}




