package com.example.huerto_hogar

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.huerto_hogar.ui.theme.Huerto_HogarTheme
import com.example.huerto_hogar.ui.theme.components.AppNavigationContainer

/**
 * MainActivity - Actividad principal de la aplicación
 * 
 * Responsabilidades NFC:
 * - Configurar foreground dispatch de NFC cuando la app está activa
 * - Interceptar tags NFC detectados por el sistema
 * - Comunicar tags detectados a Composables mediante callback
 * 
 * PATRÓN DE COMUNICACIÓN:
 * Usa un callback estático para comunicar events NFC desde Activity (mundo Android)
 * hacia Composables (mundo Jetpack Compose). En una arquitectura más robusta
 * se usaría SharedFlow o Channel para comunicación reactiva.
 */
class MainActivity : ComponentActivity() {
    
    // Adaptador NFC del sistema Android
    private var nfcAdapter: NfcAdapter? = null
    
    // PendingIntent para recibir intents NFC cuando la app está en foreground
    private var pendingIntent: PendingIntent? = null
    
    companion object {
        /**
         * Callback global para notificar tags NFC detectados a Composables
         * 
         * Se registra desde CartScreen cuando el usuario activa el escaneo
         * Se limpia cuando CartScreen se desmonta o se cancela el escaneo
         * 
         * Nota: En producción usar SharedFlow/StateFlow en lugar de callback estático
         */
        var onNFCTagDiscovered: ((Tag) -> Unit)? = null
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Inicializar adaptador NFC
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        
        // Crear PendingIntent para interceptar tags NFC
        // FLAG_MUTABLE es necesario para que el sistema actualice el intent con datos NFC
        pendingIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            PendingIntent.FLAG_MUTABLE
        )
        
        setContent {
            Huerto_HogarTheme {
                AppNavigationContainer()
            }
        }
    }
    
    override fun onResume() {
        super.onResume()
        
        /**
         * Habilitar foreground dispatch de NFC
         * 
         * Esto hace que ESTA actividad reciba todos los tags NFC detectados
         * mientras está en primer plano, con prioridad sobre otras apps.
         * 
         * Parámetros:
         * - this: Activity que recibirá los intents
         * - pendingIntent: Intent que se enviará cuando se detecte un tag
         * - null: Sin filtros de IntentFilter (acepta todos los tags)
         * - null: Sin filtros de tecnología (acepta todas las tecnologías NFC)
         */
        nfcAdapter?.enableForegroundDispatch(this, pendingIntent, null, null)
    }
    
    override fun onPause() {
        super.onPause()
        
        /**
         * Deshabilitar foreground dispatch cuando la app va a segundo plano
         * Esto permite que otras apps reciban eventos NFC
         */
        nfcAdapter?.disableForegroundDispatch(this)
    }
    
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        
        /**
         * Este método se llama cuando se detecta un tag NFC
         * (gracias a FLAG_ACTIVITY_SINGLE_TOP en el PendingIntent)
         * 
         * Verifica el tipo de acción NFC:
         * - ACTION_TAG_DISCOVERED: Tag genérico detectado
         * - ACTION_TECH_DISCOVERED: Tag con tecnología específica
         * - ACTION_NDEF_DISCOVERED: Tag NDEF con datos
         */
        if (NfcAdapter.ACTION_TAG_DISCOVERED == intent.action ||
            NfcAdapter.ACTION_TECH_DISCOVERED == intent.action ||
            NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action) {
            
            // Extraer objeto Tag del intent (compatible con Android 13+)
            val tag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(NfcAdapter.EXTRA_TAG, Tag::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
            }
            
            // Si hay un tag y alguien está escuchando, notificar
            tag?.let {
                onNFCTagDiscovered?.invoke(it)
            }
        }
    }
}
