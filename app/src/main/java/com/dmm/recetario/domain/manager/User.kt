package com.dmm.recetario.domain.manager

import com.dmm.recetario.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserManager {
    suspend fun syncUser()

    fun getUserLocal(token: String?): Flow<User?>
}