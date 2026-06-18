package com.dmm.recetario.ui.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmm.recetario.R
import com.dmm.recetario.core.utils.helper.ResourceHelper
import com.dmm.recetario.domain.manager.TokenManager
import com.dmm.recetario.domain.manager.UserManager
import com.dmm.recetario.domain.model.User
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
class SettingViewModel @Inject constructor (
    private val userManager: UserManager,
    private val tokenManager: TokenManager,
    private val resourceHelper: ResourceHelper
) : ViewModel() {
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
}