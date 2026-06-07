package com.dmm.recetario.ui.auth.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmm.recetario.data.local.database.entity.TokenUserRefImpl
import com.dmm.recetario.domain.dao.UserDao
import com.dmm.recetario.domain.entity.RecipeEntity
import com.dmm.recetario.domain.entity.TokenUserRef
import com.dmm.recetario.domain.entity.UserEntity
import com.dmm.recetario.domain.manager.TokenManager
import com.dmm.recetario.domain.manager.UserManager
import com.dmm.recetario.domain.repository.LoginData
import com.dmm.recetario.domain.service.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor (
    private val service: AuthService,
    private val tokenManager: TokenManager,
    private val userManager: UserManager,
    private val dao: UserDao<UserEntity, TokenUserRef, RecipeEntity>
): ViewModel() {
    var uiState by mutableStateOf<LoginUiState>(LoginUiState.Idle)
        private set

    fun login (
        email: String,
        password: String
    ) {
        uiState = LoginUiState.Loading

        viewModelScope.launch {
            try {
                val data = LoginData(email, password)
                val response = service.login(data)
                val token = response.token

                awaitAll (
                    async { saveTokenToPreferences(token) },
                    async { insertTokenReference(token, email) },
                )

                syncUserLocally()

                uiState = LoginUiState.Success(token)
            } catch (e: Exception) {
                uiState = LoginUiState.Error("Error: ${e.message}")
            }
        }
    }

    private suspend fun syncUserLocally() {
        userManager.syncUser()
    }

    private suspend fun saveTokenToPreferences(token: String) {
        tokenManager.saveToken(token)
    }

    private suspend fun insertTokenReference(token: String, email: String) {
        dao.insertTokenRefs(listOf(TokenUserRefImpl(token, email)))
    }

    fun loginAsGuest() {
        viewModelScope.launch {
            val token = ""
            saveTokenToPreferences(token)

            uiState = LoginUiState.Success(token)
        }
    }

    fun resetToIdle() {
        uiState = LoginUiState.Idle
    }
}