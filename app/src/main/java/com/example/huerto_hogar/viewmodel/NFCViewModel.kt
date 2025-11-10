package com.example.huerto_hogar.viewmodel

import android.nfc.Tag
import androidx.lifecycle.ViewModel
import com.example.huerto_hogar.manager.NFCCardData
import com.example.huerto_hogar.manager.NFCManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel para gestionar el estado de NFC.
 */
class NFCViewModel : ViewModel() {
    
    private val _nfcState = MutableStateFlow<NFCState>(NFCState.Idle)
    val nfcState: StateFlow<NFCState> = _nfcState.asStateFlow()
    
    private val _lastScannedCard = MutableStateFlow<NFCCardData?>(null)
    val lastScannedCard: StateFlow<NFCCardData?> = _lastScannedCard.asStateFlow()
    
    /**
     * Inicia el proceso de escaneo NFC.
     */
    fun startScanning() {
        _nfcState.value = NFCState.Scanning
    }
    
    /**
     * Procesa un tag NFC escaneado.
     */
    fun processNFCTag(tag: Tag, nfcManager: NFCManager): Boolean {
        return try {
            _nfcState.value = NFCState.Processing
            
            val cardData = nfcManager.processTag(tag)
            _lastScannedCard.value = cardData
            
            val isValid = nfcManager.isValidStudentCard(cardData.uid)
            
            _nfcState.value = if (isValid) {
                NFCState.Success(cardData)
            } else {
                NFCState.Error("Tarjeta no autorizada")
            }
            
            isValid
        } catch (e: Exception) {
            _nfcState.value = NFCState.Error("Error al leer tarjeta: ${e.message}")
            false
        }
    }
    
    /**
     * Maneja error cuando NFC no está disponible.
     */
    fun handleNFCNotAvailable() {
        _nfcState.value = NFCState.Error("NFC no disponible en este dispositivo")
    }
    
    /**
     * Maneja error cuando NFC está deshabilitado.
     */
    fun handleNFCDisabled() {
        _nfcState.value = NFCState.Error("Por favor habilita NFC en la configuración")
    }
    
    /**
     * Resetea el estado NFC.
     */
    fun resetState() {
        _nfcState.value = NFCState.Idle
    }
    
    /**
     * Cancela el escaneo.
     */
    fun cancelScanning() {
        _nfcState.value = NFCState.Idle
    }
}

/**
 * Estados posibles del proceso NFC.
 */
sealed class NFCState {
    object Idle : NFCState()
    object Scanning : NFCState()
    object Processing : NFCState()
    data class Success(val cardData: NFCCardData) : NFCState()
    data class Error(val message: String) : NFCState()
}
