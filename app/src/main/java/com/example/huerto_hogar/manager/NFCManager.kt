package com.example.huerto_hogar.manager

import android.app.Activity
import android.nfc.NfcAdapter
import android.nfc.Tag

/**
 * NFCManager - Gestor centralizado de operaciones NFC
 * 
 * Responsabilidades:
 * - Verificar disponibilidad y estado del hardware NFC
 * - Procesar tags NFC y extraer información del UID
 * - Convertir datos binarios a formato legible
 * 
 * Nota: Esta implementación es una prueba conceptual (MVP).
 * En producción debería implementarse validación de tarjetas contra backend.
 */
class NFCManager(private val activity: Activity) {
    
    // Adaptador NFC del sistema Android
    private var nfcAdapter: NfcAdapter? = null
    
    init {
        // Obtener adaptador NFC del dispositivo (null si no tiene hardware NFC)
        nfcAdapter = NfcAdapter.getDefaultAdapter(activity)
    }
    
    /**
     * Verifica si el dispositivo tiene hardware NFC
     * @return true si el dispositivo soporta NFC, false en caso contrario
     */
    fun isNFCAvailable(): Boolean = nfcAdapter != null
    
    /**
     * Verifica si NFC está habilitado en la configuración del dispositivo
     * @return true si NFC está activado, false si está desactivado o no disponible
     */
    fun isNFCEnabled(): Boolean = nfcAdapter?.isEnabled == true
    
    /**
     * Procesa un tag NFC detectado y extrae su información
     * 
     * @param tag Tag NFC detectado por el sistema
     * @return NFCCardData con información del tag (UID, tipo, tecnologías, timestamp)
     */
    fun processTag(tag: Tag): NFCCardData {
        // Convertir UID binario a formato hexadecimal legible (ej: "04:A1:B2:C3")
        val uid = bytesToHex(tag.id)
        
        // Obtener lista de tecnologías NFC soportadas por el tag
        val techList = tag.techList.toList()
        
        // Determinar tipo principal del tag basado en tecnologías disponibles
        val type = when {
            techList.contains("android.nfc.tech.Ndef") -> "NDEF"
            techList.contains("android.nfc.tech.NfcA") -> "NfcA"
            techList.contains("android.nfc.tech.NfcB") -> "NfcB"
            techList.contains("android.nfc.tech.NfcF") -> "NfcF"
            techList.contains("android.nfc.tech.NfcV") -> "NfcV"
            else -> "Unknown"
        }
        
        return NFCCardData(
            uid = uid,
            tech = techList,
            type = type,
            timestamp = System.currentTimeMillis()
        )
    }
    
    /**
     * Convierte array de bytes a string hexadecimal con formato separado por ":"
     * Ejemplo: [0x04, 0xA1, 0xB2] -> "04:A1:B2"
     * 
     * @param bytes Array de bytes a convertir
     * @return String hexadecimal formateado
     */
    private fun bytesToHex(bytes: ByteArray): String {
        val hexChars = "0123456789ABCDEF"
        val result = StringBuilder(bytes.size * 3)
        
        bytes.forEachIndexed { index, byte ->
            val value = byte.toInt() and 0xFF  // Convertir a unsigned
            result.append(hexChars[value shr 4])  // Nibble alto
            result.append(hexChars[value and 0x0F])  // Nibble bajo
            if (index < bytes.size - 1) result.append(":")
        }
        
        return result.toString()
    }
}

/**
 * NFCCardData - Modelo de datos para tarjetas NFC escaneadas
 * 
 * @property uid Identificador único de la tarjeta en formato hexadecimal (ej: "04:A1:B2:C3")
 * @property tech Lista de tecnologías NFC soportadas por la tarjeta
 * @property type Tipo principal de la tarjeta (NDEF, NfcA, etc.)
 * @property timestamp Momento del escaneo (milisegundos desde epoch)
 */
data class NFCCardData(
    val uid: String,
    val tech: List<String>,
    val type: String,
    val timestamp: Long
)
