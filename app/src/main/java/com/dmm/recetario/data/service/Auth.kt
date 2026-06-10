package com.dmm.recetario.data.service

import com.dmm.recetario.domain.model.LoginData
import com.dmm.recetario.domain.model.LoginResponse
import com.dmm.recetario.domain.model.MeResponse
import com.dmm.recetario.domain.model.RegisterData
import com.dmm.recetario.domain.repository.AuthRepository
import com.dmm.recetario.domain.service.AuthService

class AuthServiceImpl (
    private val repository: AuthRepository
) : AuthService {
    override suspend fun login(data: LoginData): LoginResponse {
        return repository.login(data)
    }

    override suspend fun register(data: RegisterData): LoginResponse {
        return repository.register(data)
    }

    override suspend fun logout() {
        repository.logout()
    }

    override suspend fun me(): MeResponse {
        return repository.me()
    }
}