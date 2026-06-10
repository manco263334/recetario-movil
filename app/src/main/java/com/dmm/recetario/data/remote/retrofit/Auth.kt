package com.dmm.recetario.data.remote.retrofit

import com.dmm.recetario.domain.model.LoginData
import com.dmm.recetario.domain.model.LoginResponse
import com.dmm.recetario.domain.model.MeResponse
import com.dmm.recetario.domain.model.RegisterData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthRemote {
    @POST("auth/login")
    suspend fun login(@Body data: LoginData): Response<LoginResponse>

    @POST("auth/register")
    suspend fun register(@Body data: RegisterData): Response<LoginResponse>

    @POST("auth/logout")
    suspend fun logout(): Response<Unit>

    @GET("auth/me")
    suspend fun me(): Response<MeResponse>
}