package com.dmm.recetario.domain.service

import com.dmm.recetario.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserService {
    fun getAllUsers(): Flow<List<User>>

    suspend fun syncUsers (
        page: Int,
        size: Int,
        withRecipes: Boolean?
    ): Boolean

    fun getUserById(id: String): Flow<User?>

    fun getUserByEmail(email: String): Flow<User?>

    fun getUserByUsername(username: String): Flow<User?>

    fun getUserByTokenOrAnonymous(token: String): Flow<User?>

    suspend fun deleteUser(id: String)

    suspend fun updateUser(id: String, data: User): User
}