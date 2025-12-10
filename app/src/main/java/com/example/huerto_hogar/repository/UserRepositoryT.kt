package com.example.huerto_hogar.repository

import com.example.huerto_hogar.data.api.UserApiService
import com.example.huerto_hogar.model.AuthResponse
import com.example.huerto_hogar.model.UserLoginRequest

class UserRepositoryT(private val apiService: UserApiService) {
    suspend fun loginUser(request: UserLoginRequest): AuthResponse {
        return  apiService.loginUser(request)
    }
}