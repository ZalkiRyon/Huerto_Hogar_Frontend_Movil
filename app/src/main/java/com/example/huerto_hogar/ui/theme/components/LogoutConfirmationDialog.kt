package com.example.huerto_hogar.ui.theme.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Diálogo de confirmación de logout reutilizable.
 * 
 * @param showDialog Estado que controla la visibilidad del diálogo
 * @param onDismiss Callback cuando se cancela el diálogo
 * @param onConfirm Callback cuando se confirma el logout
 * @param title Título del diálogo (por defecto "Cerrar Sesión")
 * @param message Mensaje descriptivo del diálogo
 * @param icon Icono a mostrar en el diálogo
 * @param confirmButtonText Texto del botón de confirmación
 * @param dismissButtonText Texto del botón de cancelación
 */
@Composable
fun LogoutConfirmationDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    title: String = "Cerrar Sesión",
    message: String = "¿Estás seguro de que deseas cerrar sesión?",
    icon: ImageVector = Icons.Default.ExitToApp,
    confirmButtonText: String = "Sí, cerrar sesión",
    dismissButtonText: String = "Cancelar"
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            icon = {
                Icon(
                    imageVector = icon,
                    contentDescription = title
                )
            },
            title = {
                Text(text = title)
            },
            text = {
                Text(text = message)
            },
            confirmButton = {
                Button(
                    onClick = onConfirm,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(confirmButtonText)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onDismiss
                ) {
                    Text(dismissButtonText)
                }
            }
        )
    }
}
