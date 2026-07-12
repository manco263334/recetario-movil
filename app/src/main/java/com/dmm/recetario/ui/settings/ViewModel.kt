package com.dmm.recetario.ui.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.dmm.recetario.R
import com.dmm.recetario.core.utils.helper.ResourceHelper
import com.dmm.recetario.domain.manager.PreferenceManager
import com.dmm.recetario.domain.manager.TokenManager
import com.dmm.recetario.domain.manager.UserManager
import com.dmm.recetario.domain.model.User
import com.dmm.recetario.domain.service.UserService
import com.dmm.recetario.ui.core.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class SettingsViewModel @Inject constructor (
    private val userManager: UserManager,
    private val userService: UserService,
    private val tokenManager: TokenManager,
    private val resourceHelper: ResourceHelper,
    private val preferenceManager: PreferenceManager
) : BaseViewModel() {
    var uiState: SettingsUiState by mutableStateOf (
        SettingsUiState.Loading (
            resourceHelper.getString(R.string.loading_user)
        )
    )
        private set

    private val _token = tokenManager.token

    @OptIn(ExperimentalCoroutinesApi::class)
    val user: StateFlow<User?> = _token
        .flatMapLatest { token ->
            val user = userManager.getUserLocal(token).firstOrNull()
            flowOf(user).also { uiState = SettingsUiState.Success }
        }
        .stateIn (
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    suspend fun refresh() {
        if (uiState is SettingsUiState.Loading) return

        uiState = SettingsUiState.Loading (
            resourceHelper.getString(R.string.loading_user)
        )

        val user = user.firstOrNull()

        if (user == null) {
            uiState = SettingsUiState.Error (
                resourceHelper.getString(R.string.error_syncing_user)
            )
            return
        }

        val result = userService.syncUser(user.id)

        if (result) {
            uiState = SettingsUiState.Success
        } else {
            val message = resourceHelper.getString(R.string.error_syncing_user)
            uiState = SettingsUiState.Error(message)
            throw Exception(message)
        }
    }
}