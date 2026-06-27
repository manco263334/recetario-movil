package com.dmm.recetario.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Routes : NavKey {
    @Serializable
    data object Login : Routes

    @Serializable
    data object Register : Routes

    @Serializable
    data object Home : Routes

    @Serializable
    data object Settings : Routes

    @Serializable
    data class Category(val id: String) : Routes

    @Serializable
    data class Recipe(val id: String) : Routes
}