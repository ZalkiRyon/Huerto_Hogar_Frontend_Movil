package com.example.huerto_hogar

import com.example.huerto_hogar.data.api.UserApiService
import com.example.huerto_hogar.model.AuthResponse
import com.example.huerto_hogar.model.User
import com.example.huerto_hogar.model.UserLoginRequest
import com.example.huerto_hogar.repository.UserRepositoryT
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test


class UserLoginRepositoryTest {
    private val mockApiService = mockk<UserApiService>()
    private val repositoryT = UserRepositoryT(mockApiService)

    private val loginRequest = UserLoginRequest(
        email = "test@duocuc.cl",
        password = "testpassword"
    )

    // Test 1: Validar que el repository me devuelva la respuesta esperada
    @Test
    fun loginUser_devuleve_usuario_con_mismo_email_en_request() = runTest {
        val mockUser = User(
            id = 1,
            role = "cliente",
            comment = null,
            phone = "123456789",
            name = "Test",
            lastname = "User",
            email = loginRequest.email,
            password = loginRequest.password,
            run = "11.111.111-1",
            region = "RM",
            comuna = "Santiago",
            address = "Calle Falsa 123",
            registrationDate = "2024-01-01",
            profilePictureUrl = null,
            activo = true
        )

        val expectedResponse = AuthResponse(
            message = "Login exitoso",
            user = mockUser,
            token = "token_de_prueba_123"
        )

        coEvery {
            mockApiService.loginUser(loginRequest, any())
        } returns expectedResponse

        val result = repositoryT.loginUser(loginRequest)

        // Al ser exitoso el resultado de user no deberia ser null
        assertNotNull(result.user)

        // Ademas validar que el email que me devuleve es el esperado
        assertEquals(loginRequest.email, result.user?.email)
    }


    // Test 2: En caso de no entregarle credenciales no deveria devovler nada porque se espera que sea incorrecto
    @Test
    fun loginUser_devuelve_user_nulo_cuando_el_email_viene_vacio() = runTest {

        val requestSinEmail = UserLoginRequest(
            email = "",
            password = "testpassword"
        )


        val expectedResponse = AuthResponse(
            message = "Email requerido",
            user = null,
            token = null
        )

        coEvery {
            mockApiService.loginUser(requestSinEmail, any())
        } returns expectedResponse


        val result = repositoryT.loginUser(requestSinEmail)


        assertNull(result.user)
        assertEquals("Email requerido", result.message)

    }
}



