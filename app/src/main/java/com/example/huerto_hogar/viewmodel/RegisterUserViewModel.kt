package com.example.huerto_hogar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.huerto_hogar.model.UserRegisterRequest
import com.example.huerto_hogar.model.uistate.RegisterUserUiState
import com.example.huerto_hogar.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class RegisterUserViewModel(
    private val repository: UserRepository = UserRepository(),
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUserUiState())
    val uiState: StateFlow<RegisterUserUiState> = _uiState.asStateFlow()
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
            // When we try to call de update is necessary to specify which val is like:
            //  email = email, otherwise the program will not know which it is
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
                nombre = normalizeName,
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
                apellido = normalizeLastname,
                errors = it.errors.copy(lastnameError = error)
            )
        }
    }

    fun onChangePhone(phone: String) {
        var error: String? = null
        val strippedPhone = phone.filter { it.isDigit() }

        if (strippedPhone.isNotEmpty()) {
            if (strippedPhone.length < 8 || strippedPhone.length > 15) {
                error = "El teléfono debe tener entre 8 y 15 dígitos."
            }

        }

        _uiState.update {
            it.copy(
                telefono = phone,
                errors = it.errors.copy(phoneError = error)
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
                direccion = address,
                errors = currentState.errors.copy(addressError = error)
            )
        }
    }

    fun onChangeRun(run: String) {
        var error: String? = null

        if (run.isEmpty() || run.trim().isBlank()) {
            error = "El RUN es obligatorio y no puede estar vacio"

        } else if (!run.matches(Regex("^\\d{1,2}\\.\\d{3}\\.\\d{3}-[\\dkK]\$"))) {
            error = "El formato del RUN no es válido. Debe usar puntos y guión (ej: 12.345.678-K)."
        }

        _uiState.update {
            it.copy(
                run = run,
                errors = it.errors.copy(runErrors = error)
            )
        }
    }

    fun onChangeRegion(region: String) {
        var error: String? = null

        if (region.isEmpty() || region.trim().isBlank()) {
            error = "La region es obligatoria y no puede estar vacia."

        }

        _uiState.update {
            it.copy(
                region = region,
                errors = it.errors.copy(regionErrors = error)
            )
        }
    }

    fun onChangeComuna(comuna: String) {
        var error: String? = null

        if (comuna.isEmpty() || comuna.trim().isBlank()) {
            error = "La comuna comuna es obligatoria y no puede estar vacia."

        }

        _uiState.update {
            it.copy(
                comuna = comuna,
                errors = it.errors.copy(comunaErrors = error)
            )
        }
    }

    // Function for button register - Refactorizado para usar API
    fun onClickRegister() {
        val currentState = _uiState.value

        onChangeName(currentState.nombre)
        onChangeLastname(currentState.apellido)
        onChangeEmail(currentState.email)
        onChangePassword(currentState.password)
        onChangeConfirmPassword(currentState.confirmPassword)
        onChangeAddress(currentState.direccion)


        val validatedState = _uiState.value

        val hasLocalErrors = with(validatedState.errors) {
            emailError != null || passwordError != null || confirmPasswordError != null ||
                    nameError != null || lastnameError != null
        }

        if (hasLocalErrors) {
            _uiState.update { it.copy(isLoading = false) }
            return
        }

        _uiState.update { it.copy(isLoading = true) }


        viewModelScope.launch {
            try {
                val request = UserRegisterRequest(
                    email = validatedState.email,
                    password = validatedState.password,
                    nombre = validatedState.nombre,
                    apellido = validatedState.apellido,
                    telefono = validatedState.telefono,
                    direccion = validatedState.direccion,
                    run = validatedState.run,
                    region = validatedState.region,
                    comuna = validatedState.comuna,
                    comentario = null,
                    fotoPerfil = null
                )

                val response = repository.registerUser(request)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        registrationSuccess = true,
                        registeredUser = response.user,
                        error = null
                    )
                }

            } catch (e: Exception) {


                val errorMessage = when {
                    e.message?.contains(
                        "409",
                        ignoreCase = true
                    ) == true || e.message?.contains(
                        "ya existe",
                        ignoreCase = true
                    ) == true -> "Este correo ya está registrado."

                    else -> "Error de conexión: ${e.message}"
                }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        registrationSuccess = false,
                        error = errorMessage,
                        errors = it.errors.copy(
                            emailError = if (errorMessage.contains("ya registrado")) errorMessage else null
                        )
                    )
                }
            }
        }
    }

    fun resetUiState() {
        _uiState.update { RegisterUserUiState() }
    }
}