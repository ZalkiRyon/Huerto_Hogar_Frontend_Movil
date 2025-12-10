package com.example.huerto_hogar

import com.example.huerto_hogar.repository.UserRepository

import com.example.huerto_hogar.viewmodel.LoginViewModel
import com.example.huerto_hogar.viewmodel.UserViewModel
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test


// Prueba unitaria donde validamos el correcto funcionamiento de onChangeEmail que se encuentra en el LoginViewModel
class OnChangeEmailUserLoginTest {
    private lateinit var viewModel: LoginViewModel

    // ahcemos mocks de userrepository y userviewmodel porque el  LoginViewModel los exige.
    private val mockRepository = mockk<UserRepository>()
    private val mockUserViewModel = mockk<UserViewModel>()

    // como su anotacion lo dice, se ejecuta antes de cada test
    @Before
    fun setup() {
        viewModel = LoginViewModel(
            repository = mockRepository,
            userViewModel = mockUserViewModel
        )
    }
   // cada test empieza con un viewModel nuevo y loginuistate limpiecito

    @Test
    fun campo_email_vacio() {
        viewModel.onChangeEmail("")

        val state = viewModel.uiState.value

        assertEquals("", state.email)

        assertNull(state.loginErrors.emailError)
    }

    @Test
    fun campo_email_formato_erroneo() {
        viewModel.onChangeEmail("correo_malo")

        val state = viewModel.uiState.value
        assertEquals("correo_malo", state.email)

        assertEquals("El formato del correo es incorrecto", state.loginErrors.emailError)
    }

    @Test
    fun campo_email_formato_correcto_dominio_erroneo() {
        viewModel.onChangeEmail("alguien@gmail.com")

        val state = viewModel.uiState.value

        assertEquals("alguien@gmail.com", state.email)
        assertEquals(
            "Solo se aceptan correos @duocuc.cl o @profesor.duoc.cl",
            state.loginErrors.emailError
        )
    }

    @Test
    fun campo_email_formato_correcto_dominio_correcto() {
        viewModel.onChangeEmail("alguien@duocuc.cl")

        val state = viewModel.uiState.value
        assertEquals("alguien@duocuc.cl", state.email)
        assertNull(state.loginErrors.emailError)
    }

}