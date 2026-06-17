package com.dmm.recetario.core.access_hardware

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable

@Composable
fun openGallery(onResult: (Uri?) -> Unit) = rememberLauncherForActivityResult (
    contract = ActivityResultContracts.GetContent(),
    onResult = onResult
)