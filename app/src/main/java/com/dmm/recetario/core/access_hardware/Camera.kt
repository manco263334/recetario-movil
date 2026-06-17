package com.dmm.recetario.core.access_hardware

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable

@Composable
fun openCamera(onResult: (Boolean) -> Unit) = rememberLauncherForActivityResult (
    contract = ActivityResultContracts.TakePicture(),
    onResult = onResult
)