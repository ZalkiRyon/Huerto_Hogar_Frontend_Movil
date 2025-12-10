package com.example.huerto_hogar

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.example.huerto_hogar.model.Product
import com.example.huerto_hogar.ui.theme.components.ModalDetailProduct
import com.example.huerto_hogar.viewmodel.CartViewModel
import org.junit.Rule
import org.junit.Test

class ModalDetailQuantityTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val fakeProduct = Product(
        id = 1,
        name = "Lechuga",
        category = "Verduras",
        price = 1000.0,
        stock = 10,
        imageUrl = "",
        description = "Lechuga de prueba"
    )


    @Test
    fun botones_aumentar_y_reducir_actualizan_quantity_en_pantalla() {
        val cartViewModel = CartViewModel()

        composeTestRule.setContent {
            ModalDetailProduct(
                product = fakeProduct,
                onClose = {},
                cart = cartViewModel,
                isFavorite = false,
                onToggleFavorito = {}
            )
        }

        val btnSuma = composeTestRule.onNodeWithTag("increaseButton")
        val btnResta = composeTestRule.onNodeWithTag("decreaseButton")
        val cantidad = composeTestRule.onNodeWithTag("quantityText")


        cantidad.assertTextContains("0")

        btnSuma.performClick()
        btnSuma.performClick()

        cantidad.assertTextContains("2")

        btnSuma.performClick()
        btnResta.performClick()

        cantidad.assertTextContains("2")
    }

    @Test
    fun deshabilitar_boton_si_no_cantidad_mayor_1() {
        val cartViewModel = CartViewModel()

        composeTestRule.setContent {
            ModalDetailProduct(
                product = fakeProduct,
                onClose = {},
                cart = cartViewModel,
                isFavorite = false,
                onToggleFavorito = {}
            )
        }

        val btnResta = composeTestRule.onNodeWithTag("decreaseButton")
        val cantidad = composeTestRule.onNodeWithTag("quantityText")


        cantidad.assertTextContains("0")

        btnResta.performClick()
        btnResta.assertIsNotEnabled()
    }

    @Test
    fun habilitar_boton_si_la_cantidad_mayor_1() {
        val cartViewModel = CartViewModel()

        composeTestRule.setContent {
            ModalDetailProduct(
                product = fakeProduct,
                onClose = {},
                cart = cartViewModel,
                isFavorite = false,
                onToggleFavorito = {}
            )
        }

        val btnResta = composeTestRule.onNodeWithTag("decreaseButton")
        val btnSuma = composeTestRule.onNodeWithTag("increaseButton")
        val cantidad = composeTestRule.onNodeWithTag("quantityText")

        cantidad.assertTextContains("0")

        btnSuma.performClick()
        btnResta.assertIsEnabled()
    }


}