package com.example.huerto_hogar.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.huerto_hogar.data.di.NetworkModule
import com.example.huerto_hogar.model.User
import com.example.huerto_hogar.model.uistate.SettingUserUIState
import com.example.huerto_hogar.model.uistate.SettingUserUIStateErrors
import com.example.huerto_hogar.utils.FileUtils
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserSettingsViewModel() : ViewModel() {
    lateinit var userManager: UserViewModel
    private val userApiService = NetworkModule.userApiService

    private val _uiState = MutableStateFlow(SettingUserUIState())
    val uiState: StateFlow<SettingUserUIState> = _uiState.asStateFlow()

    private val _saveResult = MutableSharedFlow<Boolean>()
    val saveResult: SharedFlow<Boolean> = _saveResult.asSharedFlow()

    private val _profilePictureUri = MutableStateFlow<Uri?>(null)
    val profilePictureUri: StateFlow<Uri?> = _profilePictureUri

    private val _uploadingImage = MutableStateFlow(false)
    val uploadingImage: StateFlow<Boolean> = _uploadingImage.asStateFlow()

    private val _imageUploadError = MutableSharedFlow<String?>()
    val imageUploadError: SharedFlow<String?> = _imageUploadError.asSharedFlow()


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
                    newProfilePhoto = user.profilePictureUrl ?: "",
                    isInitialLoadComplete = true
                )
            }
            Log.d("USER_VM_DEBUG", "TRAE IMAGEN: ${ user.profilePictureUrl}")
            // If user has a profile picture URL from Cloudinary, clear the local URI
            if (!user.profilePictureUrl.isNullOrBlank()) {
                _profilePictureUri.value = null
            }
        }
    }

    fun setProfilePictureUri(uri: Uri?, context: Context? = null) {
        _profilePictureUri.value = uri

        // Auto-upload to backend when URI is set and context is available
        if (uri != null && context != null) {
            uploadProfileImage(uri, context)
        }
    }

    /**
     * Uploads the selected profile image to the backend (which uploads to Cloudinary)
     * and updates the user's profile with the new image URL
     */
    private fun uploadProfileImage(uri: Uri, context: Context) = viewModelScope.launch {
        val currentUser = userManager.currentUser.value
        if (currentUser == null) {
            _imageUploadError.emit("Usuario no encontrado")
            return@launch
        }

        _uploadingImage.value = true

        try {
            // Convert URI to MultipartBody.Part using FileUtils
            val imagePart = FileUtils.prepareImagePart(context, uri, "file")

            if (imagePart == null) {
                _imageUploadError.emit("Error al preparar la imagen")
                _uploadingImage.value = false
                return@launch
            }

            // Get auth token
            val token = userManager.getAuthToken() ?: run {
                _imageUploadError.emit("Token no disponible")
                _uploadingImage.value = false
                return@launch
            }
            Log.d("USER_VM_DEBUG", "TOKEN: ${token}")
            // Upload image to backend
            val response = userApiService.uploadProfileImage(
                id = currentUser.id,
                file = imagePart,
                token = "Bearer $token"
            )

            if (response.isSuccessful) {
                val uploadResponse = response.body()
                val imageUrl = uploadResponse?.imageUrl

                if (imageUrl != null) {
                    // Update user state with the new Cloudinary URL
                    val updatedUser = currentUser.copy(profilePictureUrl = imageUrl)
                    Log.d("UserSettingsViewModel", "Updated user object: $updatedUser")

                    val updateSuccess = userManager.updateUser(updatedUser)
                    Log.d(
                        "UserSettingsViewModel",
                        "UserManager.updateUser returned: $updateSuccess"
                    )
                    Log.d(
                        "UserSettingsViewModel",
                        "Current user after update: ${userManager.currentUser.value}"
                    )

                    // Clear local URI since we now have the Cloudinary URL
                    _profilePictureUri.value = null

                    // Update UI state
                    _uiState.update {
                        it.copy(newProfilePhoto = imageUrl)
                    }

                    Log.d("UserSettingsViewModel", "Profile image uploaded successfully: $imageUrl")
                    _imageUploadError.emit("✓ Foto de perfil actualizada")
                } else {
                    _imageUploadError.emit("No se recibió la URL de la imagen")
                }
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Error desconocido"
                _imageUploadError.emit("Error al subir imagen: ${response.code()}")
                Log.e("UserSettingsViewModel", "Upload failed: $errorMsg")
            }

        } catch (e: Exception) {
            _imageUploadError.emit("Error de red: ${e.message}")
            Log.e("UserSettingsViewModel", "Exception during upload", e)
        } finally {
            _uploadingImage.value = false
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

        _uiState.update { it.copy(name = name, errors = it.errors.copy(nameError = error)) }
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

        _uiState.update { it.copy(email = email, errors = it.errors.copy(emailError = error)) }
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
                errors = it.errors.copy(runError = error)
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
                errors = it.errors.copy(regionError = error)
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
                errors = it.errors.copy(comunaError = error)
            )
        }
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

        _uiState.update {
            it.copy(
                address = address,
                errors = it.errors.copy(addressError = error),
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

        _uiState.update { it.copy(phone = phone, errors = it.errors.copy(phoneError = error)) }
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
        _uiState.update { it.copy(isLoading = true) }

        val state = _uiState.value
        val currentUser = userManager.currentUser.value

        if (currentUser == null) {
            _uiState.update { it.copy(isLoading = false) }
            _saveResult.emit(false)
            return@launch
        }

        // Validar formulario
        val hasValidationErrors = validateProfileForm(state, currentUser)
        if (hasValidationErrors) {
            _uiState.update { it.copy(isLoading = false) }
            return@launch
        }

        try {
            // Determinar password (si está cambiando o mantener el actual)
            val finalPassword = state.newPassword.ifBlank { currentUser.password }

            // Construir request para el backend
            val updateRequest = com.example.huerto_hogar.model.UpdateUserRequest(
                email = state.email,
                password = finalPassword,
                name = state.name,
                lastname = state.lastname,
                run = state.run,
                phone = state.phone,
                region = state.region,
                comuna = state.comuna,
                address = state.address,
                comment = currentUser.comment,
                profilePhoto = state.newProfilePhoto,
                roleId = if (currentUser.role == "admin") 1 else if (currentUser.role == "vendedor") 3 else 2
            )

            // Obtener token
            val token = userManager.getAuthToken()
            if (token == null) {
                Log.e("UserSettingsViewModel", "No auth token available")
                _uiState.update { it.copy(isLoading = false) }
                _saveResult.emit(false)
                return@launch
            }

            // Llamar al backend
            val response = userApiService.updateUser(
                id = currentUser.id,
                userRequest = updateRequest,
                token = "Bearer $token"
            )

            if (response.isSuccessful) {
                val updatedUser = response.body()
                if (updatedUser != null) {
                    Log.d("UserSettingsViewModel", "Backend returned user: $updatedUser")

                    // Actualizar currentUser DIRECTAMENTE (no depende de la lista)
                    userManager.setCurrentUser(updatedUser)
                    Log.d("UserSettingsViewModel", "Set currentUser directly")
                    Log.d(
                        "UserSettingsViewModel",
                        "Current user after update: ${userManager.currentUser.value}"
                    )

                    // También intentar actualizar en la lista (si existe)
                    userManager.updateUser(updatedUser)

                    // Recargar perfil en el formulario y limpiar contraseñas
                    _uiState.update {
                        it.copy(
                            id = updatedUser.id,
                            name = updatedUser.name,
                            lastname = updatedUser.lastname,
                            run = updatedUser.run ?: "",
                            region = updatedUser.region ?: "",
                            comuna = updatedUser.comuna ?: "",
                            email = updatedUser.email,
                            address = updatedUser.address,
                            phone = updatedUser.phone ?: "",
                            newProfilePhoto = updatedUser.profilePictureUrl ?: "",
                            currentPassword = "",
                            newPassword = "",
                            confirmNewPassword = "",
                            isLoading = false,
                            errors = SettingUserUIStateErrors() // Limpiar errores
                        )
                    }

                    Log.d(
                        "UserSettingsViewModel",
                        "Profile updated successfully and UI state refreshed"
                    )
                    _saveResult.emit(true)
                } else {
                    Log.e("UserSettingsViewModel", "Response body is null")
                    _uiState.update { it.copy(isLoading = false) }
                    _saveResult.emit(false)
                }
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("UserSettingsViewModel", "Update failed: ${response.code()} - $errorBody")

                // Verificar si es error de email duplicado
                if (errorBody?.contains("correo") == true || errorBody?.contains("email") == true) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errors = it.errors.copy(errors = "El correo electrónico ya está en uso.")
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false) }
                }
                _saveResult.emit(false)
            }

        } catch (e: Exception) {
            Log.e("UserSettingsViewModel", "Exception updating profile", e)
            _uiState.update { it.copy(isLoading = false) }
            _saveResult.emit(false)
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun validateProfileForm(state: SettingUserUIState, currentUser: User): Boolean {
        var errors = SettingUserUIStateErrors()
        var hasError = false

        // Validación de nombre
        if (state.name.isBlank()) {
            errors = errors.copy(nameError = "El nombre es obligatorio"); hasError = true
        } else if (state.name.trim().length < 1) {
            errors = errors.copy(nameError = "El nombre debe tener al menos 1 caracter"); hasError =
                true
        } else if (state.name.trim().length > 20) {
            errors = errors.copy(nameError = "El nombre no puede exceder 20 caracteres"); hasError =
                true
        } else if (!state.name.trim().matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$"))) {
            errors = errors.copy(nameError = "El nombre solo puede contener letras"); hasError =
                true
        }

        // Validación de apellido
        if (state.lastname.isBlank()) {
            errors = errors.copy(lastnameError = "El apellido es obligatorio"); hasError = true
        } else if (state.lastname.trim().length < 4) {
            errors =
                errors.copy(lastnameError = "El apellido debe tener al menos 4 caracteres"); hasError =
                true
        } else if (state.lastname.trim().length > 20) {
            errors =
                errors.copy(lastnameError = "El apellido no puede exceder 20 caracteres"); hasError =
                true
        } else if (!state.lastname.trim().matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$"))) {
            errors =
                errors.copy(lastnameError = "El apellido solo puede contener letras"); hasError =
                true
        }

        // Validación de dirección
        if (state.address.isBlank()) {
            errors = errors.copy(addressError = "La direccion es obligatoria"); hasError = true
        } else if (state.address.trim().length < 5) {
            errors =
                errors.copy(addressError = "La direccion debe tener al menos 5 caracteres"); hasError =
                true
        } else if (state.address.trim().length > 40) {
            errors =
                errors.copy(addressError = "La direccion no puede exceder 40 caracteres"); hasError =
                true
        }

        // Validación de teléfono
        if (state.phone.trim().isNotEmpty()) {
            if (!state.phone.trim().matches(Regex("^[0-9]+$"))) {
                errors =
                    errors.copy(phoneError = "El teléfono solo puede contener números"); hasError =
                    true
            } else if (state.phone.trim().length < 8) {
                errors =
                    errors.copy(phoneError = "El teléfono debe tener al menos 8 dígitos"); hasError =
                    true
            } else if (state.phone.trim().length > 9) {
                errors =
                    errors.copy(phoneError = "El teléfono no puede exceder 9 dígitos"); hasError =
                    true
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
            errors =
                errors.copy(emailError = "Solo se aceptan correos @duocuc.cl o @profesor.duoc.cl"); hasError =
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



