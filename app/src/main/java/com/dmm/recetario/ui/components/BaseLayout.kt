package com.dmm.recetario.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.dmm.recetario.core.utils.extension.isNeitherNullNorAnonymous
import com.dmm.recetario.domain.model.User
import com.dmm.recetario.ui.components.drawer.DrawerContent
import com.dmm.recetario.ui.components.fab.FAB

@Composable
fun BaseLayout (
    user: User?,
    drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onSettingsClick: (User?) -> Unit,
    onLogOutSuccess: () -> Unit,
    onCompleteForm: () -> Unit,
    onHomeClick: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    ModalNavigationDrawer (
        drawerState = drawerState,
        drawerContent = {
            DrawerContent (
                drawerState = drawerState,
                user = user,
                snackbarHostState = snackbarHostState,
                onSettingsClick = onSettingsClick,
                onLogOutSuccess = onLogOutSuccess,
                onHomeClick = onHomeClick,
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