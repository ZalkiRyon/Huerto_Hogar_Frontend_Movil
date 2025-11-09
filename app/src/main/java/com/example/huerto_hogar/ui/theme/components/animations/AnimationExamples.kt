package com.example.huerto_hogar.ui.theme.components.animations

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

/**
 * Ejemplos de implementación de animaciones.
 * 
 * Contiene casos de uso demostrativos para cada tipo de animación disponible,
 * incluyendo botones, campos de entrada, transiciones y combinaciones complejas.
 */

// ============================================
// EJEMPLO 1: BOTONES CON ANIMACIONES
// ============================================

/**
 * Demostración de variantes de animación para botones.
 * Incluye press effects, bounce, shake, loading y estados disabled.
 */
@Composable
fun ButtonAnimationsExample() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Button Animations", style = MaterialTheme.typography.headlineSmall)
        
        // 1. Botón con efecto de press
        Button(
            onClick = { /* Acción */ },
            modifier = Modifier
                .fillMaxWidth()
                .pressClickEffect()
        ) {
            Text("Botón con Press Effect")
        }
        
        // 2. Botón con bounce al aparecer
        Button(
            onClick = { /* Acción */ },
            modifier = Modifier
                .fillMaxWidth()
                .bounceInEffect(delay = 100)
        ) {
            Text("Botón con Bounce In")
        }
        
        // 3. Botón con shake para errores
        var errorTrigger by remember { mutableStateOf(0) }
        val shakeController = rememberShakeController()
        
        Button(
            onClick = { 
                shakeController.shake() 
            },
            modifier = Modifier
                .fillMaxWidth()
                .shakeEffect(shakeController.trigger),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text("Botón que Shake (Click para ver)")
        }
        
        // 4. Botón con estado de loading
        var isLoading by remember { mutableStateOf(false) }
        
        Button(
            onClick = { 
                isLoading = true
                // Simular carga
            },
            modifier = Modifier
                .fillMaxWidth()
                .loadingPulseEffect(isLoading),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(if (isLoading) "Cargando..." else "Botón con Loading")
        }
        
        LaunchedEffect(isLoading) {
            if (isLoading) {
                delay(2000)
                isLoading = false
            }
        }
        
        // 5. Botón con disabled animation
        var isEnabled by remember { mutableStateOf(true) }
        
        Column {
            Button(
                onClick = { /* Acción */ },
                enabled = isEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .disabledStateEffect(isEnabled)
            ) {
                Text("Botón con Disabled State")
            }
            
            TextButton(onClick = { isEnabled = !isEnabled }) {
                Text(if (isEnabled) "Deshabilitar" else "Habilitar")
            }
        }
        
        // 6. Botón con todas las animaciones combinadas
        Button(
            onClick = { /* Acción */ },
            modifier = Modifier
                .fillMaxWidth()
                .animatedButton(enabled = true, bounceDelay = 0)
        ) {
            Text("Botón con Animación Completa")
        }
    }
}

// ============================================
// EJEMPLO 2: INPUT FIELDS CON ANIMACIONES
// ============================================

/**
 * Demostración de animaciones para campos de entrada.
 * Incluye focus, error shake, password visibility y contador de caracteres.
 */
@Composable
fun InputFieldAnimationsExample() {
    var email by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Input Field Animations", style = MaterialTheme.typography.headlineSmall)
        
        // 1. Input con focus animation
        OutlinedTextField(
            value = email,
            onValueChange = { 
                email = it
                // Validación simple
                emailError = if (it.isNotEmpty() && !it.contains("@")) {
                    "Email inválido"
                } else {
                    null
                }
            },
            label = { Text("Email con Focus Animation") },
            modifier = Modifier
                .fillMaxWidth()
                .inputFocusAnimation(),
            isError = emailError != null
        )
        
        // Mensaje de error con fade
        if (emailError != null) {
            Text(
                text = emailError!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
        
        // 2. Input con error shake
        var shakeText by remember { mutableStateOf("") }
        var shakeError by remember { mutableStateOf<String?>(null) }
        
        Column {
            OutlinedTextField(
                value = shakeText,
                onValueChange = { shakeText = it },
                label = { Text("Campo con Shake Error") },
                modifier = Modifier
                    .fillMaxWidth()
                    .inputErrorShake(shakeError),
                isError = shakeError != null
            )
            
            Button(
                onClick = {
                    if (shakeText.isEmpty()) {
                        shakeError = "Campo requerido"
                    } else {
                        shakeError = null
                    }
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Validar (shake si está vacío)")
            }
        }
        
        // 3. Password con toggle visibility animation
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password con Toggle Animado") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (passwordVisible) {
                androidx.compose.ui.text.input.VisualTransformation.None
            } else {
                androidx.compose.ui.text.input.PasswordVisualTransformation()
            },
            trailingIcon = {
                IconButton(
                    onClick = { passwordVisible = !passwordVisible },
                    modifier = Modifier.passwordVisibilityAnimation(passwordVisible)
                ) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Check else Icons.Default.Lock,
                        contentDescription = if (passwordVisible) "Ocultar" else "Mostrar"
                    )
                }
            }
        )
        
        // 4. Input con contador de caracteres animado
        val maxLength = 50
        var limitedText by remember { mutableStateOf("") }
        
        Column {
            OutlinedTextField(
                value = limitedText,
                onValueChange = { if (it.length <= maxLength) limitedText = it },
                label = { Text("Texto con Límite") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Text(
                text = "${limitedText.length}/$maxLength",
                modifier = Modifier
                    .align(Alignment.End)
                    .characterCountAnimation(limitedText.length, maxLength),
                style = MaterialTheme.typography.bodySmall
            )
        }
        
        // 5. Input field animado completo
        var username by remember { mutableStateOf("") }
        var isFocused by remember { mutableStateOf(false) }
        var usernameError by remember { mutableStateOf<String?>(null) }
        var isSuccess by remember { mutableStateOf(false) }
        
        OutlinedTextField(
            value = username,
            onValueChange = { 
                username = it
                // Validación simple
                when {
                    it.isEmpty() -> {
                        usernameError = "Campo requerido"
                        isSuccess = false
                    }
                    it.length < 3 -> {
                        usernameError = "Mínimo 3 caracteres"
                        isSuccess = false
                    }
                    else -> {
                        usernameError = null
                        isSuccess = true
                    }
                }
            },
            label = { Text("Username (Animación Completa)") },
            modifier = Modifier
                .fillMaxWidth()
                .animatedInputField(
                    isFocused = isFocused,
                    errorMessage = usernameError,
                    isSuccess = isSuccess
                ),
            isError = usernameError != null
        )
    }
}

