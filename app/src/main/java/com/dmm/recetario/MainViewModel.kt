package com.dmm.recetario

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmm.recetario.core.jwt.isTokenExpired
import com.dmm.recetario.domain.manager.TokenManager
import com.dmm.recetario.domain.manager.UserManager
import com.dmm.recetario.domain.model.User
import com.dmm.recetario.domain.service.CategoryService
import com.dmm.recetario.domain.service.RecipeService
import com.dmm.recetario.domain.service.UserService
import com.dmm.recetario.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.SharingStarted

@HiltViewModel
class MainViewModel @Inject constructor (
    private val userService: UserService,
    private val userManager: UserManager,
    private val tokenManager: TokenManager,
    private val recipeService: RecipeService,
    private val categoryService: CategoryService
) : ViewModel() {
    private val _token = tokenManager.token

    @OptIn(ExperimentalCoroutinesApi::class)
    val user: StateFlow<User?> = _token
        .flatMapLatest { token ->
            if (token == null || isTokenExpired(token)) {
                flowOf(null)
            } else {
                userManager.getUserLocal(token)
            }
        }
        .stateIn (
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val startDestination: StateFlow<Routes?> = _token
        .flatMapLatest { token ->
            if (token == null || isTokenExpired(token)) {
                flowOf(Routes.Login)
            } else {
                flowOf(Routes.Home)
            }
        }
        .stateIn (
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    init {
        sync()
    }

    private fun sync() {
        viewModelScope.launch {
            awaitAll (
                async { userManager.syncUser() },
                async { userService.syncUsers(0, 10, false) },
                async { recipeService.syncRecipes(0, 10, false, true) },
                async { categoryService.syncCategories(0, 10, true) }
            )
        }
    }
}