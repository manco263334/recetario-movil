package com.dmm.recetario.ui.components

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.dmm.recetario.core.utils.extension.isNeitherNullNorAnonymous
import com.dmm.recetario.core.utils.helper.ResourceHelper
import com.dmm.recetario.domain.model.User
import com.dmm.recetario.ui.components.drawer.DrawerContent
import com.dmm.recetario.ui.components.fab.FAB
import com.dmm.recetario.ui.components.refresher.PullToRefresh

@Composable
fun BaseLayout (
    user: User?,
    resourceHelper: ResourceHelper,
    showFab: Boolean = user.isNeitherNullNorAnonymous(),
    drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onHomeClick: () -> Unit,
    onCompleteForm: () -> Unit,
    onSettingsClick: () -> Unit,
    onLogOutSuccess: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    ModalNavigationDrawer (
        drawerState = drawerState,
        drawerContent = {
            DrawerContent (
                user = user,
                drawerState = drawerState,
                snackbarHostState = snackbarHostState,
                onHomeClick = onHomeClick,
                onSettingsClick = onSettingsClick,
                onLogOutSuccess = onLogOutSuccess
            )
        }
    ) {
        Scaffold (
            topBar = {
                Toolbar(drawerState = drawerState, resourceHelper = resourceHelper) {
                    WelcomeHeader(user = user, resourceHelper = resourceHelper)
                }
            },
            floatingActionButton = {
                if (showFab) {
                    FAB(onCompleteForm = onCompleteForm, resourceHelper = resourceHelper)
                }
            },
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
            content = content
        )
    }
}

@Composable
fun BaseLayoutWithRefresh (
    user: User?,
    resourceHelper: ResourceHelper,
    showFab: Boolean = user.isNeitherNullNorAnonymous(),
    drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onHomeClick: () -> Unit,
    onCompleteForm: () -> Unit,
    onSettingsClick: () -> Unit,
    onLogOutSuccess: () -> Unit,
    onRefresh: suspend () -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    BaseLayout (
        user = user,
        showFab = showFab,
        drawerState = drawerState,
        resourceHelper = resourceHelper,
        snackbarHostState = snackbarHostState,
        onHomeClick = onHomeClick,
        onCompleteForm = onCompleteForm,
        onSettingsClick = onSettingsClick,
        onLogOutSuccess = onLogOutSuccess
    ) { paddingValues ->
        PullToRefresh (
            onRefresh = onRefresh,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            snackbarHostState = snackbarHostState,
            content = content
        )
    }
}