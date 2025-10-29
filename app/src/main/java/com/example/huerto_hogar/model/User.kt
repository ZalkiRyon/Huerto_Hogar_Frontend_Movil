package com.example.huerto_hogar.model

import java.time.LocalDateTime

// Roles in the system
enum class Role {
    ADMIN,
    CLIENT,
    SALESMAN
}

// CLASS FOR STATE FORM
data class RegisterUser(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val name: String = "",
    val lastname: String = "",
    val phone: String = "",
    val address: String = "",
    val registrationSuccess: Boolean = false,
    val errors: RegisterUserErrors = RegisterUserErrors()
)

// CLASS MANAGE ERROR STATE FORM
data class RegisterUserErrors(
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val nameError: String? = null,
    val lastnameError: String? = null,
    val addressError: String? = null
)

// THIS IS THE FINAL CLASS FOR USER
data class User(
    // THIS WILL ASSIGN AUTOMATICALLY
    val id: Int,
    val role: Role,
    val registerDate: LocalDateTime,
    val comment: String?,

    // THIS THE USER HAVE TO FILL
    val name: String,
    val lastname: String,
    val email: String,
    val password: String,
    val phone: String?,
    val address: String,
)