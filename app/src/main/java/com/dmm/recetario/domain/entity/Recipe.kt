package com.dmm.recetario.domain.entity


abstract class RecipeEntity (
    open val id: String,
    open val name: String,
    open val persons: Int,
    open val ingredients: List<Map<String, String>>,
    open val steps: List<String>,
    open val totalTimeInMinutes: Int,
    open val cookingTimeInMinutes: Int,
    open val preparationTimeInMinutes: Int,
    open val stars: Int,
    open val icon: String?,

    open val user_id: String
)

abstract class RecipeCategoryCrossRef (
    open val recipe_id: String,
    open val category_id: String
)