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
import androidx.compose.foundation.layout.visible
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
import androidx.compose.ui.res.stringResource
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
    viewModel: DrawerViewModel = hiltViewModel(),
    onHomeClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onLogOutSuccess: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val logOutState = viewModel.logOutState

    LaunchedEffect (logOutState) {
        if (logOutState is LogOutUiState.Success) {
            onLogOutSuccess()
        } else if (logOutState is LogOutUiState.Error) {
            snackbarHostState.showSnackbar(logOutState.message)
        }
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
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
                    tint = MaterialTheme.colorScheme.onBackground,
                    contentDescription = stringResource(R.string.close_menu)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        AsyncImage (
            model = user?.icon,
            contentDescription = stringResource(R.string.user_photo_description),
            error = painterResource(R.drawable.user_default_icon),
            fallback = painterResource(R.drawable.user_default_icon),
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .border (
                    2.dp,
                    Color(0xFF00C2FF),
                    CircleShape
                )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text (
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                text = user?.name ?: stringResource(R.string.anonymous_text)
            )

            Text (
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                text = user?.email ?: stringResource(R.string.anonymous_email)
            )

            Text (
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                text = user?.username ?: stringResource(R.string.anonymous_username)
            )

            Text (
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                text = if (user?.phone.isNullOrBlank())
                        stringResource(R.string.anonymous_phone)
                    else
                        user.phone
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        NavigationDrawerItem (
            selected = false,
            shape = RoundedCornerShape(16.dp),
            onClick = onHomeClick,
            label = {
                Text(stringResource(R.string.home))
            },
            icon = {
                Icon(Icons.Default.Home, null)
            }
        )

        NavigationDrawerItem (
            selected = false,
            shape = RoundedCornerShape(16.dp),
            onClick = onSettingsClick,
            label = {
                Text(stringResource(R.string.settings))
            },
            icon = {
                Icon(Icons.Default.Settings, null)
            }
        )

        NavigationDrawerItem (
            selected = false,
            shape = RoundedCornerShape(16.dp),
            onClick = viewModel::logout,
            label = {
                Text(stringResource(R.string.logout))
            },
            icon = {
                Icon(Icons.AutoMirrored.Filled.ExitToApp, null)
            }
        )

        CircularProgressIndicator (
            modifier = Modifier.visible(logOutState is LogOutUiState.Loading)
        )
    }
}