package com.example.huerto_hogar.viewmodel

import androidx.lifecycle.ViewModel
import com.example.huerto_hogar.manager.UserManagerViewModel
import com.example.huerto_hogar.model.RegisterUser
import com.example.huerto_hogar.model.RegistrationResult
import com.example.huerto_hogar.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class RegisterUserViewModel() : ViewModel() {

    lateinit var userManager: UserManagerViewModel
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
        } else if (!isValidDuocEmail(email)) {
            error = "Solo se aceptan correos @duocuc.cl o @profesor.duoc.cl"
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

    private fun isValidDuocEmail(email: String): Boolean {
        val lowerEmail = email.lowercase().trim()
        return lowerEmail.endsWith("@duocuc.cl") || lowerEmail.endsWith("@profesor.duoc.cl")
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
        } else if (normalizeName.length < 1) {
            error = "El nombre debe tener al menos 1 caracter"
        } else if (normalizeName.length > 20) {
            error = "El nombre no puede exceder 20 caracteres"
        } else if (!normalizeName.matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$"))) {
            error = "El nombre solo puede contener letras"
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
        } else if (normalizeLastname.length < 4) {
            error = "El apellido debe tener al menos 4 caracteres"
        } else if (normalizeLastname.length > 20) {
            error = "El apellido no puede exceder 20 caracteres"
        } else if (!normalizeLastname.matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$"))) {
            error = "El apellido solo puede contener letras"
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
        val trimmedPhone = phone.trim()

        if (trimmedPhone.isNotEmpty()) {
            if (!trimmedPhone.matches(Regex("^[0-9]+$"))) {
                error = "El teléfono solo puede contener números"
            } else if (trimmedPhone.length < 8) {
                error = "El teléfono debe tener al menos 8 dígitos"
            } else if (trimmedPhone.length > 9) {
                error = "El teléfono no puede exceder 9 dígitos"
            }
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
        val trimmedAddress = address.trim()

        if (trimmedAddress.isBlank()) {
            error = "La direccion es obligatoria"
        } else if (trimmedAddress.length < 5) {
            error = "La direccion debe tener al menos 5 caracteres"
        } else if (trimmedAddress.length > 40) {
            error = "La direccion no puede exceder 40 caracteres"
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

        onChangeName(currentState.name)
        onChangeLastname(currentState.lastname)
        onChangeEmail(currentState.email)
        onChangePassword(currentState.password)
        onChangeConfirmPassword(currentState.confirmPassword)
        onChangeAddress(currentState.address)
        onChangePhone(currentState.phone)

        val validatedState = _uiState.value

        val hasLocalErrors = with(validatedState.errors) {
            emailError != null || passwordError != null || confirmPasswordError != null ||
                    nameError != null || lastnameError != null
        }

        if (hasLocalErrors) {
            _uiState.update { it.copy(isLoading = false) }
            return
        }

        val newUser = User(
            name = validatedState.name,
            lastname = validatedState.lastname,
            email = validatedState.email,
            password = validatedState.password,
            address = validatedState.address,
            phone = validatedState.phone.ifBlank { null },
            comment = null,
        )

        val finalRegisteredUser = userManager.registerUser(newUser)

        val result = if (finalRegisteredUser != null) {
            RegistrationResult.SUCCESS
        } else {
            RegistrationResult.EMAIL_ALREADY_EXISTS
        }


        _uiState.update {
            it.copy(
                registrationResultEvent = result,
                isLoading = false,
                registeredUser = finalRegisteredUser
            )
        }

    }
}