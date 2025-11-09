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
import com.example.huerto_hogar.model.Role
import com.example.huerto_hogar.model.User
import com.example.huerto_hogar.ui.theme.components.animations.bounceInEffect

// No necesitamos MockUsers aquí - usamos directamente UserManagerViewModel

/**
 * Pantalla de gestión de usuarios (display only por ahora).
 */
@Composable
fun AdminUsersScreen(
    navController: NavController,
    userManager: com.example.huerto_hogar.manager.UserManagerViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    var selectedRole by remember { mutableStateOf<Role?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    
    val userList by userManager.userList.collectAsState()
    
    val filteredUsers = remember(selectedRole, searchQuery, userList) {
        userList.filter { user ->
            val matchesRole = selectedRole == null || user.role == selectedRole
            val matchesSearch = searchQuery.isEmpty() || 
                user.name.contains(searchQuery, ignoreCase = true) ||
                user.lastname.contains(searchQuery, ignoreCase = true) ||
                user.email.contains(searchQuery, ignoreCase = true)
            matchesRole && matchesSearch
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
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
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 2
                )
                Text(
                    text = "${filteredUsers.size} usuarios",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            }
            
            Button(
                onClick = { /* TODO: Add user */ },
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
            
            Role.values().forEach { role ->
                FilterChip(
                    selected = selectedRole == role,
                    onClick = { selectedRole = role },
                    label = { 
                        Text(
                            when(role) {
                                Role.ADMIN -> "Admin"
                                Role.CLIENT -> "Cliente"
                                Role.SALESMAN -> "Vendedor"
                            }
                        ) 
                    }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Users List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredUsers) { user ->
                UserManagementCard(user = user)
            }
        }
    }
}

@Composable
fun UserManagementCard(user: User) {
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
                        Role.ADMIN -> Color(0xFFE91E63)
                        Role.SALESMAN -> Color(0xFF2196F3)
                        Role.CLIENT -> Color(0xFF4CAF50)
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
                            Role.ADMIN -> Color(0xFFE91E63).copy(alpha = 0.1f)
                            Role.SALESMAN -> Color(0xFF2196F3).copy(alpha = 0.1f)
                            Role.CLIENT -> Color(0xFF4CAF50).copy(alpha = 0.1f)
                        }
                    ) {
                        Text(
                            text = when(user.role) {
                                Role.ADMIN -> "Admin"
                                Role.CLIENT -> "Cliente"
                                Role.SALESMAN -> "Vendedor"
                            },
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = when(user.role) {
                                Role.ADMIN -> Color(0xFFE91E63)
                                Role.SALESMAN -> Color(0xFF2196F3)
                                Role.CLIENT -> Color(0xFF4CAF50)
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
                    onClick = { /* TODO: Edit */ },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                
                IconButton(
                    onClick = { /* TODO: Delete */ },
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
