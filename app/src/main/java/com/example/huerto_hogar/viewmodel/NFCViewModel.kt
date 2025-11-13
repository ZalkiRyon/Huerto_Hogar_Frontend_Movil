package com.example.huerto_hogar.viewmodel

import android.nfc.Tag
import androidx.lifecycle.ViewModel
import com.example.huerto_hogar.manager.NFCCardData
import com.example.huerto_hogar.manager.NFCManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * NFCViewModel - ViewModel para gestionar estado de lectura NFC
 * 
 * Responsabilidades:
 * - Mantener estado actual del proceso de escaneo NFC
 * - Procesar tags NFC detectados
 * - Manejar errores de disponibilidad y lectura
 * 
 * Nota: En esta implementación MVP, cualquier tarjeta NFC es aceptada.
 * Para producción, implementar validación contra backend o lista blanca.
 */
class NFCViewModel : ViewModel() {
    
    // Estado actual del proceso NFC (Idle, Scanning, Success, Error)
    private val _nfcState = MutableStateFlow<NFCState>(NFCState.Idle)
    val nfcState: StateFlow<NFCState> = _nfcState.asStateFlow()
    
    /**
     * Inicia el proceso de escaneo NFC
     * Cambia el estado a Scanning para mostrar UI apropiada
     */
    fun startScanning() {
        _nfcState.value = NFCState.Scanning
    }
    
    /**
     * Procesa un tag NFC detectado por el sistema
     * 
     * IMPLEMENTACIÓN MVP: Acepta cualquier tarjeta NFC como válida
     * 
     * Flujo:
     * 1. Cambia estado a Processing
     * 2. Extrae información del tag (UID, tipo, etc.)
     * 3. Valida tarjeta (en MVP siempre es válida si se lee correctamente)
     * 4. Cambia estado a Success o Error según resultado
     * 
     * @param tag Tag NFC detectado por Android
     * @param nfcManager Gestor NFC para procesar el tag
     * @return true si la tarjeta es válida, false en caso contrario
     */
    fun processNFCTag(tag: Tag, nfcManager: NFCManager): Boolean {
        return try {
            // Indicar que se está procesando la tarjeta
            _nfcState.value = NFCState.Processing
            
            // Extraer información del tag NFC
            val cardData = nfcManager.processTag(tag)
            
            // MVP: Si se pudo leer el UID, la tarjeta es válida
            // En producción aquí se validaría contra backend o lista blanca
            val isValid = cardData.uid.isNotEmpty()
            
            // Actualizar estado según resultado
            _nfcState.value = if (isValid) {
                NFCState.Success(cardData)
            } else {
                NFCState.Error("No se pudo leer la tarjeta")
            }
            
            isValid
        } catch (e: Exception) {
            // Manejar cualquier error en la lectura
            _nfcState.value = NFCState.Error("Error al leer tarjeta: ${e.message}")
            false
        }
    }
    
    /**
     * Maneja el caso cuando el dispositivo no tiene hardware NFC
     */
    fun handleNFCNotAvailable() {
        _nfcState.value = NFCState.Error("NFC no disponible en este dispositivo")
    }
    
    /**
     * Maneja el caso cuando NFC está deshabilitado en configuración
     */
    fun handleNFCDisabled() {
        _nfcState.value = NFCState.Error("Por favor habilita NFC en la configuración")
    }
    
    /**
     * Resetea el estado a Idle (estado inicial)
     */
    fun resetState() {
        _nfcState.value = NFCState.Idle
    }
}

/**
 * NFCState - Estados posibles del proceso de escaneo NFC
 * 
 * Sealed class para representar todos los estados posibles de forma type-safe
 */
sealed class NFCState {
    /** Estado inicial - Sin actividad NFC */
    object Idle : NFCState()
    
    /** Esperando detección de tarjeta NFC */
    object Scanning : NFCState()
    
    /** Procesando tarjeta detectada */
    object Processing : NFCState()
    
    /** Tarjeta leída y validada correctamente */
    data class Success(val cardData: NFCCardData) : NFCState()
    
    /** Error en lectura o validación */
    data class Error(val message: String) : NFCState()
}
