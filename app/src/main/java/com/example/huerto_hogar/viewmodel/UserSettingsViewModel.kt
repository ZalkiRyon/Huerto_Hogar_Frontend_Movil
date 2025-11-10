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
        _uiState.update { it.copy(name = name, errors = it.errors.copy(nameError = null)) }
    }

    fun onChangeLastname(lastname: String) {
        _uiState.update {
            it.copy(
                lastname = lastname,
                errors = it.errors.copy(lastnameError = null)
            )
        }
    }

    fun onChangeEmail(email: String) {
        _uiState.update { it.copy(email = email, errors = it.errors.copy(emailError = null)) }
    }

    fun onChangeAddress(address: String) {
        _uiState.update { it.copy(address = address, errors = it.errors.copy(addressError = null)) }
    }

    fun onChangePhone(phone: String) {
        _uiState.update { it.copy(phone = phone, errors = it.errors.copy(phoneError = null)) }
    }

    fun onChangeCurrentPassword(password: String) {
        _uiState.update {
            it.copy(
                currentPassword = password,
                errors = it.errors.copy(currentPasswordError = null, errors = null)
            )
        }
    }

    fun onChangeNewPassword(password: String) {
        _uiState.update {
            it.copy(
                newPassword = password,
                errors = it.errors.copy(newPasswordError = null)
            )
        }
    }

    fun onChangeConfirmNewPassword(password: String) {
        _uiState.update {
            it.copy(
                confirmNewPassword = password,
                errors = it.errors.copy(confirmNewPasswordError = null)
            )
        }
    }


    fun onClickSave() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, errors = UserSettingErrors()) }
        val state = _uiState.value
        val currentUser = userManager.currentUser.value ?: run {
            _saveResult.emit(false); return@launch
        }

        val hasValidationErrors = validateProfileForm(state, currentUser)

        if (hasValidationErrors) {
            _uiState.update { it.copy(isLoading = false) }
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
            it.copy(
                isLoading = false,
                currentPassword = "",
                newPassword = "",
                confirmNewPassword = ""
            )
        }

        if (!success && !updatedUser.email.equals(currentUser.email, ignoreCase = true)) {
            _uiState.update {
                it.copy(errors = it.errors.copy(errors = "El nuevo correo electrónico ya está en uso por otro usuario."))
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


        if (state.name.isBlank()) {
            errors = errors.copy(nameError = "El nombre es obligatorio"); hasError = true
        }
        if (state.lastname.isBlank()) {
            errors = errors.copy(lastnameError = "El apellido es obligatorio"); hasError = true
        }
        if (state.address.isBlank()) {
            errors = errors.copy(addressError = "La direccion es obligatoria"); hasError = true
        }


        if (state.phone.trim().length > 9) {
            errors = errors.copy(phoneError = "El número es demasiado largo"); hasError = true
        }


        if (state.email.isBlank()) {
            errors = errors.copy(emailError = "El correo es obligatorio"); hasError = true
        } else if (state.email.length < 5) {
            errors =
                errors.copy(emailError = "El correo debe tener un mínimo de 5 caracteres"); hasError =
                true
        } else if (!isValidEmail(state.email)) {
            errors = errors.copy(emailError = "El formato del correo es incorrecto"); hasError =
                true
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

        _uiState.update { it.copy(errors = errors) }
        return hasError
    }

}



