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
 * Implementa feedback visual durante interacciones, validaciones y estados de foco.
 */

// ============================================
// 1. FOCUS ANIMATION - Border + Scale
// ============================================

/**
 * Modifier que anima el estado de foco del campo de entrada.
 * Aplica transición de color y escala sutil al enfocar/desenfocar.
 * 
 * @param focusedColor Color del borde en estado enfocado
 * @param unfocusedColor Color del borde en estado no enfocado
 */
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
// 2. ERROR SHAKE - Sacudida en error
// ============================================

/**
 * Modifier que aplica efecto de sacudida al detectar error de validación.
 * La animación se activa cuando errorMessage pasa de null a un valor.
 * 
 * @param errorMessage Mensaje de error actual o null si no hay error
 */
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

// ============================================
// 3. SUCCESS INDICATOR - Checkmark animado
// ============================================

/**
 * Controlador de estado para indicador de validación exitosa.
 */
class InputSuccessState {
    var isSuccess by mutableStateOf(false)
        private set
    
    var showCheckmark by mutableStateOf(false)
        private set
    
    fun triggerSuccess() {
        isSuccess = true
        showCheckmark = true
    }
    
    fun reset() {
        isSuccess = false
        showCheckmark = false
    }
}

@Composable
fun rememberInputSuccessState(): InputSuccessState {
    return remember { InputSuccessState() }
}

/**
 * Modifier que aplica efecto de rebote para indicador de validación exitosa.
 * Anima la escala del componente con comportamiento elástico.
 * 
 * @param isSuccess Estado de validación exitosa
 */
@Composable
fun Modifier.inputSuccessBounce(isSuccess: Boolean): Modifier {
    val scale by animateFloatAsState(
        targetValue = if (isSuccess) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "successBounce"
    )
    
    return this.scale(scale)
}

// ============================================
// 4. LABEL FLOAT ANIMATION - Label sube al escribir
// ============================================

/**
 * Modifier que implementa animación de label flotante tipo Material Design.
 * El label se desplaza verticalmente y escala al enfocar o cuando hay contenido.
 * 
 * @param hasContent Indica si el campo tiene contenido
 * @param isFocused Estado de foco del campo
 */
@Composable
fun Modifier.floatingLabel(
    hasContent: Boolean,
    isFocused: Boolean
): Modifier {
    val shouldFloat = hasContent || isFocused
    
    val offsetY by animateDpAsState(
        targetValue = if (shouldFloat) (-20).dp else 0.dp,
        animationSpec = tween(
            durationMillis = AnimationSpecs.DURATION_SHORT,
            easing = FastOutSlowInEasing
        ),
        label = "labelOffsetY"
    )
    
    val scale by animateFloatAsState(
        targetValue = if (shouldFloat) 0.85f else 1f,
        animationSpec = tween(
            durationMillis = AnimationSpecs.DURATION_SHORT,
            easing = FastOutSlowInEasing
        ),
        label = "labelScale"
    )
    
    return this
        .graphicsLayer {
            translationY = offsetY.toPx()
            scaleX = scale
            scaleY = scale
        }
}

// ============================================
// 5. PASSWORD VISIBILITY TOGGLE ANIMATION
// ============================================

/**
 * Modifier que aplica rotación animada para toggle de visibilidad de contraseña.
 * Rota el ícono en el eje Y al cambiar de estado.
 * 
 * @param isVisible Estado actual de visibilidad de la contraseña
 */
@Composable
fun Modifier.passwordVisibilityAnimation(isVisible: Boolean): Modifier {
    val rotation by animateFloatAsState(
        targetValue = if (isVisible) 180f else 0f,
        animationSpec = tween(
            durationMillis = AnimationSpecs.DURATION_MEDIUM,
            easing = FastOutSlowInEasing
        ),
        label = "passwordVisibilityRotation"
    )
    
    return this.graphicsLayer {
        rotationY = rotation
    }
}

// ============================================
// 6. TYPING INDICATOR - Pulsación al escribir
// ============================================

/**
 * Modifier que aplica efecto de pulsación durante escritura activa.
 * Anima la opacidad del componente en loop infinito.
 * 
 * @param isTyping Indica si el usuario está escribiendo activamente
 */
@Composable
fun Modifier.typingIndicator(isTyping: Boolean): Modifier = composed {
    val infiniteTransition = rememberInfiniteTransition(label = "typingIndicator")
    
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "typingAlpha"
    )
    
    val borderAlpha = if (isTyping) alpha else 1f
    
    this.graphicsLayer {
        this.alpha = borderAlpha
    }
}

// ============================================
// 7. CHARACTER COUNT ANIMATION
// ============================================

/**
 * Modifier que anima el contador de caracteres con transiciones de color y escala.
 * El color cambia progresivamente según proximidad al límite máximo.
 * 
 * @param currentLength Longitud actual del contenido
 * @param maxLength Límite máximo de caracteres
 */
@Composable
fun Modifier.characterCountAnimation(
    currentLength: Int,
    maxLength: Int
): Modifier = composed {
    val percentage = currentLength.toFloat() / maxLength.toFloat()
    
    val color by animateColorAsState(
        targetValue = when {
            percentage >= 1f -> Color.Red
            percentage >= 0.9f -> Color(0xFFFF9800) // Orange
            percentage >= 0.75f -> Color(0xFFFFC107) // Yellow
            else -> Color.Gray
        },
        animationSpec = tween(durationMillis = AnimationSpecs.DURATION_SHORT),
        label = "characterCountColor"
    )
    
    val scale by animateFloatAsState(
        targetValue = if (percentage >= 1f) 1.1f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy
        ),
        label = "characterCountScale"
    )
    
    this
        .scale(scale)
        .graphicsLayer { alpha = color.alpha }
}

// ============================================
// 8. COMBINED INPUT FIELD EFFECT
// ============================================

/**
 * Modifier que combina múltiples efectos de animación para campos de entrada.
 * Incluye animación de foco y sacudida de error.
 * 
 * @param isFocused Estado de foco del campo
 * @param errorMessage Mensaje de error actual o null
 * @param isSuccess Estado de validación exitosa
 */
@Composable
fun Modifier.animatedInputField(
    isFocused: Boolean,
    errorMessage: String? = null,
    isSuccess: Boolean = false
): Modifier {
    return this
        .inputFocusAnimation()
        .inputErrorShake(errorMessage)
}

// ============================================
// 9. PLACEHOLDER FADE ANIMATION
// ============================================

/**
 * Modifier que anima la opacidad del placeholder según presencia de contenido.
 * Transición suave de fade in/out.
 * 
 * @param hasContent Indica si el campo tiene contenido
 */
@Composable
fun Modifier.placeholderFade(hasContent: Boolean): Modifier {
    val alpha by animateFloatAsState(
        targetValue = if (hasContent) 0f else 0.5f,
        animationSpec = tween(durationMillis = AnimationSpecs.DURATION_SHORT),
        label = "placeholderAlpha"
    )
    
    return this.graphicsLayer { this.alpha = alpha }
}
