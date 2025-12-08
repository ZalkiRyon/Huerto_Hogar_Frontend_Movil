package com.example.huerto_hogar.screen.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.huerto_hogar.viewmodel.ProductViewModel
import com.example.huerto_hogar.model.Product
import com.example.huerto_hogar.repository.ProductRepository
import com.example.huerto_hogar.manager.UserManagerViewModel
import com.example.huerto_hogar.utils.Resource
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductScreen(
    navController: NavController,
    productId: Int,
    productViewModel: ProductViewModel = viewModel(),
    userManager: UserManagerViewModel = viewModel()
) {
    val productRepository = remember { ProductRepository() }
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    
    var isLoadingProduct by remember { mutableStateOf(true) }
    var product by remember { mutableStateOf<Product?>(null) }
    
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Frutas frescas") }
    var imageUrl by remember { mutableStateOf("") }
    
    var showCategoryMenu by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    val scrollState = rememberScrollState()
    
    val categories = listOf(
        "Frutas frescas" to "FR",
        "Verduras organicas" to "VR",
        "Productos organicos" to "PO",
        "Productos lacteos" to "PL"
    )
    
    // Cargar producto al iniciar
    LaunchedEffect(productId) {
        isLoadingProduct = true
        productRepository.getProductById(productId).collect { resource ->
            when (resource) {
                is Resource.Loading -> isLoadingProduct = true
                is Resource.Success -> {
                    isLoadingProduct = false
                    resource.data?.let { prod ->
                        product = prod
                        // Extraer el nombre sin el prefijo si existe
                        val productName = if (prod.name.matches(Regex("^[A-Z]{2}\\d{3} - (.*)"))) {
                            prod.name.substringAfter(" - ")
                        } else {
                            prod.name
                        }
                        name = productName
                        description = prod.description
                        price = prod.price.toInt().toString()
                        stock = prod.stock.toString()
                        selectedCategory = prod.category
                        imageUrl = prod.imageUrl ?: ""
                    }
                }
                is Resource.Error -> {
                    isLoadingProduct = false
                    errorMessage = "Error al cargar el producto: ${resource.message}"
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Producto") },
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
        if (isLoadingProduct) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
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
                
                // Información Básica
                Text(
                    text = "Información del Producto",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre del Producto") },
                    leadingIcon = { Icon(Icons.Default.ShoppingCart, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    placeholder = { Text("Ej: Manzanas Fuji") }
                )
                
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción") },
                    leadingIcon = { Icon(Icons.Default.Info, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    minLines = 3,
                    maxLines = 5,
                    placeholder = { Text("Describe las características del producto") }
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Categoría
                Text(
                    text = "Categoría",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                
                ExposedDropdownMenuBox(
                    expanded = showCategoryMenu,
                    onExpandedChange = { showCategoryMenu = it }
                ) {
                    OutlinedTextField(
                        value = selectedCategory,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                if (showCategoryMenu) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = "Seleccionar categoría"
                            )
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                        label = { Text("Categoría del Producto") },
                        shape = RoundedCornerShape(12.dp)
                    )
                    
                    ExposedDropdownMenu(
                        expanded = showCategoryMenu,
                        onDismissRequest = { showCategoryMenu = false }
                    ) {
                        categories.forEach { (categoryName, _) ->
                            DropdownMenuItem(
                                text = { Text(categoryName) },
                                onClick = {
                                    selectedCategory = categoryName
                                    showCategoryMenu = false
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Star,
                                        contentDescription = null
                                    )
                                }
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Precio y Stock
                Text(
                    text = "Precio y Stock",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = price,
                        onValueChange = { 
                            if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                                price = it
                            }
                        },
                        label = { Text("Precio") },
                        leadingIcon = { Text("$", style = MaterialTheme.typography.titleMedium) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        placeholder = { Text("1200") }
                    )
                    
                    OutlinedTextField(
                        value = stock,
                        onValueChange = { 
                            if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                                stock = it
                            }
                        },
                        label = { Text("Stock") },
                        leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        placeholder = { Text("100") }
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Imagen (Opcional)
                Text(
                    text = "Imagen (Opcional)",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                
                OutlinedTextField(
                    value = imageUrl,
                    onValueChange = { imageUrl = it },
                    label = { Text("URL de la imagen") },
                    leadingIcon = { Icon(Icons.Default.Share, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    placeholder = { Text("https://ejemplo.com/imagen.jpg") }
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Botón Guardar
                Button(
                    onClick = {
                        when {
                            name.isBlank() -> errorMessage = "El nombre del producto es requerido"
                            name.length < 3 -> errorMessage = "El nombre debe tener al menos 3 caracteres"
                            description.isBlank() -> errorMessage = "La descripción es requerida"
                            description.length < 10 -> errorMessage = "La descripción debe tener al menos 10 caracteres"
                            price.isBlank() -> errorMessage = "El precio es requerido"
                            price.toIntOrNull() == null || price.toInt() <= 0 -> errorMessage = "El precio debe ser mayor a 0"
                            stock.isBlank() -> errorMessage = "El stock es requerido"
                            stock.toIntOrNull() == null || stock.toInt() < 0 -> errorMessage = "El stock debe ser mayor o igual a 0"
                            else -> {
                                errorMessage = null
                                isLoading = true
                                
                                val updatedProduct = Product(
                                    id = productId,
                                    name = name,
                                    category = selectedCategory,
                                    price = price.toDouble(),
                                    stock = stock.toInt(),
                                    description = description.trim(),
                                    imageUrl = if (imageUrl.isNotBlank()) imageUrl.trim() else null
                                )
                                
                                coroutineScope.launch {
                                    val token = userManager.getAuthToken() ?: ""
                                    productRepository.updateProduct(productId, updatedProduct, token).collect { resource ->
                                        when (resource) {
                                            is Resource.Loading -> {
                                                // isLoading ya es true
                                            }
                                            is Resource.Success -> {
                                                isLoading = false
                                                snackbarHostState.showSnackbar(
                                                    message = "Producto actualizado exitosamente",
                                                    duration = SnackbarDuration.Short
                                                )
                                                productViewModel.getAllProducts()
                                                navController.navigateUp()
                                            }
                                            is Resource.Error -> {
                                                isLoading = false
                                                errorMessage = resource.message ?: "Error al actualizar producto"
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
                        Icon(Icons.Default.Edit, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Guardar Cambios", style = MaterialTheme.typography.titleMedium)
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
