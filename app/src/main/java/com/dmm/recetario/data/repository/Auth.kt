package com.dmm.recetario.data.repository

import com.dmm.recetario.core.utils.extension.isNotNull
import com.dmm.recetario.core.utils.handler.handleApiCall
import com.dmm.recetario.data.remote.retrofit.AuthRemote
import com.dmm.recetario.domain.model.LoginData
import com.dmm.recetario.domain.model.LoginResponse
import com.dmm.recetario.domain.model.MeResponse
import com.dmm.recetario.domain.model.RegisterData
import com.dmm.recetario.domain.repository.AuthRepository

class AuthRepositoryImpl (
    private val remote: AuthRemote
) : AuthRepository {
    override suspend fun login(data: LoginData): LoginResponse {
        val response = handleApiCall { remote.login(data) }

        return response ?: throw NoSuchElementException("Credenciales Inválidas")
    }

    override suspend fun register(data: RegisterData): LoginResponse {
        val response = handleApiCall { remote.register(data) }

        assert(response.isNotNull())

        return response!!
    }

    override suspend fun logout() {
        handleApiCall { remote.logout() }
    }

    override suspend fun me(): MeResponse {
        val response = handleApiCall { remote.me() }

        return response!!
    }
}