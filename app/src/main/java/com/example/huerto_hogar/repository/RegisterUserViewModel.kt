package com.example.huerto_hogar.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.huerto_hogar.data.UserEntity
import com.example.huerto_hogar.model.RegisterUser
import com.example.huerto_hogar.model.Role
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class RegisterUserViewModel(private val userRepository: UserRepository) : ViewModel() {
    // Unique StateFlow trying to clean the code without using multiples mutablestateflow
    private val _uiState = MutableStateFlow(RegisterUser())
    val uiState: StateFlow<RegisterUser> = _uiState.asStateFlow()

    fun onChangeEmail(email: String) {
        var error: String? = null

        if (email.isBlank()) {
            error = "El correo es obligatorio"
        } else if (email.length < 5) {
            error = "El correo debe tener un minimo de 5 caracteres"
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            error = "El formato del correo es incorrecto"
        }

        _uiState.update {
            // When we try to call de update is necessary to specify wich val is like:
            //  email = email, otherwise the program will not know wich it is
            it.copy(
                email = email.trim(),
                errors = it.errors.copy(emailError = error)
            )
        }
    }

    fun onChangePassword(password: String) {
        var error: String? = null

        if (password.isBlank()) {
            error = "La contrasena es obligatoria"
        } else if (password.length < 4) {
            error = "La contrasena debe tener un minimo de 4 caracteres"
        }

        _uiState.update {
            it.copy(
                password = password.trim(),
                errors = it.errors.copy(passwordError = error)
            )
        }
    }

    fun onChangeConfirmPassword(confirmPassword: String) {
        val currentPassword = _uiState.value.password
        var error: String? = null

        if (confirmPassword.isBlank()) {
            error = "La contrasena es obligatoria"
        } else if (confirmPassword != currentPassword) {
            error = "Las contrasenas no coinciden"
        }

        _uiState.update {
            it.copy(
                confirmPassword = confirmPassword.trim(),
                errors = it.errors.copy(confirmPasswordError = error)
            )
        }
    }

    fun onChangeName(name: String) {
        // normalize name, delete space blanks and cap first letter
        val normalizeName = if (name.trim().isNotEmpty()) {
            name.trim().lowercase().replaceFirstChar { it.titlecase() }
        } else {
            ""
        }

        var error: String? = null

        if (normalizeName.isEmpty()) {
            error = "El nombre es obligatorio"
        }

        _uiState.update {
            it.copy(
                name = normalizeName,
                errors = it.errors.copy(nameError = error)
            )
        }
    }

    fun onChangeLastname(lastname: String) {
        val normalizeLastname = if (lastname.trim().isNotEmpty()) {
            lastname.trim().lowercase().replaceFirstChar { it.titlecase() }
        } else {
            ""
        }

        var error: String? = null

        if (normalizeLastname.isEmpty()) {
            error = "El apellido es obligatorio"
        }

        _uiState.update {
            it.copy(
                lastname = normalizeLastname,
                errors = it.errors.copy(lastnameError = error)
            )
        }
    }

    fun onChangePhone(phone: String) {
        var error: String? = null

        if (phone.trim().length > 9) {
            error = "El número es demasiado largo"
        }

        _uiState.update { currentState ->
            currentState.copy(
                phone = phone,
                errors = currentState.errors.copy(phoneError = error)
            )
        }
    }

    fun onChangeAddress(address: String) {
        var error: String? = null
        if (address.isBlank()) {
            error = "La direccion es obligatoria"
        }

        _uiState.update { currentState ->
            currentState.copy(
                address = address,
                errors = currentState.errors.copy(addressError = error)
            )
        }
    }

    fun clearRegistrationResultEvent() {
        _uiState.update {
            it.copy(registrationResultEvent = null)
        }
    }

    fun resetUiState() {
        _uiState.update { RegisterUser() }
    }

    // Function for button register
    fun onClickRegister() {
        val currentState = _uiState.value

        viewModelScope.launch {
            val newUserEntity = UserEntity(
                id = 0,
                email = currentState.email,
                name = currentState.name,
                lastname = currentState.lastname,
                password = currentState.password,
                address = currentState.password,
                phone = currentState.phone.ifBlank { null },
                role = Role.CLIENT,
                comment = null

            )

            val result = userRepository.registerUser(newUserEntity)

            _uiState.update {
                it.copy(
                    registrationResultEvent = result
                )
            }
        }
    }
}