package com.example.huerto_hogar.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.huerto_hogar.model.User
import com.example.huerto_hogar.repository.UserRepository
import com.example.huerto_hogar.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserViewModel(
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {

    private val _userList = MutableStateFlow<MutableList<User>>(mutableListOf())
    val userList: StateFlow<List<User>> = _userList.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    // Token de autenticación
    private val _authToken = MutableStateFlow<String?>(null)
    val authToken: StateFlow<String?> = _authToken.asStateFlow()

    // Control de vista para administradores (true = ver tienda como cliente, false = ver dashboard admin)
    private val _showAdminStoreView = MutableStateFlow(false)
    val showAdminStoreView: StateFlow<Boolean> = _showAdminStoreView.asStateFlow()

    // Estados para carga de datos
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private var nextId = 100 // ID inicial alto para evitar conflictos


    /**
     * Carga todos los usuarios desde el backend
     * Puede llamarse con o sin token de autenticación
     */
    fun loadUsersFromBackend(token: String? = null) {
        viewModelScope.launch {
            // Usar token proporcionado, o el guardado, o vacío para endpoints públicos
            val authToken = token ?: _authToken.value ?: ""

            userRepository.getAllUsers(authToken).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _isLoading.value = true
                        _errorMessage.value = null
                    }
                    is Resource.Success -> {
                        _isLoading.value = false
                        resource.data?.let { users ->
                            _userList.value = users.toMutableList()
                            nextId = (users.maxOfOrNull { it.id } ?: 100) + 1
                        }
                    }
                    is Resource.Error -> {
                        _isLoading.value = false
                        _errorMessage.value = resource.message
                        // Mantener datos mock si falla la carga
                    }
                }
            }
        }
    }

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

    /**
     * Guarda el token de autenticación
     *
     * @param token Token JWT del backend
     */
    fun saveAuthToken(token: String) {
        _authToken.value = token
    }

    /**
     * Obtiene el token de autenticación actual
     *
     * @return Token o null si no hay usuario autenticado
     */
    fun getAuthToken(): String? {
        return _authToken.value
    }

    /**
     * Cierra sesión del usuario actual
     */
    fun logout() {
        _currentUser.value = null
        _authToken.value = null
        _showAdminStoreView.value = false
    }

    /**
     * Alterna la vista del administrador entre tienda y dashboard
     */
    fun toggleAdminView(showStore: Boolean) {
        _showAdminStoreView.value = showStore
    }
}