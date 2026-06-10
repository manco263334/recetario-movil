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
    override val name: String,
    override val persons: Int,
    override val ingredients: List<Map<String, String>>,
    override val steps: List<String>,
    override val totalTimeInMinutes: Int,
    override val cookingTimeInMinutes: Int,
    override val preparationTimeInMinutes: Int,
    override val stars: Int,
    override val icon: String?,

    override val user_id: String
) : RecipeEntity(id, name, persons, ingredients, steps, totalTimeInMinutes, cookingTimeInMinutes,
    preparationTimeInMinutes, stars, icon, user_id)

@Entity (
    tableName = "categories_recipes",
    primaryKeys = ["recipe_id", "category_id"]
)
data class RecipeCategoryCrossRefImpl (
    @ColumnInfo(name = "recipe_id")
    override val recipe_id: String,

    @ColumnInfo(name = "category_id")
    override val category_id: String
) : RecipeCategoryCrossRef(recipe_id, category_id)