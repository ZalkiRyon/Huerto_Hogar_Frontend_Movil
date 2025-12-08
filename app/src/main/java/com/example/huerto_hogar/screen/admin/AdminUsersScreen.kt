package com.example.huerto_hogar.screen.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.huerto_hogar.manager.UserManagerViewModel
import com.example.huerto_hogar.model.User
import com.example.huerto_hogar.repository.UserRepository
import com.example.huerto_hogar.ui.theme.components.animations.bounceInEffect
import com.example.huerto_hogar.ui.theme.components.ConfirmationDialog
import com.example.huerto_hogar.utils.Resource
import kotlinx.coroutines.launch

// No necesitamos MockUsers aquí - usamos directamente UserManagerViewModel

/**
 * Pantalla de gestión de usuarios (display only por ahora).
 */
@Composable
fun AdminUsersScreen(
    navController: NavController,
    userManager: com.example.huerto_hogar.manager.UserManagerViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    var selectedRole by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    
    val userList by userManager.userList.collectAsState()
    val currentUser by userManager.currentUser.collectAsState()
    
    // Estados para el modal de confirmación de eliminación
    var showDeleteDialog by remember { mutableStateOf(false) }
    var userToDelete by remember { mutableStateOf<User?>(null) }
    var isDeleting by remember { mutableStateOf(false) }
    
    val userRepository = remember { UserRepository() }
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    
    val filteredUsers = remember(selectedRole, searchQuery, userList, currentUser) {
        userList.filter { user ->
            // Excluir al usuario loggeado para evitar auto-modificación/eliminación
            val isNotCurrentUser = currentUser?.id != user.id
            val matchesRole = selectedRole == null || user.role == selectedRole
            val matchesSearch = searchQuery.isEmpty() || 
                user.name.contains(searchQuery, ignoreCase = true) ||
                user.lastname.contains(searchQuery, ignoreCase = true) ||
                user.email.contains(searchQuery, ignoreCase = true)
            isNotCurrentUser && matchesRole && matchesSearch
        }
    }
    
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(16.dp)
        ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Gestión de Usuarios",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 2
                )
                Text(
                    text = "${filteredUsers.size} usuarios",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            }
            
            Button(
                onClick = { 
                    navController.navigate("createUser")
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier.bounceInEffect()
            ) {
                Icon(Icons.Default.Add, contentDescription = "Crear", modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Crear")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Buscar usuarios...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Role Filters
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedRole == null,
                onClick = { selectedRole = null },
                label = { Text("Todos") }
            )
            
            listOf("admin" to "Admin", "cliente" to "Cliente", "vendedor" to "Vendedor").forEach { (roleValue, roleLabel) ->
                FilterChip(
                    selected = selectedRole == roleValue,
                    onClick = { selectedRole = roleValue },
                    label = { Text(roleLabel) }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Users List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredUsers) { user ->
                UserManagementCard(
                    user = user,
                    navController = navController,
                    onDeleteClick = {
                        userToDelete = user
                        showDeleteDialog = true
                    }
                )
            }
        }
        }
        
        // Modal de confirmación de eliminación
        ConfirmationDialog(
            showDialog = showDeleteDialog,
            onDismiss = {
                showDeleteDialog = false
                userToDelete = null
            },
            onConfirm = {
                userToDelete?.let { user ->
                    isDeleting = true
                    coroutineScope.launch {
                        userRepository.deleteUser(user.id, "").collect { resource ->
                            when (resource) {
                                is Resource.Loading -> {
                                    // Mantener isDeleting = true
                                }
                                is Resource.Success -> {
                                    isDeleting = false
                                    showDeleteDialog = false
                                    snackbarHostState.showSnackbar(
                                        message = "Usuario ${user.name} ${user.lastname} eliminado exitosamente",
                                        duration = SnackbarDuration.Short
                                    )
                                    // Recargar lista de usuarios
                                    userManager.loadUsersFromBackend()
                                    userToDelete = null
                                }
                                is Resource.Error -> {
                                    isDeleting = false
                                    showDeleteDialog = false
                                    snackbarHostState.showSnackbar(
                                        message = resource.message ?: "Error al eliminar usuario",
                                        duration = SnackbarDuration.Long
                                    )
                                    userToDelete = null
                                }
                            }
                        }
                    }
                }
            },
            title = "Eliminar Usuario",
            message = "¿Estás seguro de que deseas eliminar a ${userToDelete?.name} ${userToDelete?.lastname}? Esta acción no se puede deshacer.",
            confirmButtonText = if (isDeleting) "Eliminando..." else "Sí, eliminar",
            dismissButtonText = "Cancelar"
        )
    }
}

@Composable
fun UserManagementCard(
    user: User,
    navController: NavController,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Avatar
                Surface(
                    modifier = Modifier.size(48.dp),
                    shape = CircleShape,
                    color = when(user.role) {
                        "admin" -> Color(0xFFFF5252) // Rojo para admin
                        "vendedor" -> MaterialTheme.colorScheme.secondary
                        "cliente" -> MaterialTheme.colorScheme.tertiary
                        else -> MaterialTheme.colorScheme.tertiary
                    }
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = "${user.name.first()}${user.lastname.first()}",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                    }
                }
                
                Column {
                    Text(
                        text = "${user.name} ${user.lastname}",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    
                    Text(
                        text = user.email,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    // Role Badge
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = when(user.role) {
                            "admin" -> MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
                            "vendedor" -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
                            "cliente" -> MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f)
                            else -> MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f)
                        }
                    ) {
                        Text(
                            text = when(user.role) {
                                "admin" -> "Admin"
                                "cliente" -> "Cliente"
                                "vendedor" -> "Vendedor"
                                else -> "Cliente"
                            },
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = when(user.role) {
                                "admin" -> MaterialTheme.colorScheme.error
                                "vendedor" -> MaterialTheme.colorScheme.secondary
                                "cliente" -> MaterialTheme.colorScheme.tertiary
                                else -> MaterialTheme.colorScheme.tertiary
                            },
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
            
            // Action Buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = { 
                        navController.navigate("editUser/${user.id}")
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color(0xFFFFC107).copy(alpha = 0.2f) // Amarillo/amber
                    )
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = Color(0xFFFFC107)
                    )
                }
                
                IconButton(
                    onClick = onDeleteClick,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color.Red.copy(alpha = 0.1f)
                    )
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = Color.Red
                    )
                }
            }
        }
    }
}
