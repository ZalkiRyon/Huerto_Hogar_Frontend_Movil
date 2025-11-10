package com.example.huerto_hogar.manager

import android.app.Activity
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.os.Build

/**
 * Manager para operaciones NFC.
 * Maneja lectura de tarjetas NFC y validación de UIDs.
 */
class NFCManager(private val activity: Activity) {
    
    private var nfcAdapter: NfcAdapter? = null
    
    init {
        nfcAdapter = NfcAdapter.getDefaultAdapter(activity)
    }
    
    /**
     * Verifica si el dispositivo tiene hardware NFC.
     */
    fun isNFCAvailable(): Boolean {
        return nfcAdapter != null
    }
    
    /**
     * Verifica si NFC está habilitado en el dispositivo.
     */
    fun isNFCEnabled(): Boolean {
        return nfcAdapter?.isEnabled == true
    }
    
    /**
     * Procesa un tag NFC leído.
     * Retorna el UID de la tarjeta como String hexadecimal.
     */
    fun processTag(tag: Tag): NFCCardData {
        val uid = bytesToHex(tag.id)
        val techList = tag.techList.toList()
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
     * Valida si un UID es válido para descuento estudiante.
     * MVP: Cualquier UID es válido (siempre retorna true).
     * Futuro: Validar contra lista blanca o backend.
     */
    fun isValidStudentCard(uid: String): Boolean {
        // MVP: Cualquier tarjeta NFC es válida
        return uid.isNotEmpty()
        
        // Futuro: Implementar validación real
        // return validUIDs.contains(uid) || validateWithBackend(uid)
    }
    
    /**
     * Convierte bytes a String hexadecimal.
     */
    private fun bytesToHex(bytes: ByteArray): String {
        val hexChars = "0123456789ABCDEF"
        val result = StringBuilder(bytes.size * 3)
        bytes.forEachIndexed { index, byte ->
            val value = byte.toInt() and 0xFF
            result.append(hexChars[value shr 4])
            result.append(hexChars[value and 0x0F])
            if (index < bytes.size - 1) result.append(":")
        }
        return result.toString()
    }
    
    /**
     * Obtiene información legible del adaptador NFC.
     */
    fun getNFCInfo(): String {
        return when {
            !isNFCAvailable() -> "Dispositivo sin NFC"
            !isNFCEnabled() -> "NFC deshabilitado"
            else -> "NFC disponible y habilitado"
        }
    }
}

/**
 * Datos capturados de una tarjeta NFC.
 */
data class NFCCardData(
    val uid: String,              // UID en formato hexadecimal (ej: "04:A1:B2:C3")
    val tech: List<String>,       // Tecnologías soportadas por la tarjeta
    val type: String,             // Tipo de tarjeta (NDEF, NfcA, etc.)
    val timestamp: Long           // Timestamp del escaneo
)
