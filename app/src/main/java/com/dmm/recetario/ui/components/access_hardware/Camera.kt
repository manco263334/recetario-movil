package com.dmm.recetario.ui.components.access_hardware

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@Composable
fun openCamera(onResult: suspend (Boolean) -> Unit): ManagedActivityResultLauncher<Uri, Boolean> {
    val scope = rememberCoroutineScope()

    return rememberLauncherForActivityResult (
        contract = ActivityResultContracts.TakePicture(),
        onResult = {
            scope.launch {
                onResult(it)
            }
        }
    )
}