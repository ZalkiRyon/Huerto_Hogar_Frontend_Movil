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
            name = "Juan",
            lastname = "Perez",
            email = "juan.perez@duocuc.cl",
            password = "cliente123",
            phone = "945678912",
            address = "Av. Apoquindo 4567, Casa 78"
        )
    )

    // Users List
    private val _userList = MutableStateFlow(initialUsers.toMutableList())
    val userList: StateFlow<List<User>> = _userList.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

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

    fun findUserByCredentials(email: String, password: String): User? {
        return _userList.value.find { user ->

            val emailMatch = user.email.equals(email, ignoreCase = true)
            val passwordMatch = user.password == password

            emailMatch && passwordMatch
        }
    }

    fun setCurrentUser(user: User?) {
        _currentUser.value = user
    }

    fun updateUser(updatedUser: User): Boolean {
        val currentList = _userList.value.toMutableList()

        val index = currentList.indexOfFirst { it.id == updatedUser.id }

        if (index != -1) {
            currentList[index] = updatedUser
            _userList.value = currentList.toList() as MutableList<User>

            _currentUser.value = updatedUser
            return true
        }
        return false
    }

    fun findUserByEmail(email: String): User? {
        return _userList.value.find { it.email.equals(email, ignoreCase = true) }
    }
}