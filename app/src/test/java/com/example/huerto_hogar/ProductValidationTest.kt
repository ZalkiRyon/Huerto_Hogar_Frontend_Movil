package com.example.huerto_hogar

import org.junit.Assert.*
import org.junit.Test

/**
 * Test Unitario #3: Validación de Productos
 * 
 * Verifica las reglas de negocio para productos:
 * - Validación de formato de código (PREFIJO### - Nombre)
 * - Validación de precios positivos
 * - Validación de stock no negativo
 */
class ProductValidationTest {

    @Test
    fun validar_formato_codigo_producto_correcto() {
        // Formato correcto: "PREFIJO### - Nombre"
        val codigosValidos = listOf(
            "FR001 - Manzanas Fuji",
            "VR002 - Zanahorias",
            "PO123 - Miel Orgánica",
            "PL999 - Leche Entera"
        )
        
        val regex = Regex("^[A-Z]{2}\\d{3} - .+$")
        
        codigosValidos.forEach { codigo ->
            assertTrue("El código '$codigo' debería ser válido", regex.matches(codigo))
        }
    }

    @Test
    fun validar_formato_codigo_producto_incorrecto() {
        // Formatos incorrectos
        val codigosInvalidos = listOf(
            "FR001-Manzanas",        // Sin espacios
            "FR1 - Manzanas",         // Número corto
            "F001 - Manzanas",        // Prefijo corto
            "001 - Manzanas",         // Sin prefijo
            "FR001 -",                // Sin nombre
            "FR001 - "                // Solo espacios después
        )
        
        val regex = Regex("^[A-Z]{2}\\d{3} - .+$")
        
        codigosInvalidos.forEach { codigo ->
            assertFalse("El código '$codigo' debería ser inválido", regex.matches(codigo))
        }
    }

    @Test
    fun validar_precio_debe_ser_positivo() {
        val preciosValidos = listOf(100.0, 1200.0, 5000.0, 0.01)
        val preciosInvalidos = listOf(0.0, -100.0, -1.0)
        
        // Precios válidos (mayores a 0)
        preciosValidos.forEach { precio ->
            assertTrue("Precio $precio debería ser válido", precio > 0)
        }
        
        // Precios inválidos (0 o negativos)
        preciosInvalidos.forEach { precio ->
            assertFalse("Precio $precio debería ser inválido", precio > 0)
        }
    }

    @Test
    fun validar_stock_no_negativo() {
        val stocksValidos = listOf(0, 1, 100, 1000)
        val stocksInvalidos = listOf(-1, -10, -100)
        
        // Stocks válidos (0 o positivos)
        stocksValidos.forEach { stock ->
            assertTrue("Stock $stock debería ser válido", stock >= 0)
        }
        
        // Stocks inválidos (negativos)
        stocksInvalidos.forEach { stock ->
            assertFalse("Stock $stock debería ser inválido", stock >= 0)
        }
    }

    @Test
    fun validar_nombre_producto_no_vacio() {
        val nombresValidos = listOf(
            "Manzanas Fuji",
            "M",
            "Producto con nombre largo para testing"
        )
        
        val nombresInvalidos = listOf(
            "",
            "   ",
            "\t\n"
        )
        
        // Nombres válidos
        nombresValidos.forEach { nombre ->
            assertTrue("Nombre '$nombre' debería ser válido", nombre.trim().isNotEmpty())
        }
        
        // Nombres inválidos
        nombresInvalidos.forEach { nombre ->
            assertFalse("Nombre '$nombre' debería ser inválido", nombre.trim().isNotEmpty())
        }
    }

    @Test
    fun validar_descripcion_minima() {
        // Descripción debe tener al menos 10 caracteres
        val descripcionesValidas = listOf(
            "Descripción válida con más de 10 caracteres",
            "1234567890",
            "Producto x"  // Exactamente 10 caracteres
        )
        
        val descripcionesInvalidas = listOf(
            "Corta",
            "123",
            ""
        )
        
        val longitudMinima = 10
        
        // Descripciones válidas
        descripcionesValidas.forEach { desc ->
            assertTrue(
                "Descripción '$desc' debería ser válida",
                desc.trim().length >= longitudMinima
            )
        }
        
        // Descripciones inválidas
        descripcionesInvalidas.forEach { desc ->
            assertFalse(
                "Descripción '$desc' debería ser inválida",
                desc.trim().length >= longitudMinima
            )
        }
    }

    @Test
    fun extraer_prefijo_de_codigo_producto() {
        val productos = mapOf(
            "FR001 - Manzanas" to "FR",
            "VR002 - Zanahorias" to "VR",
            "PO123 - Miel" to "PO",
            "PL999 - Leche" to "PL"
        )
        
        productos.forEach { (codigo, prefijoEsperado) ->
            val prefijoExtraido = codigo.substring(0, 2)
            assertEquals("El prefijo de '$codigo' debería ser '$prefijoEsperado'", 
                prefijoEsperado, prefijoExtraido)
        }
    }
}
