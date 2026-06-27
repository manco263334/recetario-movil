package com.dmm.recetario.ui.settings

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.dmm.recetario.R
import com.dmm.recetario.core.access_hardware.openCamera
import com.dmm.recetario.core.access_hardware.openGallery
import com.dmm.recetario.core.utils.extension.createImageFile
import com.dmm.recetario.core.utils.extension.isNeitherNullNorAnonymous
import com.dmm.recetario.domain.model.User
import com.dmm.recetario.ui.components.ShowImagePickerDialog

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel()) {
    val uiState = viewModel.uiState
    val user by viewModel.user.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    Crossfade (
        targetState = uiState,
        label = "settings_crossfade"
    ) { state ->
        if (state is SettingsUiState.Loading) {
            SettingsContentSkeleton(loadingMessage =  state.message)
        } else {
            SettingsContent (
                user = user,
                context = context,
                snackbarHostState = snackbarHostState
            )
        }
    }
}

@Composable
fun SettingsContent (
    user: User?,
    context: Context,
    snackbarHostState: SnackbarHostState
) {
    val getString: (Int) -> String = context::getString
    val canEditInfo = user.isNeitherNullNorAnonymous()

    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile (
        context,
        "com.dmm.recetario.provider",
        file
    )

    var showDialog by rememberSaveable { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    var name by rememberSaveable {
        mutableStateOf (
        user?.name ?: getString(R.string.anonymous_text)
        )
    }
    var email by rememberSaveable {
        mutableStateOf (
        user?.email ?: getString(R.string.anonymous_email)
        )
    }
    var username by rememberSaveable {
        mutableStateOf (
            user?.username ?: getString(R.string.anonymous_username)
        )
    }
    var phone by rememberSaveable {
        mutableStateOf (
            user?.phone ?: getString(R.string.anonymous_phone)
        )
    }
    var profilePictureUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var isUploading by remember { mutableStateOf(false) }

    val galleryLauncher = openGallery {
        it?.let { uri ->
            profilePictureUri = uri
        }
    }

    val cameraLauncher = openCamera { success ->
        if (success) {
            profilePictureUri = uri
        } else {
            snackbarHostState.showSnackbar (
                message = getString (
                    R.string.error_taking_photo
                )
            )
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult (
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cameraLauncher.launch(uri)
        } else {
            // Handle permission denied logic
        }
    }

    val galleryPermissionLauncher = rememberLauncherForActivityResult (
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            galleryLauncher.launch("image/*")
        } else {
            // Handle permission denied logic
        }
    }

    Box (
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(scrollState),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box (
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(140.dp)
                    .background (
                        shape = CircleShape,
                        color = Color.Gray,
                    )
                    .border (
                        width = 3.dp,
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    .clickable { showDialog = true }
            ) {
                if (profilePictureUri != null) {
                    AsyncImage (
                        model = profilePictureUri,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                        contentDescription = stringResource (
                            R.string.profile_picture_description
                        )
                    )
                } else {
                    Text (
                        fontSize = 14.sp,
                        color = Color.White,
                        text = stringResource(R.string.add_photo)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            CustomOutlinedTextField (
                value = name,
                canEdit = canEditInfo,
                label = getString(R.string.name),
                onValueChange = { name = it }
            )

            CustomOutlinedTextField (
                value = email,
                canEdit = canEditInfo,
                label = getString(R.string.email),
                keyboardType = KeyboardType.Email,
                onValueChange = { email = it }
            )

            CustomOutlinedTextField (
                value = username,
                canEdit = canEditInfo,
                label = getString(R.string.username),
                onValueChange = { username = it }
            )

            CustomOutlinedTextField (
                value = phone,
                canEdit = canEditInfo,
                keyboardType = KeyboardType.Phone,
                label = getString(R.string.number_phone),
                onValueChange = { phone = it }
            )

            if (canEditInfo) {
                Spacer(modifier = Modifier.height(20.dp))

                Button (
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors (
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .shadow(8.dp, RoundedCornerShape(12.dp)),
                    onClick = {
//                    user.let {
//                        it.name = name
//                        it.phone = phone
//                        it.email = email
//                    }
//                    scope.launch(Dispatchers.IO) {
//                        val updateResponse = UserController(context).updateByID(user.id, user)
//                        withContext(Dispatchers.Main) {
//                            Toast.makeText(context, updateResponse.message!!, Toast.LENGTH_SHORT).show()
//                            if (updateResponse.ok) {
//                                onSettingsClick(user)
//                            }
//                        }
//                    }
                    }
                ) {
                    if (isUploading) {
                        CircularProgressIndicator (
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    } else {
                        Text (
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            text = stringResource(R.string.save_changes)
                        )
                    }
                }
            }
        }
    }

    if (showDialog) {
        ShowImagePickerDialog (
            onDismiss = { showDialog = false },
            onCameraClick = {
                val permissionCheck = ContextCompat.checkSelfPermission (
                    context,
                    Manifest.permission.CAMERA
                )

                if (permissionCheck == PackageManager.
                    PERMISSION_GRANTED) {
                    cameraLauncher.launch(uri)
                } else {
                    cameraPermissionLauncher.
                        launch(Manifest.permission.CAMERA)
                }
            },
            onGalleryClick = {
                val permissionCheck = ContextCompat.checkSelfPermission (
                    context,
                    Manifest.permission.READ_MEDIA_IMAGES
                )

                if (permissionCheck == PackageManager.
                    PERMISSION_GRANTED) {
                    galleryLauncher.launch("image/*")
                } else {
                    galleryPermissionLauncher.
                        launch(Manifest.permission.READ_MEDIA_IMAGES)
                }
            }
        )
    }
}

@Composable
private fun CustomOutlinedTextField (
    label: String,
    value: String,
    canEdit: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField (
        value = value,
        enabled = canEdit,
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
        textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSurface),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .shadow(6.dp, RoundedCornerShape(12.dp))
            .background (
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surface
            ),
        colors = OutlinedTextFieldDefaults.colors (
            cursorColor = Color.Cyan,
            focusedBorderColor = Color.Cyan,
            unfocusedBorderColor = Color.Gray
        ),
        label = {
            Text(text = label, color = MaterialTheme.colorScheme.onSurface)
        },
        onValueChange = onValueChange
    )
}

@Composable
fun SettingsContentSkeleton (loadingMessage: String) {

}