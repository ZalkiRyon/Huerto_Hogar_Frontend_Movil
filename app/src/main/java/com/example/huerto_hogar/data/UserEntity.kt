package com.example.huerto_hogar.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.huerto_hogar.model.Role

// we defined our table user, this will contains the final user
// ()J() this has nothing to do with our class register user, this is for our class USER from Model
@Entity(tableName = "user")
data class UserEntity(
    // The program understands that the variable under this notation is the PK, starts from 3
    // Cuz we have 2 users (admin with id 1 and one salesman with id 2
    @PrimaryKey(autoGenerate = true)
    val id: Int = 3,

    val role: Role,

    val name: String,
    val lastname: String,
    val email: String,
    val password: String,
    val address: String,

    val phone: String?,
    val comment: String?
)
