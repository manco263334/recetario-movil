package com.dmm.recetario.ui.auth.register

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
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Phone
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
fun RegisterScreen (
    viewModel: RegisterViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit,
    onNavigateToLogin: () -> Unit,
) {
    val uiState = viewModel.uiState

    LaunchedEffect(uiState) {
        if (uiState is RegisterUiState.Success) {
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
            label = "register_crossfade"
        ) { state ->
            when (state) {
                is RegisterUiState.Loading -> CircularProgressIndicator()
                is RegisterUiState.Error -> {
                    ErrorScreen (
                        message = state.message,
                        onRetry = viewModel::resetToIdle
                    )
                }
                else -> {
                    RegisterContent (
                        onRegister = viewModel::register,
                        onNavigateToLogin = onNavigateToLogin
                    )
                }
            }
        }
    }
}

@Composable
fun RegisterContent (
    onNavigateToLogin: () -> Unit,
    onRegister: (String, String, String, String?, String?) -> Unit
) {
    RegisterForm (
        onRegister = onRegister,
        onNavigateToLogin = onNavigateToLogin
    )
}

@Composable
private fun RegisterForm (
    onNavigateToLogin: () -> Unit,
    onRegister: (String, String, String, String?, String?) -> Unit
) {
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var username by rememberSaveable { mutableStateOf("") }
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
                    imageVector = Icons.Default.PersonAdd
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text (
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    text = stringResource(R.string.create_account),
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text (
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    text = stringResource(R.string.signup_suggest)
                )

                Spacer(modifier = Modifier.height(32.dp))

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
                            value = name,
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            onValueChange = {
                                name = it
                            },
                            label = {
                                Text(stringResource(R.string.name))
                            },
                            placeholder = {
                                Text(stringResource(R.string.name_placeholder))
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Person, null)
                            }
                        )

                        OutlinedTextField (
                            value = email,
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions.Default
                                .copy(keyboardType = KeyboardType.Email),
                            onValueChange = {
                                email = it
                            },
                            label = {
                                Text(stringResource(R.string.email))
                            },
                            placeholder = {
                                Text(stringResource(R.string.email_placeholder))
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Email, null)
                            }
                        )

                        OutlinedTextField (
                            value = password,
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            visualTransformation =
                                if(passwordVisible)
                                    VisualTransformation.None
                                else
                                    PasswordVisualTransformation(),
                            onValueChange = {
                                password = it
                            },
                            label = {
                                Text(stringResource(R.string.password))
                            },
                            placeholder = {
                                Text(stringResource(R.string.password_placeholder))
                            },
                            supportingText = {
                                Text(stringResource(R.string.password_guide))
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

                        OutlinedTextField (
                            value = phone,
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions.Default
                                .copy(keyboardType = KeyboardType.Phone),
                            onValueChange = {
                                phone = it
                            },
                            label = {
                                Text(stringResource(R.string.number_phone))
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Phone, null)
                            }
                        )

                        OutlinedTextField (
                            value = username,
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            onValueChange = {
                                username = it
                            },
                            label = {
                                Text(stringResource(R.string.username))
                            },
                            leadingIcon = {
                                Icon(Icons.Default.AlternateEmail, null)
                            }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Button (
                            shape = RoundedCornerShape(18.dp),
                            colors = ButtonDefaults.buttonColors (
                                containerColor = Color(0xFF00C2FF)
                            ),
                            enabled =
                                name.isNotBlank() &&
                                email.isNotBlank() &&
                                password.isNotBlank(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            onClick = {
                                keyboardController?.hide()

                                onRegister (
                                    name,
                                    email,
                                    password,
                                    phone.ifBlank { null },
                                    username.ifBlank { null }
                                )
                            }
                        ) {
                            Icon (
                                Icons.Default.Check,
                                contentDescription = null
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text (
                                fontSize = 16.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                text = stringResource(R.string.create_account)
                            )
                        }

                        HorizontalDivider(color = MaterialTheme.colorScheme.onSurfaceVariant)

                        TextButton (
                            modifier = Modifier.fillMaxWidth(),
                            onClick = onNavigateToLogin
                        ) {
                            Text (
                                text = stringResource(R.string.redirect_to_login)
                            )
                        }
                    }
                }
            }
        }
    }
}