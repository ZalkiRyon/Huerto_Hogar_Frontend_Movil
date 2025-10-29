package com.example.huerto_hogar.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Delete
    suspend fun deleteUser(user: UserEntity)

    @Query(value = "SELECT * FROM user ORDER BY id")
    fun listUsers(): Flow<List<UserEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM user WHERE email = :email LIMIT 1)")
    suspend fun doesEmailExist(email: String): Boolean

    @Query("SELECT * FROM user WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?
}