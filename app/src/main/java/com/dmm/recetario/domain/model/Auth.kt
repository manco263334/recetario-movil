package com.dmm.recetario.domain.model

data class LoginData (
    val email: String,
    val password: String
)

data class LoginResponse (
    val name: String,
    val token: String,
    val username: String?
)

data class RegisterData (
    val name: String,
    val email: String,
    val phone: String?,
    val password: String,
    val username: String?
)

data class MeResponse (
    val id: String,
    val name: String,
    val email: String,
    val username: String?
)