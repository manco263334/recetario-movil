package com.dmm.recetario.ui.auth.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dmm.recetario.R
import com.dmm.recetario.ui.components.ErrorScreen

@Composable
fun LoginScreen (
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val uiState = viewModel.uiState

    LaunchedEffect(uiState) {
        if (uiState is LoginUiState.Success) {
            onNavigateToHome()
        }
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background (
                Brush.verticalGradient (
                    colors = listOf (
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.background.copy(alpha = 0.5f),
                        MaterialTheme.colorScheme.background.copy(alpha = 0.75f)
                    )
                )
            )
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Crossfade (
            targetState = uiState,
            label = "login_crossfade"
        ) { state ->
            when (state) {
                is LoginUiState.Loading -> CircularProgressIndicator()
                is LoginUiState.Error -> {
                    ErrorScreen (
                        message = state.message,
                        onRetry = viewModel::resetToIdle
                    )
                }
                else -> {
                    LoginContent (
                        onLogin = viewModel::login,
                        onLoginAsGuest = viewModel::loginAsGuest,
                        onNavigateToRegister = onNavigateToRegister
                    )
                }
            }
        }
    }
}

@Composable
private fun LoginContent (
    onLoginAsGuest: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onLogin: (String, String) -> Unit
) {
    LoginForm (
        onLogin = onLogin,
        onLoginAsGuest = onLoginAsGuest,
        onNavigateToRegister = onNavigateToRegister
    )
}

@Composable
private fun LoginForm (
    onLoginAsGuest: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onLogin: (String, String) -> Unit
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()

    AnimatedVisibility (
        visible = true,
        enter = fadeIn() + slideInVertically()
    ) {
        Card (
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors (
                MaterialTheme.colorScheme.surface
            )
        ) {
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon (
                    tint = Color.Cyan,
                    contentDescription = null,
                    modifier = Modifier.size(72.dp),
                    imageVector = Icons.Default.Lock
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text (
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    text = stringResource(R.string.login_title),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text (
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    text = stringResource(R.string.login_subtitle)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Card (
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                    colors = CardDefaults.cardColors (
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column (
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        OutlinedTextField (
                            value = email,
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions.Default
                                .copy(keyboardType = KeyboardType.Email),
                            colors = TextFieldDefaults.colors (
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                cursorColor = MaterialTheme.colorScheme.onBackground,
                                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                                unfocusedTextColor = MaterialTheme.colorScheme.onBackground
                            ),
                            onValueChange = {
                                email = it
                            },
                            placeholder = {
                                Text(stringResource(R.string.email_placeholder))
                            },
                            label = {
                                Text(stringResource(R.string.email))
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Email, null)
                            }
                        )

                        OutlinedTextField (
                            value = password,
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions.Default
                                .copy(keyboardType = KeyboardType.Password),
                            visualTransformation = if (passwordVisible)
                                VisualTransformation.None
                            else
                                PasswordVisualTransformation(),
                            onValueChange = {
                                password = it
                            },
                            label = {
                                Text(stringResource(R.string.password))
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Lock, null)
                            },
                            trailingIcon = {
                                IconButton (
                                    onClick = {
                                        passwordVisible = !passwordVisible
                                    }
                                ) {
                                    Icon (
                                        contentDescription = null,
                                        imageVector =
                                            if(passwordVisible)
                                                Icons.Default.Visibility
                                            else
                                                Icons.Default.VisibilityOff
                                    )
                                }
                            }
                        )

                        Button (
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C2FF)),
                            onClick = {
                                keyboardController?.hide()
                                onLogin(email, password)
                            }
                        ) {
                            Text (
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                text = stringResource(R.string.login_button),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        HorizontalDivider(color = MaterialTheme.colorScheme.onSurfaceVariant)

                        OutlinedButton (
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            onClick = onLoginAsGuest
                        ) {
                            Icon (
                                contentDescription = null,
                                imageVector = Icons.Default.Person
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(stringResource(R.string.login_guest))
                        }

                        HorizontalDivider(color = MaterialTheme.colorScheme.onSurfaceVariant)

                        OutlinedButton (
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            onClick = {  }
                        ) {
                            Icon(Icons.Default.AccountCircle, null)

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(stringResource(R.string.login_google))
                        }

                        HorizontalDivider(color = MaterialTheme.colorScheme.onSurfaceVariant)

                        TextButton(onClick = onNavigateToRegister) {
                            Text(stringResource(R.string.redirect_to_signup))
                        }

                        HorizontalDivider(color = MaterialTheme.colorScheme.onSurfaceVariant)

                        TextButton(onClick = { }) {
                            Text(stringResource(R.string.password_forgotten))
                        }
                    }
                }
            }
        }
    }
}