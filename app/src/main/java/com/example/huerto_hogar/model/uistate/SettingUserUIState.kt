package com.example.huerto_hogar.model.uistate

data class SettingUserUIState(
    val id: Int = 0,
    val name: String = "",
    val lastname: String = "",
    val email: String = "",
    val run: String = "",
    val region: String = "",
    val comuna: String = "",
    val address: String = "",
    val phone: String = "",
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmNewPassword: String = "",
    val newProfilePhoto: String = "",

    val isLoading: Boolean = false,
    val isInitialLoadComplete: Boolean = false,
    val errors: SettingUserUIStateErrors = SettingUserUIStateErrors(),
)

data class SettingUserUIStateErrors(
    val nameError: String? = null,
    val lastnameError: String? = null,
    val emailError: String? = null,
    val runError: String? = null,
    val regionError: String? = null,
    val comunaError: String? = null,
    val addressError: String? = null,
    val phoneError: String? = null,
    val currentPasswordError: String? = null,
    val newPasswordError: String? = null,
    val confirmNewPasswordError: String? = null,

    val errors: String? = null
)