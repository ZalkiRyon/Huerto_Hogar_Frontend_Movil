package com.example.huerto_hogar.utils

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

/**
 * Utilidades para manejo de archivos e imágenes
 */
object FileUtils {
    
    /**
     * Convierte una Uri de imagen a MultipartBody.Part para subir con Retrofit
     * 
     * @param context Contexto de la aplicación
     * @param uri Uri de la imagen seleccionada
     * @param partName Nombre del parámetro multipart (por defecto "file")
     * @return MultipartBody.Part listo para enviar
     */
    fun prepareImagePart(
        context: Context,
        uri: Uri,
        partName: String = "file"
    ): MultipartBody.Part? {
        return try {
            // Obtener nombre del archivo
            val fileName = getFileName(context, uri) ?: "image.jpg"
            
            // Crear archivo temporal
            val tempFile = createTempFileFromUri(context, uri, fileName)
            
            // Determinar tipo MIME
            val mimeType = context.contentResolver.getType(uri) ?: "image/*"
            
            // Crear RequestBody
            val requestBody = tempFile.asRequestBody(mimeType.toMediaTypeOrNull())
            
            // Crear MultipartBody.Part
            MultipartBody.Part.createFormData(partName, fileName, requestBody)
            
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    /**
     * Obtiene el nombre del archivo desde una Uri
     */
    private fun getFileName(context: Context, uri: Uri): String? {
        var fileName: String? = null
        
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (nameIndex != -1) {
                        fileName = it.getString(nameIndex)
                    }
                }
            }
        }
        
        if (fileName == null) {
            fileName = uri.path?.let { path ->
                val cut = path.lastIndexOf('/')
                if (cut != -1) path.substring(cut + 1) else path
            }
        }
        
        return fileName
    }
    
    /**
     * Crea un archivo temporal desde una Uri
     */
    private fun createTempFileFromUri(context: Context, uri: Uri, fileName: String): File {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw IllegalArgumentException("No se pudo abrir el archivo")
        
        val tempFile = File(context.cacheDir, fileName)
        
        FileOutputStream(tempFile).use { outputStream ->
            inputStream.copyTo(outputStream)
        }
        
        inputStream.close()
        
        return tempFile
    }
    
    /**
     * Valida si la Uri corresponde a una imagen
     */
    fun isImageUri(context: Context, uri: Uri): Boolean {
        val mimeType = context.contentResolver.getType(uri)
        return mimeType?.startsWith("image/") == true
    }
}
