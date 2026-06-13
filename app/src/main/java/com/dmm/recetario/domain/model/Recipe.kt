package com.dmm.recetario.domain.model

data class Recipe (
    val id: String,
    val stars: Int,
    val name: String,
    val persons: Int,
    val icon: String?,
    val steps: List<String>,
    val totalTimeInMinutes: Int,
    val cookingTimeInMinutes: Int,
    val preparationTimeInMinutes: Int,
    val ingredients: List<Map<String, String>>,

    val creator: String?,
    var categories: List<String>?
)