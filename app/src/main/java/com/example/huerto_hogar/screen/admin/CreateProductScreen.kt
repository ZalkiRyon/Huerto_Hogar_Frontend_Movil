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
import com.example.huerto_hogar.utils.Resource
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope

/**
 * Formulario de creación de producto desde el panel de administración
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProductScreen(
    navController: NavController,
    productViewModel: ProductViewModel = viewModel()
) {
    val productRepository = remember { ProductRepository() }
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Producto") },
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
                            when {
                                selectedCategory.contains("Frutas") -> Icons.Default.Star
                                selectedCategory.contains("Verduras") -> Icons.Default.Star
                                selectedCategory.contains("lacteos") -> Icons.Default.Star
                                else -> Icons.Default.Star
                            },
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
                        // Solo permitir números
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
                        // Solo permitir números
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
            
            Text(
                text = "Si no se proporciona una imagen, se usará una por defecto",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Botón Crear
            Button(
                onClick = {
                    // Validaciones
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
                            
                            // El backend se encargará de generar el código secuencial automáticamente
                            val newProduct = Product(
                                id = 0,
                                name = name.trim(),
                                category = selectedCategory,
                                price = price.toDouble(),
                                stock = stock.toInt(),
                                description = description.trim(),
                                imageUrl = if (imageUrl.isNotBlank()) imageUrl.trim() else null
                            )
                            
                            coroutineScope.launch {
                                productRepository.createProduct(newProduct, "").collect { resource ->
                                    when (resource) {
                                        is Resource.Loading -> {
                                            // Ya tenemos isLoading = true
                                        }
                                        is Resource.Success -> {
                                            isLoading = false
                                            val createdProductName = resource.data?.name ?: name
                                            snackbarHostState.showSnackbar(
                                                message = "Producto \"$createdProductName\" creado exitosamente",
                                                duration = SnackbarDuration.Short
                                            )
                                            // Recargar lista de productos
                                            productViewModel.getAllProducts()
                                            // Volver a la pantalla anterior
                                            navController.navigateUp()
                                        }
                                        is Resource.Error -> {
                                            isLoading = false
                                            errorMessage = resource.message ?: "Error al crear producto"
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
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Crear Producto", style = MaterialTheme.typography.titleMedium)
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
