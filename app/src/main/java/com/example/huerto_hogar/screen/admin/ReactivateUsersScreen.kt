package com.example.huerto_hogar.screen.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.huerto_hogar.model.User
import com.example.huerto_hogar.repository.UserRepository
import com.example.huerto_hogar.ui.theme.components.ConfirmationDialog
import com.example.huerto_hogar.utils.Resource
import com.example.huerto_hogar.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReactivateUsersScreen(
    navController: NavController,
    userManager: UserViewModel = viewModel()
) {
    val scope = rememberCoroutineScope()
    val userRepository = remember { UserRepository() }
    
    var inactiveUsers by remember { mutableStateOf<List<User>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showReactivateDialog by remember { mutableStateOf(false) }
    var selectedUser by remember { mutableStateOf<User?>(null) }
    var snackbarMessage by remember { mutableStateOf<String?>(null) }
    
    val snackbarHostState = remember { SnackbarHostState() }
    
    // Cargar usuarios inactivos
    LaunchedEffect(Unit) {
        isLoading = true
        val token = userManager.getAuthToken() ?: ""
        userRepository.getAllUsersIncludingInactive(token).collect { resource ->
            when (resource) {
                is Resource.Loading -> isLoading = true
                is Resource.Success -> {
                    isLoading = false
                    // Filtrar solo usuarios inactivos
                    inactiveUsers = resource.data?.filter { !it.activo } ?: emptyList()
                }
                is Resource.Error -> {
                    isLoading = false
                    errorMessage = resource.message
                }
            }
        }
    }
    
    // Mostrar snackbar cuando hay mensaje
    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            snackbarMessage = null
        }
    }
    
    // Diálogo de confirmación
    ConfirmationDialog(
        showDialog = showReactivateDialog,
        onDismiss = { showReactivateDialog = false },
        title = "Reactivar Usuario",
        confirmButtonText = "Sí, reactivar",
        onConfirm = {
            showReactivateDialog = false
            selectedUser?.let { user ->
                scope.launch {
                    isLoading = true
                    val token = userManager.getAuthToken() ?: ""
                    userRepository.reactivateUser(user.id, token).collect { resource ->
                        when (resource) {
                            is Resource.Loading -> isLoading = true
                            is Resource.Success -> {
                                isLoading = false
                                snackbarMessage = "Usuario ${user.name} reactivado exitosamente"
                                // Recargar lista
                                userRepository.getAllUsersIncludingInactive(token).collect { res ->
                                    if (res is Resource.Success) {
                                        inactiveUsers = res.data?.filter { !it.activo } ?: emptyList()
                                    }
                                }
                            }
                            is Resource.Error -> {
                                isLoading = false
                                snackbarMessage = "Error: ${resource.message}"
                            }
                        }
                    }
                }
            }
        },
        message = "¿Estás seguro de que deseas reactivar al usuario ${selectedUser?.name} ${selectedUser?.lastname}?"
    )
    
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Reactivar Usuarios") },
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                errorMessage != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Error al cargar usuarios",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                        Text(
                            text = errorMessage ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                inactiveUsers.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = Color(0xFF4CAF50)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No hay usuarios inactivos",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(inactiveUsers) { user ->
                            InactiveUserCard(
                                user = user,
                                onReactivate = {
                                    selectedUser = user
                                    showReactivateDialog = true
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InactiveUserCard(
    user: User,
    onReactivate: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(
                            when (user.role) {
                                "admin" -> Color(0xFFFF5252)
                                "vendedor" -> Color(0xFF2196F3)
                                else -> Color(0xFF4CAF50)
                            }.copy(alpha = 0.2f)
                        )
                        .border(
                            2.dp,
                            when (user.role) {
                                "admin" -> Color(0xFFFF5252)
                                "vendedor" -> Color(0xFF2196F3)
                                else -> Color(0xFF4CAF50)
                            },
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (user.profilePictureUrl != null) {
                        AsyncImage(
                            model = user.profilePictureUrl,
                            contentDescription = "Avatar de ${user.name}",
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                        )
                    } else {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            tint = when (user.role) {
                                "admin" -> Color(0xFFFF5252)
                                "vendedor" -> Color(0xFF2196F3)
                                else -> Color(0xFF4CAF50)
                            },
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
                
                // Información del usuario
                Column {
                    Text(
                        text = "${user.name} ${user.lastname}",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = user.email,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Text(
                        text = user.role?.uppercase() ?: "CLIENTE",
                        style = MaterialTheme.typography.bodySmall,
                        color = when (user.role) {
                            "admin" -> Color(0xFFFF5252)
                            "vendedor" -> Color(0xFF2196F3)
                            else -> Color(0xFF4CAF50)
                        }
                    )
                }
            }
            
            // Botón de reactivar
            Button(
                onClick = onReactivate,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = "Reactivar",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Reactivar")
            }
        }
    }
}
