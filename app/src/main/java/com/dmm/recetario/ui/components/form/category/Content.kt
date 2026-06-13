package com.dmm.recetario.ui.components.form.category

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dmm.recetario.R

@Composable
fun CategoryForm (
    viewModel: CategoryFormViewModel = hiltViewModel(),
    onDismiss: () -> Unit,
    onCompleteForm: () -> Unit
) {
    var name by rememberSaveable { mutableStateOf("") }
    var icon by rememberSaveable { mutableStateOf("") }

    AlertDialog (
        onDismissRequest = onDismiss,
        confirmButton = {
            Button (
                onClick = {
                    viewModel.createCategory(name = name, icon = icon.ifBlank { null })
                    onCompleteForm()
                }
            ) {
                Text(stringResource(R.string.create))
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        },
        title = {
            Text(stringResource(R.string.add_category))
        },
        text = {
            Column {
                OutlinedTextField (
                    value = name,
                    onValueChange = {
                        name = it
                    },
                    label = {
                        Text(stringResource(R.string.name_label))
                    }
                )
                OutlinedTextField (
                    value = icon,
                    onValueChange = {
                        icon = it
                    },
                    label = {
                        Text(stringResource(R.string.icon_label))
                    }
                )
            }
        }
    )
}