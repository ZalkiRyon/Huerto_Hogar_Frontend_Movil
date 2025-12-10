package com.example.huerto_hogar.model.uistate

import com.example.huerto_hogar.model.User

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val loginSuccess: Boolean = false,
    val error: String? = null,
    val token: String? = null,
    val loggedInUser: User? = null,
    val loginErrors: LoginErrors = LoginErrors()
)

data class LoginErrors(
    val emailError: String? = null,
    val passwordError: String? = null
)