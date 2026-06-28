package com.dmm.recetario.ui.auth.login

import android.util.Log
import android.util.Patterns.EMAIL_ADDRESS
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
import com.dmm.recetario.domain.model.LoginData
import com.dmm.recetario.domain.service.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor (
    private val service: AuthService,
    private val userManager: UserManager,
    private val tokenManager: TokenManager,
    private val resourceHelper: ResourceHelper,
    private val dao: UserDao<UserEntity, TokenUserRef, RecipeEntity>
) : ViewModel() {
    var uiState: LoginUiState by mutableStateOf(LoginUiState.Idle)
        private set

    var data by mutableStateOf(LoginData("", ""))
        private set

    fun isDataValid(): Boolean {
        val validations = arrayOf (
            data.email.isNotBlank(),
            EMAIL_ADDRESS.matcher(data.email).matches(),
            data.password.isNotBlank(),
            data.password.length >= 8
        )

        return validations.all { it }
    }

    fun login() {
        if (uiState is LoginUiState.Loading) return

        viewModelScope.launch {
            uiState = LoginUiState.Loading

            try {
                if (!isDataValid()) {
                    uiState = LoginUiState.Error (
                        resourceHelper.getString(R.string.something_went_wrong)
                    )
                    return@launch
                }

                val data = LoginData(data.email, data.password)
                val response = service.login(data)
                val token = response.token

                awaitAll (
                    async { saveTokenToPreferences(token) },
                    async { insertTokenReference(token, data.email) },
                )

                syncUserLocally()

                uiState = LoginUiState.Success(token)
            } catch (e: Exception) {
                val errorMessage = e.message
                    ?: resourceHelper.getString(R.string.something_went_wrong)

                Log.d (
                    "LoginViewModel",
                    resourceHelper.getString (
                        R.string.error_fetching_data,
                        errorMessage
                    ),
                    e
                )

                uiState = LoginUiState.Error (
                    resourceHelper.getString(R.string.something_went_wrong)
                )
            }
        }
    }

    fun updateEmail(email: String) {
        data = data.copy(email = email)
    }

    fun updatePassword(password: String) {
        data = data.copy(password = password)
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