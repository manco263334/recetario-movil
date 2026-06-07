package com.dmm.recetario.ui.components.drawer

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmm.recetario.domain.dao.UserDao
import com.dmm.recetario.domain.entity.RecipeEntity
import com.dmm.recetario.domain.entity.TokenUserRef
import com.dmm.recetario.domain.entity.UserEntity
import com.dmm.recetario.domain.manager.TokenManager
import com.dmm.recetario.domain.service.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class DrawerViewModel @Inject constructor (
    private val tokenManager: TokenManager,
    private val authService: AuthService,
    private val userDAO: UserDao<UserEntity, TokenUserRef, RecipeEntity>
): ViewModel() {
    var logOutState by mutableStateOf<LogOutUiState>(LogOutUiState.Idle)
        private set

    fun logout() {
        if (logOutState is LogOutUiState.Loading) return

        viewModelScope.launch {
            logOutState = LogOutUiState.Loading

            try {
                authService.logout()

                awaitAll (
                    async { tokenManager.clearToken() },
                    async { userDAO.clear() },
                    async { userDAO.clearTokenRefs() },
                )

                logOutState = LogOutUiState.Success("Sesión cerrada con éxito")
            } catch (e: Exception) {
                Log.w("DrawerViewModel", "Error al cerrar sesión: ${e.message}")
                logOutState = LogOutUiState.Error("Error al cerrar sesión: ${e.message}")
            } finally {
                delay(3.seconds)
                logOutState = LogOutUiState.Idle
            }
        }
    }
}