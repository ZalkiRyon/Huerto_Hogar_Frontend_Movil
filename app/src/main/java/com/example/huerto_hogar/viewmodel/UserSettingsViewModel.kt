package com.example.huerto_hogar.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.huerto_hogar.manager.UserManagerViewModel
import com.example.huerto_hogar.model.User
import com.example.huerto_hogar.model.UserSetting
import com.example.huerto_hogar.model.UserSettingErrors
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserSettingsViewModel() : ViewModel() {
    lateinit var userManager: UserManagerViewModel

    private val _uiState = MutableStateFlow(UserSetting())
    val uiState: StateFlow<UserSetting> = _uiState.asStateFlow()

    private val _saveResult = MutableSharedFlow<Boolean>()
    val saveResult: SharedFlow<Boolean> = _saveResult.asSharedFlow()

    private val _profilePictureUri = MutableStateFlow<Uri?>(null)
    val profilePictureUri: StateFlow<Uri?> = _profilePictureUri


    fun loadUserProfile() {
        val user = userManager.currentUser.value
        if (user != null) {
            _uiState.update {
                it.copy(
                    id = user.id,
                    name = user.name,
                    lastname = user.lastname,
                    email = user.email,
                    address = user.address,
                    phone = user.phone ?: "",
                    newProfilePhoto = user.profilePictureUrl.toString(),
                    isInitialLoadComplete = true
                )
            }
        }
    }

    fun setProfilePictureUri(uri: Uri?) {
        _profilePictureUri.value = uri
        val uriString = uri.toString()

        _uiState.update {
            it.copy(
                newProfilePhoto = uriString,
            )
        }
    }

    fun onChangeName(name: String) {
        var error: String? = null
        val trimmedName = name.trim()

        if (trimmedName.isNotEmpty()) {
            if (trimmedName.length < 1) {
                error = "El nombre debe tener al menos 1 caracter"
            } else if (trimmedName.length > 20) {
                error = "El nombre no puede exceder 20 caracteres"
            } else if (!trimmedName.matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$"))) {
                error = "El nombre solo puede contener letras"
            }
        }

        _uiState.update { it.copy(name = name, errors = it.errors.copy(nameError = error),) }
    }

    fun onChangeLastname(lastname: String) {
        var error: String? = null
        val trimmedLastname = lastname.trim()

        if (trimmedLastname.isNotEmpty()) {
            if (trimmedLastname.length < 4) {
                error = "El apellido debe tener al menos 4 caracteres"
            } else if (trimmedLastname.length > 20) {
                error = "El apellido no puede exceder 20 caracteres"
            } else if (!trimmedLastname.matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$"))) {
                error = "El apellido solo puede contener letras"
            }
        }

        _uiState.update {
            it.copy(
                lastname = lastname,
                errors = it.errors.copy(lastnameError = error),
            )
        }
    }

    fun onChangeEmail(email: String) {
        var error: String? = null
        val trimmedEmail = email.trim()

        if (trimmedEmail.isNotEmpty() && !isValidDuocEmail(trimmedEmail)) {
            error = "Solo se aceptan correos @duocuc.cl o @profesor.duoc.cl"
        }

        _uiState.update { it.copy(email = email, errors = it.errors.copy(emailError = error),) }
    }

    fun onChangeAddress(address: String) {
        var error: String? = null
        val trimmedAddress = address.trim()

        if (trimmedAddress.isNotEmpty()) {
            if (trimmedAddress.length < 5) {
                error = "La direccion debe tener al menos 5 caracteres"
            } else if (trimmedAddress.length > 40) {
                error = "La direccion no puede exceder 40 caracteres"
            }
        }

        _uiState.update { it.copy(address = address, errors = it.errors.copy(addressError = error),) }
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

        _uiState.update { it.copy(phone = phone, errors = it.errors.copy(phoneError = error),) }
    }

    private fun isValidDuocEmail(email: String): Boolean {
        val lowerEmail = email.lowercase().trim()
        return lowerEmail.endsWith("@duocuc.cl") || lowerEmail.endsWith("@profesor.duoc.cl")
    }

    fun onChangeCurrentPassword(password: String) {
        _uiState.update {
            it.copy(
                currentPassword = password,
                errors = it.errors.copy(currentPasswordError = null, errors = null),
            )
        }
    }

    fun onChangeNewPassword(password: String) {
        _uiState.update {
            it.copy(
                newPassword = password,
                errors = it.errors.copy(newPasswordError = null),
            )
        }
    }

    fun onChangeConfirmNewPassword(password: String) {
        _uiState.update {
            it.copy(
                confirmNewPassword = password,
                errors = it.errors.copy(confirmNewPasswordError = null),
            )
        }
    }


    fun onClickSave() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true,) }
        val state = _uiState.value
        val currentUser = userManager.currentUser.value ?: run {
            _saveResult.emit(false); return@launch
        }

        val hasValidationErrors = validateProfileForm(state, currentUser)

        if (hasValidationErrors) {
            _uiState.update { it.copy() }
            return@launch
        }


        val finalPassword = state.newPassword.ifBlank {
            currentUser.password
        }


        val updatedUser = currentUser.copy(
            name = state.name,
            lastname = state.lastname,
            email = state.email,
            address = state.address,
            phone = state.phone.ifBlank { null },
            password = finalPassword,
            profilePictureUrl = state.newProfilePhoto
        )


        val success = userManager.updateUser(updatedUser)


        _uiState.update {
            it.copy()
        }

        if (!success && !updatedUser.email.equals(currentUser.email, ignoreCase = true)) {
            _uiState.update {
                it.copy(errors = it.errors.copy(errors = "El nuevo correo electrónico ya está en uso por otro usuario."),)
            }
        }
        _saveResult.emit(success)
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun validateProfileForm(state: UserSetting, currentUser: User): Boolean {
        var errors = UserSettingErrors()
        var hasError = false

        // Validación de nombre
        if (state.name.isBlank()) {
            errors = errors.copy(nameError = "El nombre es obligatorio"); hasError = true
        } else if (state.name.trim().length < 1) {
            errors = errors.copy(nameError = "El nombre debe tener al menos 1 caracter"); hasError = true
        } else if (state.name.trim().length > 20) {
            errors = errors.copy(nameError = "El nombre no puede exceder 20 caracteres"); hasError = true
        } else if (!state.name.trim().matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$"))) {
            errors = errors.copy(nameError = "El nombre solo puede contener letras"); hasError = true
        }

        // Validación de apellido
        if (state.lastname.isBlank()) {
            errors = errors.copy(lastnameError = "El apellido es obligatorio"); hasError = true
        } else if (state.lastname.trim().length < 4) {
            errors = errors.copy(lastnameError = "El apellido debe tener al menos 4 caracteres"); hasError = true
        } else if (state.lastname.trim().length > 20) {
            errors = errors.copy(lastnameError = "El apellido no puede exceder 20 caracteres"); hasError = true
        } else if (!state.lastname.trim().matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$"))) {
            errors = errors.copy(lastnameError = "El apellido solo puede contener letras"); hasError = true
        }

        // Validación de dirección
        if (state.address.isBlank()) {
            errors = errors.copy(addressError = "La direccion es obligatoria"); hasError = true
        } else if (state.address.trim().length < 5) {
            errors = errors.copy(addressError = "La direccion debe tener al menos 5 caracteres"); hasError = true
        } else if (state.address.trim().length > 40) {
            errors = errors.copy(addressError = "La direccion no puede exceder 40 caracteres"); hasError = true
        }

        // Validación de teléfono
        if (state.phone.trim().isNotEmpty()) {
            if (!state.phone.trim().matches(Regex("^[0-9]+$"))) {
                errors = errors.copy(phoneError = "El teléfono solo puede contener números"); hasError = true
            } else if (state.phone.trim().length < 8) {
                errors = errors.copy(phoneError = "El teléfono debe tener al menos 8 dígitos"); hasError = true
            } else if (state.phone.trim().length > 9) {
                errors = errors.copy(phoneError = "El teléfono no puede exceder 9 dígitos"); hasError = true
            }
        }

        // Validación de email
        if (state.email.isBlank()) {
            errors = errors.copy(emailError = "El correo es obligatorio"); hasError = true
        } else if (state.email.length < 5) {
            errors =
                errors.copy(emailError = "El correo debe tener un mínimo de 5 caracteres"); hasError =
                true
        } else if (!isValidEmail(state.email)) {
            errors = errors.copy(emailError = "El formato del correo es incorrecto"); hasError =
                true
        } else if (!isValidDuocEmail(state.email)) {
            errors = errors.copy(emailError = "Solo se aceptan correos @duocuc.cl o @profesor.duoc.cl"); hasError = true
        }


        val changingPassword =
            state.currentPassword.isNotBlank() || state.newPassword.isNotBlank() || state.confirmNewPassword.isNotBlank()

        if (changingPassword) {

            if (state.currentPassword != currentUser.password) {
                errors =
                    errors.copy(currentPasswordError = "Contraseña actual incorrecta."); hasError =
                    true
            }


            if (state.newPassword.isBlank()) {
                errors =
                    errors.copy(newPasswordError = "Debe ingresar una nueva contraseña."); hasError =
                    true
            } else if (state.newPassword.length < 4) {
                errors =
                    errors.copy(newPasswordError = "La contraseña debe tener un mínimo de 4 caracteres."); hasError =
                    true
            } else if (state.newPassword != state.confirmNewPassword) {
                errors =
                    errors.copy(confirmNewPasswordError = "Las nuevas contraseñas no coinciden."); hasError =
                    true
            }
        }

        _uiState.update { it.copy(errors = errors,) }
        return hasError
    }

}



