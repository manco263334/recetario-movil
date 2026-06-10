package com.dmm.recetario.domain.service

import com.dmm.recetario.domain.model.LoginData
import com.dmm.recetario.domain.model.LoginResponse
import com.dmm.recetario.domain.model.MeResponse
import com.dmm.recetario.domain.model.RegisterData

interface AuthService {
    suspend fun login(data: LoginData): LoginResponse
    suspend fun register(data: RegisterData): LoginResponse
    suspend fun logout()
    suspend fun me(): MeResponse
}