package com.dmm.recetario.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dmm.recetario.domain.entity.RecipeCategoryCrossRef
import com.dmm.recetario.domain.entity.RecipeEntity

@Entity(tableName = "recipes")
data class RecipeEntityImpl (
    @PrimaryKey
    override val id: String,
    override val stars: Int,
    override val name: String,
    override val persons: Int,
    override val icon: String?,
    override val steps: List<String>,
    override val totalTimeInMinutes: Int,
    override val cookingTimeInMinutes: Int,
    override val preparationTimeInMinutes: Int,
    override val ingredients: List<Map<String, String>>,

    @ColumnInfo(name = "user_id")
    override val userId: String
) : RecipeEntity (
    id = id,
    icon = icon,
    name = name,
    steps = steps,
    stars = stars,
    persons = persons,
    ingredients = ingredients,
    totalTimeInMinutes = totalTimeInMinutes,
    cookingTimeInMinutes = cookingTimeInMinutes,
    preparationTimeInMinutes = preparationTimeInMinutes,

    userId = userId
)

@Entity (
    tableName = "categories_recipes",
    primaryKeys = ["recipe_id", "category_id"]
)
data class RecipeCategoryCrossRefImpl (
    @ColumnInfo(name = "recipe_id")
    override val recipeId: String,

    @ColumnInfo(name = "category_id")
    override val categoryId: String
) : RecipeCategoryCrossRef(recipeId, categoryId)