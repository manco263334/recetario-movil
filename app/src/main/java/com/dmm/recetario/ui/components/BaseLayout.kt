package com.dmm.recetario.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.dmm.recetario.core.utils.extension.isNeitherNullNorAnonymous
import com.dmm.recetario.domain.model.User
import com.dmm.recetario.ui.components.drawer.DrawerContent
import com.dmm.recetario.ui.components.fab.FAB
import kotlinx.coroutines.launch

@Composable
fun BaseLayout (
    user: User?,
    snackbarHostState: SnackbarHostState,
    drawerState: DrawerState,
    onSettingsClick: (User?) -> Unit,
    onLogOutSuccess: () -> Unit,
    onCompleteForm: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer (
        drawerState = drawerState,
        drawerContent = {
            DrawerContent (
                drawerState = drawerState,
                user = user,
                snackbarHostState = snackbarHostState,
                onSettingsClick = onSettingsClick,
                onLogOutSuccess = onLogOutSuccess,
                onHomeClick = {
                    scope.launch {
                        drawerState.close()
                    }
                },
            )
        }
    ) {
        Scaffold (
            topBar = {
                Toolbar (
                    drawerState = drawerState
                ) {
                    WelcomeHeader(user)
                }
            },
            floatingActionButton = {
                if (user.isNeitherNullNorAnonymous()) {
                    FAB(onCompleteForm = onCompleteForm)
                }
            },
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
            content = content
        )
    }
}