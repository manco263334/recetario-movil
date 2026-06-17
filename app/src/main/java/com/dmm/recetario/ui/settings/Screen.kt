package com.dmm.recetario.ui.settings

import android.content.Context
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.dmm.recetario.ui.components.BaseLayout
import com.dmm.recetario.ui.components.ShowImagePickerDialog
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen (
    viewModel: SettingViewModel = hiltViewModel(),
    onHomeClick: () -> Unit,
    onLogOutSuccess: () -> Unit
) {
    val uiState = viewModel.uiState
    val galleryLauncher = openGallery {  }
    val cameraLauncher = openCamera {  }
    val user by viewModel.user.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    BaseLayout (
        user = user,
        showFab = false,
        drawerState = drawerState,
        onHomeClick = onHomeClick,
        onCompleteForm = {},
        onLogOutSuccess = onLogOutSuccess,
        onSettingsClick = {
            scope.launch {
                drawerState.close()
            }
        },
    ) { paddingValues ->
        Crossfade (
            targetState = uiState,
            label = "settings_crossfade"
        ) { state ->
            if (state is SettingsUiState.Loading) {
                SettingsContentSkeleton (
                    paddingValues = paddingValues,
                    loadingMessage =  state.message
                )
            } else {
                SettingsContent (
                    user = user,
                    context = context,
                    paddingValues = paddingValues,
                    cameraLauncher = cameraLauncher,
                    galleryLauncher = galleryLauncher
                )
            }
        }
    }
}

@Composable
fun SettingsContent (
    user: User?,
    context: Context,
    paddingValues: PaddingValues,
    cameraLauncher: ManagedActivityResultLauncher<Uri, Boolean>,
    galleryLauncher: ManagedActivityResultLauncher<String, Uri?>
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

    Box (
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(top = 20.dp)
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.TopCenter
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box (
                modifier = Modifier
                    .size(140.dp)
                    .background(Color.Gray, CircleShape)
                    .border(3.dp, Color.Cyan, CircleShape)
                    .clickable { showDialog = true },
                contentAlignment = Alignment.Center
            ) {
                if (profilePictureUri != null) {
                    AsyncImage (
                        model = profilePictureUri,
                        contentDescription = stringResource (
                            R.string.profile_picture_description
                        ),
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
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
            ) { name = it }
            CustomOutlinedTextField (
                value = email,
                canEdit = canEditInfo,
                label = getString(R.string.email),
                keyboardType = KeyboardType.Email
            ) { email = it }
            CustomOutlinedTextField (
                value = username,
                canEdit = canEditInfo,
                label = getString(R.string.username)
            ) { username = it }
            CustomOutlinedTextField (
                value = phone,
                canEdit = canEditInfo,
                keyboardType = KeyboardType.Phone,
                label = getString(R.string.number_phone)
            ) { phone = it }

            Spacer(modifier = Modifier.height(20.dp))

            Button (
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
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Cyan),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(12.dp))
                    .padding(horizontal = 20.dp)
            ) {
                if (isUploading) {
                    CircularProgressIndicator (
                        color = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text (
                        text = stringResource(R.string.save_changes),
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                }
            }
        }
    }

    if (showDialog) {
        ShowImagePickerDialog (
            onDismiss = { showDialog = false },
            onGalleryClick = { galleryLauncher.launch("image/*") },
            onCameraClick = { cameraLauncher.launch(uri) }
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
        textStyle = LocalTextStyle.current.copy(color = Color.White),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .shadow(6.dp, RoundedCornerShape(12.dp))
            .background(Color(0xFF1E1E1E), RoundedCornerShape(12.dp)),
        colors = OutlinedTextFieldDefaults.colors (
            focusedBorderColor = Color.Cyan,
            unfocusedBorderColor = Color.Gray,
            cursorColor = Color.Cyan
        ),
        label = {
            Text(label, color = Color.White)
        },
        onValueChange = onValueChange
    )
}

@Composable
fun SettingsContentSkeleton (
    loadingMessage: String,
    paddingValues: PaddingValues
) {

}