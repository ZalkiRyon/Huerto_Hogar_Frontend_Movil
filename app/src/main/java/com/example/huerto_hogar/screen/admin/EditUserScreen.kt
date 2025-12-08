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
import com.example.huerto_hogar.model.User
import com.example.huerto_hogar.repository.UserRepository
import com.example.huerto_hogar.utils.Resource
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope

/**
 * Formulario de edición de usuario desde el panel de administración
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditUserScreen(
    navController: NavController,
    userId: Int,
    userManager: UserManagerViewModel = viewModel()
) {
    val userRepository = remember { UserRepository() }
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    
    // Obtener el usuario a editar
    val userList by userManager.userList.collectAsState()
    val userToEdit = remember(userList, userId) {
        userList.find { it.id == userId }
    }
    
    // Estados del formulario inicializados con los datos del usuario
    var name by remember { mutableStateOf(userToEdit?.name ?: "") }
    var lastname by remember { mutableStateOf(userToEdit?.lastname ?: "") }
    var email by remember { mutableStateOf(userToEdit?.email ?: "") }
    var phone by remember { mutableStateOf(userToEdit?.phone ?: "") }
    var address by remember { mutableStateOf(userToEdit?.address ?: "") }
    var selectedRole by remember { mutableStateOf(userToEdit?.role ?: "cliente") }
    var comment by remember { mutableStateOf(userToEdit?.comment ?: "") }
    
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showRoleMenu by remember { mutableStateOf(false) }
    
    val scrollState = rememberScrollState()
    
    val roles = listOf(
        "cliente" to "Cliente",
        "vendedor" to "Vendedor",
        "admin" to "Administrador"
    )
    
    // Si no se encuentra el usuario, mostrar error
    if (userToEdit == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            Column(
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.error
                )
                Text(
                    text = "Usuario no encontrado",
                    style = MaterialTheme.typography.titleLarge
                )
                Button(onClick = { navController.navigateUp() }) {
                    Text("Volver")
                }
            }
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Usuario") },
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
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
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
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                enabled = false // Email no se puede cambiar
            )
            
            Text(
                text = "El email no se puede modificar",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Información de Contacto
            Text(
                text = "Información de Contacto",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
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
            
            // Botón Guardar Cambios
            Button(
                onClick = {
                    // Validaciones
                    when {
                        name.isBlank() -> errorMessage = "El nombre es requerido"
                        lastname.isBlank() -> errorMessage = "El apellido es requerido"
                        phone.isBlank() -> errorMessage = "El teléfono es requerido"
                        address.isBlank() -> errorMessage = "La dirección es requerida"
                        else -> {
                            errorMessage = null
                            isLoading = true
                            
                            // Mapear rol a role_id (1=admin, 2=cliente, 3=vendedor)
                            val roleId = when (selectedRole) {
                                "admin" -> 1
                                "cliente" -> 2
                                "vendedor" -> 3
                                else -> 2
                            }
                            
                            // Crear objeto User actualizado
                            val updatedUser = userToEdit.copy(
                                name = name.trim(),
                                lastname = lastname.trim(),
                                phone = phone.trim(),
                                address = address.trim(),
                                role = selectedRole,
                                comment = if (comment.isNotBlank()) comment.trim() else ""
                            )
                            
                            coroutineScope.launch {
                                userRepository.updateUser(userId, updatedUser, "").collect { resource ->
                                    when (resource) {
                                        is Resource.Loading -> {
                                            // Ya tenemos isLoading = true
                                        }
                                        is Resource.Success -> {
                                            isLoading = false
                                            snackbarHostState.showSnackbar(
                                                message = "Usuario actualizado exitosamente",
                                                duration = SnackbarDuration.Short
                                            )
                                            // Recargar lista de usuarios
                                            userManager.loadUsersFromBackend()
                                            // Volver a la pantalla anterior
                                            navController.navigateUp()
                                        }
                                        is Resource.Error -> {
                                            isLoading = false
                                            errorMessage = resource.message ?: "Error al actualizar usuario"
                                            snackbarHostState.showSnackbar(
                                                message = errorMessage ?: "Error desconocido",
                                                duration = SnackbarDuration.Long
                                            )
                                        }
                                    }
                                }
                            }
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
                    Icon(Icons.Default.Check, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Guardar Cambios", style = MaterialTheme.typography.titleMedium)
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
