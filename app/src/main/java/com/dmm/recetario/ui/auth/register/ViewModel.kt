package com.dmm.recetario.ui.auth.register

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmm.recetario.R
import com.dmm.recetario.core.utils.helper.ResourceHelper
import com.dmm.recetario.data.local.database.entity.TokenUserRefImpl
import com.dmm.recetario.domain.dao.UserDao
import com.dmm.recetario.domain.entity.RecipeEntity
import com.dmm.recetario.domain.entity.TokenUserRef
import com.dmm.recetario.domain.entity.UserEntity
import com.dmm.recetario.domain.manager.TokenManager
import com.dmm.recetario.domain.manager.UserManager
import com.dmm.recetario.domain.model.RegisterData
import com.dmm.recetario.domain.service.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

@HiltViewModel
class RegisterViewModel @Inject constructor (
    private val service: AuthService,
    private val userManager: UserManager,
    private val tokenManager: TokenManager,
    private val resourceHelper: ResourceHelper,
    private val dao: UserDao<UserEntity, TokenUserRef, RecipeEntity>
) : ViewModel() {
    var uiState: RegisterUiState by mutableStateOf(RegisterUiState.Idle)
        private set

    fun register (
        name: String,
        email: String,
        password: String,
        phone: String?,
        username: String?
    ) {
        if (uiState is RegisterUiState.Loading) return

        viewModelScope.launch {
            uiState = RegisterUiState.Loading

            try {
                val data = RegisterData (
                    name = name,
                    email = email,
                    phone = phone,
                    password = password,
                    username = username
                )
                val response = service.register(data)
                val token = response.token

                awaitAll (
                    async { saveTokenToPreferences(token) },
                    async { insertTokenReference(token, email) },
                )

                syncUserLocally()

                uiState = RegisterUiState.Success(token)
            } catch (e: Exception) {
                Log.d("RegisterViewModel", "Error: ${e.message}", e)
                uiState = RegisterUiState.Error (
                    resourceHelper.getString(R.string.something_went_wrong)
                )
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
        dao.insertTokenRef(TokenUserRefImpl(token, email))
    }

    fun resetToIdle () {
        uiState = RegisterUiState.Idle
    }
}