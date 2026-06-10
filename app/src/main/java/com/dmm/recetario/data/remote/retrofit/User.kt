package com.dmm.recetario.data.remote.retrofit

import com.dmm.recetario.data.model.dto.UserDTO
import com.dmm.recetario.domain.model.PageResponse
import com.dmm.recetario.domain.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface UserRemote {
    @GET("users")
    suspend fun getAllUsers (
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("withRecipes") withRecipes: Boolean?
    ): Response<PageResponse<UserDTO>>

    @GET("users/{id}")
    suspend fun getUser (
        @Path("id") id: String,
        @Query("withRecipes") withRecipes: Boolean?
    ): Response<UserDTO>

    @PUT("users/{id}")
    suspend fun updateUser (
        @Path("id") id: String,
        @Body data: User
    ): Response<UserDTO>

    @DELETE("users/{id}")
    suspend fun deleteUser (
        @Path("id") id: String
    ): Response<Unit>
}