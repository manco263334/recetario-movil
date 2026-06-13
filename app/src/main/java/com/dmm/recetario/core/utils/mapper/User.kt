package com.dmm.recetario.core.utils.mapper

import com.dmm.recetario.data.local.database.entity.UserEntityImpl
import com.dmm.recetario.data.model.dto.UserDTO
import com.dmm.recetario.domain.entity.UserEntity
import com.dmm.recetario.domain.model.User

fun UserDTO.toDomain(): User {
    val recipes = this.recipes?.mapNotNull {
        it["id"]?.toString()
    }

    return User (
        id = this.id,
        role = this.role,
        icon = this.icon,
        name = this.name,
        email = this.email,
        phone = this.phone,
        username = this.username,

        recipes = recipes
    )
}

fun UserEntity.toDomain(): User {
    return User (
        id = this.id,
        icon = this.icon,
        role = this.role,
        name = this.name,
        email = this.email,
        phone = this.phone,
        username = this.username,

        recipes = null
    )
}

fun User.toEntity(): UserEntity {
    return UserEntityImpl (
        id = this.id,
        role = this.role,
        icon = this.icon,
        name = this.name,
        email = this.email,
        phone = this.phone,
        username = this.username,
    )
}