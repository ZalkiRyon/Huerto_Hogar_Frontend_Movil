package com.example.huerto_hogar.model

data class UserSetting(
    val id: Int = 0,
    val name: String = "",
    val lastname: String = "",
    val email: String = "",
    val address: String = "",
    val phone: String = "",
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmNewPassword: String = "",
    val newProfilePhoto: String = "",

    val isLoading: Boolean = false,
    val isInitialLoadComplete: Boolean = false,
    val errors: UserSettingErrors = UserSettingErrors(),
)

data class UserSettingErrors(
    val nameError: String? = null,
    val lastnameError: String? = null,
    val emailError: String? = null,
    val addressError: String? = null,
    val phoneError: String? = null,
    val currentPasswordError: String? = null,
    val newPasswordError: String? = null,
    val confirmNewPasswordError: String? = null,

    val errors: String? = null
)