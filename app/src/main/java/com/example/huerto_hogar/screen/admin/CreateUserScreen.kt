package com.example.huerto_hogar.screen.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.huerto_hogar.manager.UserManagerViewModel

/**
 * Formulario de creación de usuario desde el panel de administración
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateUserScreen(
    navController: NavController,
    userManager: UserManagerViewModel = viewModel()
) {
    var name by remember { mutableStateOf("") }
    var lastname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var run by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var region by remember { mutableStateOf("") }
    var comuna by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf("cliente") }
    var comment by remember { mutableStateOf("") }
    
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showRoleMenu by remember { mutableStateOf(false) }
    
    val scrollState = rememberScrollState()
    
    val roles = listOf(
        "cliente" to "Cliente",
        "vendedor" to "Vendedor",
        "admin" to "Administrador"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Usuario") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Error message
            if (errorMessage != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = errorMessage ?: "",
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
            
            // Tipo de Usuario
            Text(
                text = "Tipo de Usuario",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            
            ExposedDropdownMenuBox(
                expanded = showRoleMenu,
                onExpandedChange = { showRoleMenu = it }
            ) {
                OutlinedTextField(
                    value = roles.find { it.first == selectedRole }?.second ?: "Cliente",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            if (showRoleMenu) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = "Seleccionar rol"
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                    label = { Text("Rol") },
                    shape = RoundedCornerShape(12.dp)
                )
                
                ExposedDropdownMenu(
                    expanded = showRoleMenu,
                    onDismissRequest = { showRoleMenu = false }
                ) {
                    roles.forEach { (roleValue, roleLabel) ->
                        DropdownMenuItem(
                            text = { Text(roleLabel) },
                            onClick = {
                                selectedRole = roleValue
                                showRoleMenu = false
                            }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Información Personal
            Text(
                text = "Información Personal",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
            
            OutlinedTextField(
                value = lastname,
                onValueChange = { lastname = it },
                label = { Text("Apellido") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
            
            OutlinedTextField(
                value = run,
                onValueChange = { run = it },
                label = { Text("RUN (Ej: 12.345.678-9)") },
                leadingIcon = { Icon(Icons.Default.Info, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Información de Contacto
            Text(
                text = "Información de Contacto",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
            
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Teléfono") },
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Contraseña
            Text(
                text = "Contraseña",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            if (showPassword) Icons.Default.Close else Icons.Default.Info,
                            contentDescription = if (showPassword) "Ocultar" else "Mostrar"
                        )
                    }
                },
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
            
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmar Contraseña") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                trailingIcon = {
                    IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                        Icon(
                            if (showConfirmPassword) Icons.Default.Close else Icons.Default.Info,
                            contentDescription = if (showConfirmPassword) "Ocultar" else "Mostrar"
                        )
                    }
                },
                visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Dirección
            Text(
                text = "Dirección",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            
            OutlinedTextField(
                value = region,
                onValueChange = { region = it },
                label = { Text("Región") },
                leadingIcon = { Icon(Icons.Default.Place, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
            
            OutlinedTextField(
                value = comuna,
                onValueChange = { comuna = it },
                label = { Text("Comuna") },
                leadingIcon = { Icon(Icons.Default.Place, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
            
            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Dirección") },
                leadingIcon = { Icon(Icons.Default.Home, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Comentarios (Opcional)
            Text(
                text = "Comentarios (Opcional)",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            
            OutlinedTextField(
                value = comment,
                onValueChange = { comment = it },
                label = { Text("Comentarios adicionales") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                minLines = 3,
                maxLines = 5
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Botón Crear
            Button(
                onClick = {
                    // Validaciones
                    when {
                        name.isBlank() -> errorMessage = "El nombre es requerido"
                        lastname.isBlank() -> errorMessage = "El apellido es requerido"
                        email.isBlank() -> errorMessage = "El email es requerido"
                        !email.contains("@") -> errorMessage = "Email inválido"
                        password.isBlank() -> errorMessage = "La contraseña es requerida"
                        password.length < 6 -> errorMessage = "La contraseña debe tener al menos 6 caracteres"
                        password != confirmPassword -> errorMessage = "Las contraseñas no coinciden"
                        phone.isBlank() -> errorMessage = "El teléfono es requerido"
                        address.isBlank() -> errorMessage = "La dirección es requerida"
                        else -> {
                            // TODO: Implementar creación de usuario con API
                            errorMessage = null
                            isLoading = true
                            // Por ahora solo navegamos de vuelta
                            navController.navigateUp()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Crear Usuario", style = MaterialTheme.typography.titleMedium)
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
