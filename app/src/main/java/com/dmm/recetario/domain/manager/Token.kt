package com.dmm.recetario.domain.manager

import kotlinx.coroutines.flow.Flow

interface TokenManager {
    val token: Flow<String?>

    suspend fun saveToken(token: String)

    suspend fun clearToken()
}