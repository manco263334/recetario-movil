package com.dmm.recetario.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.dmm.recetario.R

@Composable
fun ShowImagePickerDialog (
    onDismiss: () -> Unit,
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit
) {
    AlertDialog (
        onDismissRequest = onDismiss,
        title = {
            Text(stringResource(R.string.select_image))
        },
        text = {
            Text(stringResource(R.string.select_image_description))
       },
        confirmButton = {
            Column {
                TextButton (
                    onClick = {
                        onGalleryClick()
                        onDismiss()
                    }
                ) {
                    Text(stringResource(R.string.select_image_from_gallery))
                }
                TextButton (
                    onClick = {
                        onCameraClick()
                        onDismiss()
                    }
                ) {
                    Text(stringResource(R.string.take_photo))
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}