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
        // ADMINISTRADORES
        User(
            id = 1,
            role = Role.ADMIN,
            comment = "Usuario administrador principal del sistema",
            name = "Super",
            lastname = "Super",
            email = "admin@duocuc.cl",
            password = "admin123",
            phone = "912345678",
            address = "Av. Providencia 1234, Oficina 501"
        ),
        User(
            id = 2,
            role = Role.ADMIN,
            comment = "Administrador de operaciones y logística",
            name = "Carlos",
            lastname = "Rodríguez",
            email = "carlos.rodriguez@duocuc.cl",
            password = "admin123",
            phone = "923456789",
            address = "Av. Las Condes 8956, Depto 301"
        ),
        User(
            id = 3,
            role = Role.ADMIN,
            comment = "Administrador de ventas y marketing",
            name = "María",
            lastname = "González",
            email = "maria.gonzalez@duocuc.cl",
            password = "admin123",
            phone = "934567890",
            address = "Av. Vicuña Mackenna 4502, Casa 12"
        ),
        
        // CLIENTES
        User(
            id = 4,
            role = Role.CLIENT,
            comment = "Cliente VIP, compra productos orgánicos semanalmente",
            name = "Juan",
            lastname = "Pérez",
            email = "juan.perez@duocuc.cl",
            password = "cliente123",
            phone = "945678912",
            address = "Av. Apoquindo 4567, Casa 78"
        ),
        User(
            id = 5,
            role = Role.CLIENT,
            comment = "Cliente frecuente, prefiere frutas de temporada",
            name = "Ana",
            lastname = "Silva",
            email = "ana.silva@duocuc.cl",
            password = "cliente123",
            phone = "956789123",
            address = "Av. Irarrázaval 2345, Depto 506"
        ),
        User(
            id = 6,
            role = Role.CLIENT,
            comment = "Cliente nuevo, interesado en productos orgánicos",
            name = "Pedro",
            lastname = "Martínez",
            email = "pedro.martinez@duocuc.cl",
            password = "cliente123",
            phone = "967890234",
            address = "Calle Los Aromos 789, Población Central"
        ),
        User(
            id = 7,
            role = Role.CLIENT,
            comment = "Cliente regular, compra verduras cada semana",
            name = "Laura",
            lastname = "Torres",
            email = "laura.torres@duocuc.cl",
            password = "cliente123",
            phone = "978901345",
            address = "Av. Grecia 1023, Casa 45"
        ),
        User(
            id = 8,
            role = Role.CLIENT,
            comment = "Cliente corporativo, pedidos grandes mensuales",
            name = "Roberto",
            lastname = "Fernández",
            email = "roberto.fernandez@duocuc.cl",
            password = "cliente123",
            phone = "989012456",
            address = "Av. Kennedy 5678, Oficina 1202"
        ),
        
        // VENDEDORES
        User(
            id = 9,
            role = Role.SALESMAN,
            comment = "Vendedor senior, especializado en productos orgánicos",
            name = "Diego",
            lastname = "Vargas",
            email = "diego.vargas@duocuc.cl",
            password = "vendedor123",
            phone = "990123567",
            address = "Av. Tobalaba 3456, Depto 702"
        ),
        User(
            id = 10,
            role = Role.SALESMAN,
            comment = "Vendedor junior, enfocado en atención al cliente",
            name = "Carolina",
            lastname = "Muñoz",
            email = "carolina.munoz@duocuc.cl",
            password = "vendedor123",
            phone = "991234678",
            address = "Calle Santa Rosa 890, Casa 23"
        ),
        User(
            id = 11,
            role = Role.SALESMAN,
            comment = "Vendedor especialista en frutas y verduras frescas",
            name = "Andrés",
            lastname = "Soto",
            email = "andres.soto@duocuc.cl",
            password = "vendedor123",
            phone = "992345789",
            address = "Av. Quilín 2134, Depto 401"
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