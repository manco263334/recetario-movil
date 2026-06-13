package com.dmm.recetario.ui.settings

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dmm.recetario.ui.components.BaseLayout
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen (
    viewModel: SettingViewModel = hiltViewModel(),
    onHomeClick: () -> Unit,
    onLogOutSuccess: () -> Unit
) {
    val uiState = viewModel.uiState
    val user by viewModel.user.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    BaseLayout (
        user = user,
        showFab = false,
        drawerState = drawerState,
        onHomeClick = onHomeClick,
        onCompleteForm = {},
        onLogOutSuccess = onLogOutSuccess,
        onSettingsClick = {
            scope.launch {
                drawerState.close()
            }
        },
    ) { paddingValues ->
        Crossfade (
            targetState = uiState,
            label = "settings_crossfade"
        ) { state ->
            if (state is SettingsUiState.Loading) {
                SettingsContentSkeleton(paddingValues, state.message)
            } else {
                SettingsContent(paddingValues)
            }
        }
    }
}

@Composable
fun SettingsContent (paddingValues: PaddingValues) {

}

@Composable
fun SettingsContentSkeleton (
    paddingValues: PaddingValues,
    loadingMessage: String
) {

}