package com.dmm.recetario.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dmm.recetario.ui.auth.login.LoginViewModel
import com.dmm.recetario.ui.auth.register.RegisterViewModel
import com.dmm.recetario.ui.category.CategoryViewModel
import com.dmm.recetario.ui.home.HomeViewModel
import com.dmm.recetario.ui.recipe.RecipeViewModel
import com.dmm.recetario.ui.settings.SettingsViewModel

data class LayoutConfig (
    val hasFab: Boolean = false,
    val hasRefresh: Boolean = false,
    val hasBottomBar: Boolean = false,
    val showBaseLayout: Boolean = true,
    val onCompleteForm: (() -> Unit)? = null,
    val onRefresh: (suspend () -> Unit)? = null
)

@Composable
fun getViewModels() = mapOf (
    "home" to hiltViewModel<HomeViewModel>(),
    "login" to hiltViewModel<LoginViewModel>(),
    "recipe" to hiltViewModel<RecipeViewModel>(),
    "category" to hiltViewModel<CategoryViewModel>(),
    "register" to hiltViewModel<RegisterViewModel>(),
    "settings" to hiltViewModel<SettingsViewModel>()
)