package com.example.huerto_hogar.screen

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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.huerto_hogar.ui.theme.components.InputField
import com.example.huerto_hogar.ui.theme.components.ConfirmationDialog
import com.example.huerto_hogar.ui.theme.components.animations.bounceInEffect
import com.example.huerto_hogar.viewmodel.UserSettingsViewModel
import java.io.File

@Composable
fun UsSetScreen(navController: NavController, viewModel: UserSettingsViewModel) {

    val formState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val showSourceDialog = remember { mutableStateOf(false) }
    val profileUri by viewModel.profilePictureUri.collectAsState()

    var isCameraPermissionGranted by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadUserProfile()
    }


    LaunchedEffect(viewModel.saveResult) {
        viewModel.saveResult.collect { success ->
            if (success) {
                Toast.makeText(context, "¡Perfil actualizado con éxito!", Toast.LENGTH_SHORT).show()
            } else {

                if (formState.errors.errors == null) {
                    Toast.makeText(
                        context,
                        "Error al guardar los cambios. Intente de nuevo.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    if (!formState.isInitialLoadComplete) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val photoFile: File = remember {
        File.createTempFile(
            "profile_pic",
            ".jpg",
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        )
    }

    val uri: Uri = remember {
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            photoFile
        )
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.setProfilePictureUri(it) }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success) {
            viewModel.setProfilePictureUri(uri)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        isCameraPermissionGranted = isGranted
        if (isGranted) {
            cameraLauncher.launch(uri)
        }
    }

    fun launchCameraFlow() {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            cameraLauncher.launch(uri)
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = "Configuración y Datos de Cuenta",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Foto de perfil",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Box(
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                .clickable { showSourceDialog.value = true },
            contentAlignment = Alignment.Center
        ) {

            if (profileUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(model = profileUri),
                    contentDescription = "Foto de perfil",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(
                    Icons.Default.AccountCircle,
                    contentDescription = "Sin foto de perfil",
                    modifier = Modifier.size(100.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Toca para cambiar la foto",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        if (showSourceDialog.value) {
            ConfirmationDialog(
                showDialog = showSourceDialog.value,
                title = "Seleccionar Foto",
                message = "¿Desde dónde quieres obtener la foto de perfil?",
                onConfirm = {
                    showSourceDialog.value = false
                    launchCameraFlow()
                },
                onDismiss = {
                    showSourceDialog.value = false
                    galleryLauncher.launch("image/*")
                },
                confirmButtonText = "Cámara",
                dismissButtonText = "Galería"
            )
        }

        Text(
            text = "Información Personal",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(
            modifier = Modifier.height(4.dp)
        )

        InputField(
            value = formState.name,
            onValueChange = viewModel::onChangeName,
            label = "Nombre",
            modifier = Modifier,
            error = formState.errors.nameError
        )
        InputField(
            value = formState.lastname,
            onValueChange = viewModel::onChangeLastname,
            label = "Apellido",
            modifier = Modifier,
            error = formState.errors.lastnameError
        )
        InputField(
            value = formState.email,
            onValueChange = viewModel::onChangeEmail,
            label = "Correo Electrónico",
            modifier = Modifier,
            error = formState.errors.emailError
        )
        InputField(
            value = formState.address,
            onValueChange = viewModel::onChangeAddress,
            label = "Dirección",
            modifier = Modifier,
            error = formState.errors.addressError
        )
        InputField(
            value = formState.phone,
            onValueChange = viewModel::onChangePhone,
            label = "Teléfono (Opcional)",
            modifier = Modifier,
            error = formState.errors.phoneError
        )

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 16.dp),
            thickness = DividerDefaults.Thickness,
            color = DividerDefaults.color
        )


        Text(
            text = "Cambiar Contraseña (Llenar para modificar)",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.align(Alignment.Start)
        )

        InputField(
            value = formState.currentPassword,
            onValueChange = viewModel::onChangeCurrentPassword,
            label = "Contraseña Actual",
            modifier = Modifier,
            error = formState.errors.currentPasswordError,
            isPassword = true
        )

        if (formState.currentPassword.isNotBlank()) {
            InputField(
                value = formState.newPassword,
                onValueChange = viewModel::onChangeNewPassword,
                label = "Nueva Contraseña (Mín. 4 caracteres)",
                modifier = Modifier,
                error = formState.errors.newPasswordError,
                isPassword = true
            )
            InputField(
                value = formState.confirmNewPassword,
                onValueChange = viewModel::onChangeConfirmNewPassword,
                label = "Confirmar Nueva Contraseña",
                modifier = Modifier,
                error = formState.errors.confirmNewPasswordError,
                isPassword = true
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (formState.errors.errors != null) {
            Text(
                text = formState.errors.errors!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
        }

        Button(
            onClick = viewModel::onClickSave,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .bounceInEffect(),
            enabled = !formState.isLoading,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            if (formState.isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text(
                    "Guardar Cambios",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
