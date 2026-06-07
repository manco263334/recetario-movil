package com.dmm.recetario.core.utils.mapper

import com.dmm.recetario.data.local.database.entity.CategoryEntityImpl
import com.dmm.recetario.data.model.dto.CategoryDTO
import com.dmm.recetario.domain.entity.CategoryEntity
import com.dmm.recetario.domain.model.Category

fun CategoryDTO.toDomain(): Category {
    val recipes = this.recipes?.mapNotNull {
        it["id"]?.toString()
    }

    return Category (
        id = this.id,
        name = this.name,
        icon = this.icon,

        recipes = recipes
    )
}

fun CategoryEntity.toDomain(): Category {
    return Category (
        id = this.id,
        name = this.name,
        icon = this.icon,

        recipes = emptyList()
    )
}

fun Category.toEntity(): CategoryEntity {
    return CategoryEntityImpl (
        id = this.id,
        name = this.name,
        icon = this.icon
    )
}