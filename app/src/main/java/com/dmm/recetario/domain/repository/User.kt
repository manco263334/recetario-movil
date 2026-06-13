package com.dmm.recetario.domain.repository

import com.dmm.recetario.domain.model.User

interface UserRepository {
    suspend fun getAllUsers (
        page: Int,
        size: Int,
        withRecipes: Boolean?
    ): List<User>

    suspend fun getUser(id: String, withRecipes: Boolean?): User

    suspend fun updateUser(id: String, data: User): User

    suspend fun deleteUser(id: String)
}