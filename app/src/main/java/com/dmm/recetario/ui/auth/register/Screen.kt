package com.dmm.recetario.ui.auth.register

import androidx.compose.animation.AnimatedVisibility
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
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background (
                Brush.verticalGradient (
                    colors = listOf (
                        Color(0xFF121212),
                        Color(0xFF1E1E1E),
                        Color(0xFF252525)
                    )
                )
            )
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        RegisterContent (
            uiState = viewModel.uiState,
            onRegister = viewModel::register,
            onRetry = viewModel::resetToIdle,
            onNavigateToHome = onNavigateToHome,
            onNavigateToLogin = onNavigateToLogin
        )
    }
}

@Composable
fun RegisterContent (
    uiState: RegisterUiState,
    onRegister: (String, String, String, String?, String?) -> Unit,
    onRetry: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    LaunchedEffect(uiState) {
        if (uiState is RegisterUiState.Success) {
            onNavigateToHome()
        }
    }

    when (uiState) {
        is RegisterUiState.Loading -> CircularProgressIndicator()
        is RegisterUiState.Error -> {
            ErrorScreen (
                message = uiState.message,
                onRetry = onRetry
            )
        }
        else -> {
            RegisterForm (
                onRegister = onRegister,
                onNavigateToLogin = onNavigateToLogin
            )
        }
    }
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
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A2A)),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon (
                    imageVector = Icons.Default.PersonAdd,
                    contentDescription = null,
                    tint = Color(0xFF00C2FF),
                    modifier = Modifier.size(72.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text (
                    text = stringResource(R.string.create_account),
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text (
                    text = stringResource(R.string.signup_suggest),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.LightGray
                )

                Spacer(modifier = Modifier.height(32.dp))

                Card (
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                    elevation = CardDefaults.cardElevation( defaultElevation = 10.dp)
                ) {
                    Column (
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        // Nombre
                        OutlinedTextField (
                            value = name,
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
                            },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Email
                        OutlinedTextField (
                            value = email,
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
                            },
                            keyboardOptions = KeyboardOptions.Default
                                .copy(keyboardType = KeyboardType.Email),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Password
                        OutlinedTextField (
                            value = password,
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
                                        imageVector =
                                            if(passwordVisible)
                                                Icons.Default.Visibility
                                            else
                                                Icons.Default.VisibilityOff,
                                        contentDescription = null
                                    )
                                }
                            },
                            visualTransformation =
                                if(passwordVisible)
                                    VisualTransformation.None
                                else
                                    PasswordVisualTransformation(),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Teléfono
                        OutlinedTextField (
                            value = phone,
                            onValueChange = {
                                phone = it
                            },
                            label = {
                                Text(stringResource(R.string.number_phone))
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Phone, null)
                            },
                            keyboardOptions = KeyboardOptions.Default
                                .copy(keyboardType = KeyboardType.Phone),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Username
                        OutlinedTextField (
                            value = username,
                            onValueChange = {
                                username = it
                            },
                            label = {
                                Text(stringResource(R.string.username))
                            },
                            leadingIcon = {
                                Icon(Icons.Default.AlternateEmail, null)
                            },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Botón principal
                        Button(
                            onClick = {
                                keyboardController?.hide()

                                onRegister (
                                    name,
                                    email,
                                    password,
                                    phone.ifBlank { null },
                                    username.ifBlank { null }
                                )
                            },
                            enabled =
                                name.isNotBlank() &&
                                email.isNotBlank() &&
                                password.isNotBlank(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(18.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C2FF))
                        ) {
                            Icon (
                                Icons.Default.Check,
                                contentDescription = null
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text (
                                text = stringResource(R.string.create_account),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color.Black
                            )
                        }

                        HorizontalDivider(color = Color.Gray.copy(alpha = 0.3f))

                        // Navegar al login
                        TextButton (
                            modifier = Modifier.fillMaxWidth(),
                            onClick = onNavigateToLogin
                        ) {
                            Text (
                                text = stringResource(R.string.redirect_to_login),
                                color = Color(0xFF00C2FF)
                            )
                        }
                    }
                }
            }
        }
    }
}