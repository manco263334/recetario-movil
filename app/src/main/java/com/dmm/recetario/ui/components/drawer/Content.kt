package com.dmm.recetario.ui.components.drawer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.dmm.recetario.R
import com.dmm.recetario.domain.model.User
import kotlinx.coroutines.launch

@Composable
fun DrawerContent (
    user: User?,
    drawerState: DrawerState,
    snackbarHostState: SnackbarHostState,
    onHomeClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onLogOutSuccess: () -> Unit,
    viewModel: DrawerViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val logOutState = viewModel.logOutState

    LaunchedEffect(Unit) {
        drawerState.close()
    }

    LaunchedEffect (logOutState) {
        if (logOutState is LogOutUiState.Success) {
            onLogOutSuccess()
            snackbarHostState.showSnackbar(logOutState.message)
        } else if (logOutState is LogOutUiState.Error) {
            snackbarHostState.showSnackbar(logOutState.message)
        }
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton (
                onClick = {
                    scope.launch {
                        drawerState.close()
                    }
                }
            ) {
                Icon (
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cerrar menú",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        AsyncImage (
            model = user?.icon,
            contentDescription = "User Photo",
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .border (
                    2.dp,
                    Color(0xFF00C2FF),
                    CircleShape
                ),
            error = painterResource(R.drawable.user_default_icon),
            fallback = painterResource(R.drawable.user_default_icon)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text (
                text = user?.name ?: "Anónimo",
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text (
                text = user?.email ?: "Sin email",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Text (
                text = user?.username ?: "Sin apodo",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Text (
                text = if (user?.phone.isNullOrBlank()) "Sin número telefónico"
                    else user.phone,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        NavigationDrawerItem (
            label = {
                Text("Inicio")
            },
            selected = false,
            onClick = onHomeClick,
            icon = {
                Icon(Icons.Default.Home, null)
            },
            shape = RoundedCornerShape(16.dp)
        )

        NavigationDrawerItem (
            label = {
                Text("Ajustes")
            },
            selected = false,
            onClick = onSettingsClick,
            icon = {
                Icon(Icons.Default.Settings, null)
            },
            shape = RoundedCornerShape(16.dp)
        )

        NavigationDrawerItem (
            label = {
                Text("Cerrar sesión")
            },
            selected = false,
            onClick = viewModel::logout,
            icon = {
                Icon(Icons.AutoMirrored.Filled.ExitToApp, null)
            },
            shape = RoundedCornerShape(16.dp)
        )

        if (logOutState is LogOutUiState.Loading) {
            CircularProgressIndicator()
        }
    }
}