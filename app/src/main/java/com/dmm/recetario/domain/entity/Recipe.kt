package com.dmm.recetario.domain.entity


abstract class RecipeEntity (
    open val id: String,
    open val stars: Int,
    open val name: String,
    open val persons: Int,
    open val icon: String?,
    open val steps: List<String>,
    open val totalTimeInMinutes: Int,
    open val cookingTimeInMinutes: Int,
    open val preparationTimeInMinutes: Int,
    open val ingredients: List<Map<String, String>>,

    open val userId: String
)

abstract class RecipeCategoryCrossRef (
    open val recipeId: String,
    open val categoryId: String
)