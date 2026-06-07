package com.dmm.recetario.core.utils.mapper

import com.dmm.recetario.data.local.database.entity.RecipeEntityImpl
import com.dmm.recetario.data.model.dto.RecipeDTO
import com.dmm.recetario.domain.entity.RecipeEntity
import com.dmm.recetario.domain.model.Recipe

fun RecipeDTO.toDomain(): Recipe {
    val creator = this.creator?.get("id")?.toString()
    val categories = this.categories?.mapNotNull {
        it["id"]?.toString()
    }

    return Recipe (
        id = this.id,
        name = this.name,
        persons = this.persons,
        ingredients = this.ingredients,
        steps = this.steps,
        totalTimeInMinutes = this.totalTimeInMinutes,
        cookingTimeInMinutes = this.cookingTimeInMinutes,
        preparationTimeInMinutes = this.preparationTimeInMinutes,
        stars = this.stars,
        icon = this.icon,

        creator = creator,
        categories = categories
    )
}

fun RecipeEntity.toDomain(): Recipe {
    return Recipe (
        id = this.id,
        name = this.name,
        persons = this.persons,
        ingredients = this.ingredients,
        steps = this.steps,
        totalTimeInMinutes = this.totalTimeInMinutes,
        cookingTimeInMinutes = this.cookingTimeInMinutes,
        preparationTimeInMinutes = this.preparationTimeInMinutes,
        stars = this.stars,
        icon = this.icon,

        categories = emptyList(),
        creator = null
    )
}

fun Recipe.toEntity(): RecipeEntity {
    return RecipeEntityImpl (
        id = this.id,
        name = this.name,
        persons = this.persons,
        ingredients = this.ingredients,
        steps = this.steps,
        totalTimeInMinutes = this.totalTimeInMinutes,
        cookingTimeInMinutes = this.cookingTimeInMinutes,
        preparationTimeInMinutes = this.preparationTimeInMinutes,
        stars = this.stars,
        icon = this.icon,

        user_id = ""
    )
}