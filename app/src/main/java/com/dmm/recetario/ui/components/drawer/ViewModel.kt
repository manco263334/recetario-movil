package com.dmm.recetario.ui.components.drawer

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmm.recetario.R
import com.dmm.recetario.core.utils.helper.ResourceHelper
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
import kotlinx.coroutines.launch

@HiltViewModel
class DrawerViewModel @Inject constructor (
    private val authService: AuthService,
    private val tokenManager: TokenManager,
    private val resourceHelper: ResourceHelper,
    private val userDao: UserDao<UserEntity, TokenUserRef, RecipeEntity>
) : ViewModel() {
    private val getString: (Int) -> String = resourceHelper::getString

    var logOutState: LogOutUiState by mutableStateOf(LogOutUiState.Idle)
        private set

    fun logout() {
        if (logOutState is LogOutUiState.Loading) return

        viewModelScope.launch {
            logOutState = LogOutUiState.Loading

            try {
                authService.logout()

                awaitAll (
                    async { tokenManager.clearToken() },
                    async { userDao.clear() },
                    async { userDao.clearTokenRefs() }
                )

                logOutState = LogOutUiState.Success (
                    resourceHelper.getString(R.string.logout_succeed)
                )
            } catch (e: Exception) {
                val message = getString(R.string.logout_failed)
                Log.w("DrawerViewModel", "$message: ${e.message}")
                logOutState = LogOutUiState.Error(message)
            }
        }
    }
}