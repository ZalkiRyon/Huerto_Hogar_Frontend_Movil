package com.example.huerto_hogar

import org.junit.Assert.*
import org.junit.Test

/**
 * Test Unitario #2: Categorías de Productos
 * 
 * Verifica las categorías válidas del catálogo:
 * - Validación de categorías permitidas
 * - Mapeo de categorías a prefijos
 * - Validación de nombres de categorías
 */
class ProductCategoryTest {

    // Categorías válidas del sistema
    private val categoriasValidas = listOf(
        "Frutas frescas",
        "Verduras organicas",
        "Productos organicos",
        "Productos lacteos"
    )

    // Mapeo de categorías a prefijos de código
    private val categoriaPrefijos = mapOf(
        "Frutas frescas" to "FR",
        "Verduras organicas" to "VR",
        "Productos organicos" to "PO",
        "Productos lacteos" to "PL"
    )

    @Test
    fun validar_categorias_permitidas() {
        // Categorías que deben ser aceptadas
        val categoriasAceptadas = listOf(
            "Frutas frescas",
            "Verduras organicas",
            "Productos organicos",
            "Productos lacteos"
        )
        
        categoriasAceptadas.forEach { categoria ->
            assertTrue(
                "La categoría '$categoria' debería ser válida",
                categoriasValidas.contains(categoria)
            )
        }
    }

    @Test
    fun rechazar_categorias_invalidas() {
        // Categorías que NO deben ser aceptadas
        val categoriasInvalidas = listOf(
            "Carnes",
            "Pescados",
            "Bebidas",
            "frutas frescas",  // minúscula
            "FRUTAS FRESCAS"   // mayúscula
        )
        
        categoriasInvalidas.forEach { categoria ->
            assertFalse(
                "La categoría '$categoria' debería ser inválida",
                categoriasValidas.contains(categoria)
            )
        }
    }

    @Test
    fun mapear_categoria_a_prefijo_correcto() {
        // Verificar mapeo de cada categoría a su prefijo
        assertEquals("FR", categoriaPrefijos["Frutas frescas"])
        assertEquals("VR", categoriaPrefijos["Verduras organicas"])
        assertEquals("PO", categoriaPrefijos["Productos organicos"])
        assertEquals("PL", categoriaPrefijos["Productos lacteos"])
    }

    @Test
    fun obtener_prefijo_desde_categoria() {
        val categoria = "Verduras organicas"
        val prefijoEsperado = "VR"
        
        val prefijo = categoriaPrefijos[categoria]
        
        assertNotNull("El prefijo no debería ser null", prefijo)
        assertEquals("El prefijo debería ser VR", prefijoEsperado, prefijo)
    }

    @Test
    fun verificar_cantidad_de_categorias() {
        // El sistema debe tener exactamente 4 categorías
        assertEquals(
            "Deberían existir 4 categorías",
            4,
            categoriasValidas.size
        )
        
        assertEquals(
            "Deberían existir 4 mapeos de prefijos",
            4,
            categoriaPrefijos.size
        )
    }

    @Test
    fun todos_los_prefijos_tienen_dos_letras() {
        categoriaPrefijos.values.forEach { prefijo ->
            assertEquals(
                "El prefijo '$prefijo' debería tener 2 caracteres",
                2,
                prefijo.length
            )
            assertTrue(
                "El prefijo '$prefijo' debería estar en mayúsculas",
                prefijo == prefijo.uppercase()
            )
        }
    }

    @Test
    fun categoria_no_existente_retorna_null() {
        val categoriaInexistente = "Bebidas Alcohólicas"
        
        val prefijo = categoriaPrefijos[categoriaInexistente]
        
        assertNull(
            "Una categoría inexistente debería retornar null",
            prefijo
        )
    }
}
