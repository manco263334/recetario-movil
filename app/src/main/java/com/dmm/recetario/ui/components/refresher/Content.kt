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
    onRefresh: suspend () -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    viewModel: RefresherViewModel = hiltViewModel(),
    content: @Composable (BoxScope.() -> Unit)
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
        isRefreshing = uiState is RefresherUiState.Loading,
        state = pullToRefreshState,
        onRefresh = { viewModel.refresh(onRefresh) },
        modifier = modifier,
        content = content
    )
}