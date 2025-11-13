package com.example.huerto_hogar.ui.theme.components.animations

import androidx.compose.animation.core.*
import androidx.compose.ui.unit.dp

/**
 * Especificaciones de animación globales.
 * Define constantes y configuraciones reutilizables para mantener consistencia
 * en duraciones, delays y funciones de easing a través de toda la aplicación.
 */
object AnimationSpecs {
    
    // ============================================
    // DURACIONES (en milisegundos)
    // ============================================
    const val DURATION_INSTANT = 100
    const val DURATION_SHORT = 200
    const val DURATION_MEDIUM = 300
    const val DURATION_LONG = 500
    const val DURATION_EXTRA_LONG = 800
    
    // ============================================
    // VALORES DE ESCALA
    // ============================================
    const val SCALE_PRESSED = 0.95f
    const val SCALE_NORMAL = 1f
    const val SCALE_SMALL = 0.8f
    
    // ============================================
    // VALORES DE ALPHA (NOT USED - kept for potential future use)
    // ============================================
    const val ALPHA_INVISIBLE = 0f
    const val ALPHA_SEMI_TRANSPARENT = 0.5f
    const val ALPHA_VISIBLE = 1f
    
    // ============================================
    // EASING FUNCTIONS (Curvas de animación)
    // ============================================
    val EaseInOutQuad = CubicBezierEasing(0.45f, 0.05f, 0.55f, 0.95f)
    val EaseOutBack = CubicBezierEasing(0.34f, 1.56f, 0.64f, 1f)
    
    // ============================================
    // SPECS PRE-CONFIGURADAS
    // ============================================
    
    /** Especificación optimizada para interacciones de botones con respuesta rápida */
    fun <T> buttonPressSpec(): FiniteAnimationSpec<T> = tween(
        durationMillis = DURATION_INSTANT,
        easing = FastOutSlowInEasing
    )
    
    /** Especificación para transiciones entre pantallas con movimiento suave */
    fun <T> screenTransitionSpec(): FiniteAnimationSpec<T> = tween(
        durationMillis = DURATION_MEDIUM,
        easing = EaseInOutQuad
    )
    
    /** Especificación con efecto de rebote elástico */
    fun <T> bounceSpec(): FiniteAnimationSpec<T> = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMediumLow
    )
}
