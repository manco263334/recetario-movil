package com.dmm.recetario.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dmm.recetario.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Toolbar (
    drawerState: DrawerState,
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        if (drawerState.isOpen) {
            drawerState.close()
        }
    }

    TopAppBar (
        title = title,
        modifier = modifier
            .background(MaterialTheme.colorScheme.secondary)
            .padding(16.dp),
        navigationIcon = {
            IconButton (
                onClick = {
                    scope.launch {
                        drawerState.open()
                    }
                }
            ) {
                Icon (
                    imageVector = Icons.Default.Menu,
                    contentDescription = stringResource(R.string.open_menu)
                )
            }
        }
    )
}