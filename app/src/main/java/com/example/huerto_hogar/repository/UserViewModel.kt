package com.example.huerto_hogar.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.huerto_hogar.data.UserEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserViewModel(private val repository: UserRepository) : ViewModel() {

    val listAllUsers: StateFlow<List<UserEntity>> = repository.getAllUsers.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onDeleteUser(user: UserEntity) {
        viewModelScope.launch {
            repository.deleteUser(entity = user)
        }
    }
}