// ============================================
// EJEMPLO 3: TRANSICIONES ENTRE PANTALLAS
// ============================================

/**
 * Demostración de transiciones con Crossfade.
 * Muestra cambio suave entre diferentes estados de contenido.
 */
@Composable
fun ScreenTransitionsExample() {
    var currentScreen by remember { mutableStateOf(0) }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Screen Transitions", style = MaterialTheme.typography.headlineSmall)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Botones para cambiar de pantalla
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { currentScreen = 0 }) {
                Text("Screen 1")
            }
            Button(onClick = { currentScreen = 1 }) {
                Text("Screen 2")
            }
            Button(onClick = { currentScreen = 2 }) {
                Text("Screen 3")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Crossfade entre diferentes screens
        CustomCrossfade(targetState = currentScreen) { screen ->
            when (screen) {
                0 -> ScreenContent("Pantalla 1", MaterialTheme.colorScheme.primaryContainer)
                1 -> ScreenContent("Pantalla 2", MaterialTheme.colorScheme.secondaryContainer)
                2 -> ScreenContent("Pantalla 3", MaterialTheme.colorScheme.tertiaryContainer)
            }
        }
    }
}

@Composable
fun ScreenContent(title: String, backgroundColor: androidx.compose.ui.graphics.Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = backgroundColor,
            shape = MaterialTheme.shapes.medium
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(title, style = MaterialTheme.typography.headlineMedium)
            }
        }
    }
}

// ============================================
// EJEMPLO 4: ANIMACIONES COMBINADAS EN LOGIN
// ============================================

/**
 * Demostración de implementación completa en pantalla de login.
 * Combina animaciones de entrada, validación, estados de carga y feedback visual.
 */
@Composable
fun LoginScreenAnimationExample() {
    var email by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var password by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Animated Login Screen",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.bounceInEffect(delay = 0)
        )
        
        // Email field con todas las animaciones
        OutlinedTextField(
            value = email,
            onValueChange = { 
                email = it
                emailError = when {
                    it.isEmpty() -> "Email requerido"
                    !it.contains("@") -> "Email inválido"
                    else -> null
                }
            },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .bounceInEffect(delay = 100)
                .inputFocusAnimation()
                .inputErrorShake(emailError),
            isError = emailError != null
        )
        
        if (emailError != null) {
            Text(
                text = emailError!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
        
        // Password field
        OutlinedTextField(
            value = password,
            onValueChange = { 
                password = it
                passwordError = if (it.length < 4 && it.isNotEmpty()) {
                    "Mínimo 4 caracteres"
                } else null
            },
            label = { Text("Contraseña") },
            modifier = Modifier
                .fillMaxWidth()
                .bounceInEffect(delay = 200)
                .inputFocusAnimation()
                .inputErrorShake(passwordError),
            visualTransformation = if (passwordVisible) {
                androidx.compose.ui.text.input.VisualTransformation.None
            } else {
                androidx.compose.ui.text.input.PasswordVisualTransformation()
            },
            isError = passwordError != null,
            trailingIcon = {
                IconButton(
                    onClick = { passwordVisible = !passwordVisible },
                    modifier = Modifier.passwordVisibilityAnimation(passwordVisible)
                ) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Check else Icons.Default.Lock,
                        contentDescription = null
                    )
                }
            }
        )
        
        if (passwordError != null) {
            Text(
                text = passwordError!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
        
        // Botón de login con animaciones
        Button(
            onClick = {
                if (email.isEmpty()) emailError = "Email requerido"
                if (password.isEmpty()) passwordError = "Contraseña requerida"
                
                if (emailError == null && passwordError == null) {
                    isLoading = true
                    // Simular login
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .bounceInEffect(delay = 300)
                .pressClickEffect()
                .loadingPulseEffect(isLoading),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(if (isLoading) "INGRESANDO..." else "INGRESAR")
        }
        
        LaunchedEffect(isLoading) {
            if (isLoading) {
                delay(2000)
                isLoading = false
            }
        }
    }
}

// ============================================
// PREVIEWS
// ============================================

@Preview(showBackground = true)
@Composable
fun ButtonAnimationsPreview() {
    MaterialTheme {
        Surface {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                ButtonAnimationsExample()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InputFieldAnimationsPreview() {
    MaterialTheme {
        Surface {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                InputFieldAnimationsExample()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScreenTransitionsPreview() {
    MaterialTheme {
        Surface {
            ScreenTransitionsExample()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginAnimationPreview() {
    MaterialTheme {
        Surface {
            LoginScreenAnimationExample()
        }
    }
}
