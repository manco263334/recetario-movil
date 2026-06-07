package com.dmm.recetario.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dmm.recetario.domain.entity.TokenUserRef
import com.dmm.recetario.domain.entity.UserEntity

@Entity(tableName = "users")
data class UserEntityImpl (
    @PrimaryKey
    override val id: String,
    override val name: String,
    override val email: String,
    override val role: String,
    override val phone: String?,
    override val username: String?,
    override val icon: String?,
) : UserEntity(id, name, email, role, phone, username, icon)

@Entity (
    tableName = "tokens_users",
    primaryKeys = ["token", "email"]
)
data class TokenUserRefImpl (
    override val token: String,
    override val email: String
) : TokenUserRef(token, email)