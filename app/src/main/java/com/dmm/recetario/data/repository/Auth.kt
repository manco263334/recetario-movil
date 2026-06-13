package com.dmm.recetario.data.repository

import com.dmm.recetario.R
import com.dmm.recetario.core.utils.extension.isNotNull
import com.dmm.recetario.core.utils.handler.handleApiCall
import com.dmm.recetario.core.utils.helper.ResourceHelper
import com.dmm.recetario.data.remote.retrofit.AuthRemote
import com.dmm.recetario.domain.model.LoginData
import com.dmm.recetario.domain.model.LoginResponse
import com.dmm.recetario.domain.model.MeResponse
import com.dmm.recetario.domain.model.RegisterData
import com.dmm.recetario.domain.repository.AuthRepository

class AuthRepositoryImpl (
    private val remote: AuthRemote,
    private val resourceHelper: ResourceHelper
) : AuthRepository {
    override suspend fun login(data: LoginData): LoginResponse {
        val response = handleApiCall(resourceHelper) {
            remote.login(data)
        }

        return response ?: throw NoSuchElementException (
            resourceHelper.getString(R.string.invalid_credentials)
        )
    }

    override suspend fun register(data: RegisterData): LoginResponse {
        val response = handleApiCall(resourceHelper) {
            remote.register(data)
        }

        assert(response.isNotNull())

        return response!!
    }

    override suspend fun logout() {
        handleApiCall(resourceHelper) {
            remote.logout()
        }
    }

    override suspend fun me(): MeResponse {
        val response = handleApiCall(resourceHelper) {
            remote.me()
        }

        return response!!
    }
}