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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dmm.recetario.core.utils.helper.ResourceHelper

@Composable
fun CategoryForm (
    resourceHelper: ResourceHelper,
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
                Text("Crear")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        title = {
            Text("Agregar categoría")
        },
        text = {
            Column {
                OutlinedTextField (
                    value = name,
                    onValueChange = {
                        name = it
                    },
                    label = {
                        Text("Nombre")
                    }
                )
                OutlinedTextField (
                    value = icon,
                    onValueChange = {
                        icon = it
                    },
                    label = {
                        Text("Icono (URL)",)
                    }
                )
            }
        }
    )
}