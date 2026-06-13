package com.dmm.recetario.domain.entity

abstract class UserEntity (
    open val id: String,
    open val role: String,
    open val name: String,
    open val email: String,
    open val icon: String?,
    open val phone: String?,
    open val username: String?
)

abstract class TokenUserRef (
    open val token: String,
    open val email: String
)