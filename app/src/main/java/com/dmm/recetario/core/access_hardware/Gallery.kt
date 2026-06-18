package com.dmm.recetario.core.access_hardware

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@Composable
fun openGallery(onResult: suspend (Uri?) -> Unit): ManagedActivityResultLauncher<String, Uri?> {
    val scope = rememberCoroutineScope()

    return rememberLauncherForActivityResult (
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            scope.launch {
                onResult(it)
            }
        }
    )
}