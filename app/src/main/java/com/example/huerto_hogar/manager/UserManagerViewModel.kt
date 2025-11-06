package com.example.huerto_hogar.manager

import androidx.lifecycle.ViewModel
import com.example.huerto_hogar.model.Role
import com.example.huerto_hogar.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class UserManagerViewModel : ViewModel() {

    private val initialUsers = listOf(
        User(
            id = 1,
            role = Role.ADMIN,
            comment = "Usuario administrador principal del sistema",
            name = "Super",
            lastname = "Super",
            email = "admin@duoc.cl",
            password = "admin123",
            phone = "912345678",
            address = "Av. Providencia 1234, Oficina 501"
        ),
        User(
            id = 2,
            role = Role.CLIENT,
            comment = "Cliente VIP, compra productos orgánicos semanalmente",
            name = "Ana",
            lastname = "Martinez",
            email = "ana.martinez@gmail.com",
            password = "cliente123",
            phone = "945678912",
            address = "Av. Apoquindo 4567, Casa 78"
        )
    )

    // Users List
    private val _userList = MutableStateFlow(initialUsers.toMutableList())
    val userList: StateFlow<List<User>> = _userList.asStateFlow()

    private var nextId = initialUsers.maxOf { it.id } + 1

    fun registerUser(newUser: User): User? {
        if (_userList.value.any { it.email.equals(newUser.email, ignoreCase = true) }) {
            return null
        }

        val userWithId = newUser.copy(id = nextId++)
        _userList.update { currentList ->

            currentList.toMutableList().apply { add(userWithId) }
        }
        return userWithId
    }


    fun findUserByEmail(email: String): User? {
        return _userList.value.find { it.email.equals(email, ignoreCase = true) }
    }

}