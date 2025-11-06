package com.example.huerto_hogar.model

// Roles in the system
enum class Role {
    ADMIN,
    CLIENT,
    SALESMAN
}


// THIS IS THE FINAL CLASS FOR USER
data class User(
    // we use 0 for initial parameter but with the function it will be replace by another id
    val id: Int = 0,
    val role: Role = Role.CLIENT,

    // variables nullables
    val comment: String? = null,
    val phone: String?,

    // THIS THE USER HAVE TO FILL
    val name: String,
    val lastname: String,
    val email: String,
    val password: String,
    val address: String,
)