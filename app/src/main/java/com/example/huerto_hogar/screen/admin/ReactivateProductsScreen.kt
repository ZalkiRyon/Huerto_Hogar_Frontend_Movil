package com.example.huerto_hogar.screen.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.huerto_hogar.manager.UserManagerViewModel
import com.example.huerto_hogar.model.Product
import com.example.huerto_hogar.repository.ProductRepository
import com.example.huerto_hogar.ui.theme.components.ConfirmationDialog
import com.example.huerto_hogar.utils.Resource
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReactivateProductsScreen(
    navController: NavController,
    userManager: UserManagerViewModel = viewModel()
) {
    val scope = rememberCoroutineScope()
    val productRepository = remember { ProductRepository() }
    
    var inactiveProducts by remember { mutableStateOf<List<Product>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showReactivateDialog by remember { mutableStateOf(false) }
    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    var snackbarMessage by remember { mutableStateOf<String?>(null) }
    
    val snackbarHostState = remember { SnackbarHostState() }
    
    // Cargar productos inactivos
    LaunchedEffect(Unit) {
        isLoading = true
        productRepository.getAllProducts().collect { resource ->
            when (resource) {
                is Resource.Loading -> isLoading = true
                is Resource.Success -> {
                    isLoading = false
                    // Filtrar solo productos con stock 0 (considerados inactivos)
                    inactiveProducts = resource.data?.filter { it.stock == 0 } ?: emptyList()
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
        onConfirm = {
            showReactivateDialog = false
            selectedProduct?.let { product ->
                scope.launch {
                    isLoading = true
                    val token = userManager.getAuthToken() ?: ""
                    productRepository.reactivateProduct(product.id, token).collect { resource ->
                        when (resource) {
                            is Resource.Loading -> isLoading = true
                            is Resource.Success -> {
                                isLoading = false
                                snackbarMessage = "Producto ${product.name} reactivado exitosamente"
                                // Recargar lista
                                productRepository.getAllProducts().collect { res ->
                                    if (res is Resource.Success) {
                                        inactiveProducts = res.data?.filter { it.stock == 0 } ?: emptyList()
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
        message = "¿Estás seguro de que deseas reactivar el producto ${selectedProduct?.name}?"
    )
    
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Reactivar Productos") },
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
                            text = "Error al cargar productos",
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
                inactiveProducts.isEmpty() -> {
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
                            text = "No hay productos inactivos",
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
                        items(inactiveProducts) { product ->
                            InactiveProductCard(
                                product = product,
                                onReactivate = {
                                    selectedProduct = product
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
fun InactiveProductCard(
    product: Product,
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
                // Imagen del producto
                AsyncImage(
                    model = "http://10.0.2.2:8080/api/productos/imagenes/${product.imageUrl}",
                    contentDescription = product.name,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                
                // Información del producto
                Column {
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = product.category,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "$${product.price}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Text(
                        text = "Stock: ${product.stock}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFFF5252)
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
