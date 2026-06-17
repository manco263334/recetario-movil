package com.dmm.recetario.ui.components.refresher

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun PullToRefresh (
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    viewModel: RefresherViewModel = hiltViewModel(),
    onRefresh: suspend () -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    val uiState = viewModel.uiState
    val pullToRefreshState = rememberPullToRefreshState()

    LaunchedEffect(uiState) {
        if (uiState is RefresherUiState.Error) {
            snackbarHostState.showSnackbar(uiState.message)
        } else if (uiState is RefresherUiState.Success) {
            snackbarHostState.showSnackbar(uiState.message)
        }
    }

    PullToRefreshBox (
        modifier = modifier,
        state = pullToRefreshState,
        isRefreshing = uiState is RefresherUiState.Loading,
        onRefresh = {
            viewModel.refresh(onRefresh)
        },
        content = content
    )
}