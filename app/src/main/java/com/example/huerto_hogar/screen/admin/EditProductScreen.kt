package com.example.huerto_hogar.screen.admin

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.huerto_hogar.data.di.NetworkModule
import com.example.huerto_hogar.viewmodel.ProductViewModel
import com.example.huerto_hogar.model.Product
import com.example.huerto_hogar.repository.ProductRepository
import com.example.huerto_hogar.ui.theme.components.ConfirmationDialog
import com.example.huerto_hogar.utils.FileUtils
import com.example.huerto_hogar.utils.Resource
import com.example.huerto_hogar.viewmodel.UserViewModel
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductScreen(
    navController: NavController,
    productId: Int,
    productViewModel: ProductViewModel = viewModel(),
    userManager: UserViewModel = viewModel()
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
    var productImageUri by remember { mutableStateOf<Uri?>(null) }
    var cameraImageUri by remember { mutableStateOf<Uri?>(null) }
    var showSourceDialog by remember { mutableStateOf(false) }
    var uploadingImage by remember { mutableStateOf(false) }
    
    var showCategoryMenu by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    val context = LocalContext.current
    val productApiService = NetworkModule.productApiService
    
    val scrollState = rememberScrollState()
    
    // Launcher para galería
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            productImageUri = it
            // Subir imagen a Cloudinary
            uploadingImage = true
            coroutineScope.launch {
                try {
                    val imagePart = FileUtils.prepareImagePart(context, it, "file")
                    if (imagePart != null) {
                        val result = productApiService.uploadProductImage(productId, imagePart)
                        if (result.isSuccessful && result.body() != null) {
                            imageUrl = result.body()!!
                            Toast.makeText(context, "Imagen subida exitosamente", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Error al subir imagen", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Error al procesar la imagen", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                } finally {
                    uploadingImage = false
                }
            }
        }
    }

    // Launcher para cámara
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success) {
            cameraImageUri?.let {
                productImageUri = it
                // Subir imagen a Cloudinary
                uploadingImage = true
                coroutineScope.launch {
                    try {
                        val imagePart = FileUtils.prepareImagePart(context, it, "file")
                        if (imagePart != null) {
                            val result = productApiService.uploadProductImage(productId, imagePart)
                            if (result.isSuccessful && result.body() != null) {
                                imageUrl = result.body()!!
                                Toast.makeText(context, "Imagen subida exitosamente", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Error al subir imagen", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, "Error al procesar la imagen", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    } finally {
                        uploadingImage = false
                    }
                }
            }
        }
    }

    // Launcher para permisos de cámara
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            cameraImageUri?.let { uri -> cameraLauncher.launch(uri) }
        } else {
            Toast.makeText(context, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }
    }

    fun launchCameraFlow() {
        val newPhotoFile = File.createTempFile(
            "product_${System.currentTimeMillis()}",
            ".jpg",
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        )

        val newUri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            newPhotoFile
        )

        cameraImageUri = newUri

        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            cameraLauncher.launch(newUri)
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }
    
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
                    text = "Imagen del Producto (Opcional)",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
                            .clickable { showSourceDialog = true },
                        contentAlignment = Alignment.Center
                    ) {
                        if (productImageUri != null || imageUrl.isNotBlank()) {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    model = productImageUri ?: imageUrl
                                ),
                                contentDescription = "Imagen del producto",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Agregar imagen",
                                modifier = Modifier.size(80.dp),
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                            )
                        }
                        
                        // Indicador de carga
                        if (uploadingImage) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(40.dp),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                    
                    Spacer(Modifier.height(8.dp))
                    
                    Text(
                        text = when {
                            uploadingImage -> "Subiendo imagen..."
                            productImageUri != null || imageUrl.isNotBlank() -> "Toca para cambiar imagen"
                            else -> "Toca para agregar imagen"
                        },
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    Text(
                        text = "Si no se proporciona una imagen, se usará la imagen por defecto",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }
                
                // Diálogo de selección de fuente
                if (showSourceDialog) {
                    ConfirmationDialog(
                        showDialog = showSourceDialog,
                        title = "Seleccionar Imagen",
                        message = "¿De dónde deseas obtener la imagen?",
                        onConfirm = {
                            showSourceDialog = false
                            galleryLauncher.launch("image/*")
                        },
                        onDismiss = {
                            showSourceDialog = false
                            launchCameraFlow()
                        },
                        confirmButtonText = "Galería",
                        dismissButtonText = "Cámara"
                    )
                }
                
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
