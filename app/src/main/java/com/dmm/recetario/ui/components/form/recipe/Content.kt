package com.dmm.recetario.ui.components.form.recipe

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlin.collections.set

@Composable
fun RecipeForm (
    onDismiss: () -> Unit,
    onCompleteForm: () -> Unit,
    viewModel: RecipeFormViewModel = hiltViewModel()
) {
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    var stepIndex by rememberSaveable { mutableIntStateOf(0) }

    var name by rememberSaveable { mutableStateOf("") }
    var persons by rememberSaveable { mutableIntStateOf(1) }
    var totalTimeInMinutes by rememberSaveable { mutableIntStateOf(0) }
    var cookTimeInMinutes by rememberSaveable { mutableIntStateOf(0) }
    var prepareTimeInMinutes by rememberSaveable { mutableIntStateOf(0) }
    val ingredients = rememberSaveable { mutableListOf<Map<String, String>>() }
    val steps = rememberSaveable { mutableListOf<String>() }

    var ingredientName by rememberSaveable { mutableStateOf("") }
    var ingredientQuantity by rememberSaveable { mutableStateOf("") }
    var stepDescription by rememberSaveable { mutableStateOf("") }

    val categoriesIDS = rememberSaveable { mutableListOf<String>() }

    when (stepIndex) {
        0 -> AlertDialog (
            onDismissRequest = onDismiss,
            confirmButton = {
                Button(onClick = { stepIndex = 1 }) {
                    Text("Siguiente")
                }
            },
            dismissButton = {
                Button(onClick = onDismiss) {
                    Text("Cancelar")
                }
            },
            title = { 
                Text("Cancelar receta") 
            },
            text = {
                Column {
                    OutlinedTextField (
                        value = name, 
                        onValueChange = { name = it }, 
                        label = { 
                            Text("Nombre de la Receta") 
                        }
                    )
                    
                    OutlinedTextField (
                        value = persons.toString(), 
                        onValueChange = { persons = it.toIntOrNull() ?: 0 }, 
                        label = { 
                            Text("Personas") 
                        }
                    )
                }
            }
        )

        1 -> AlertDialog (
            onDismissRequest = onDismiss,
            confirmButton = {
                Button(onClick = { stepIndex = 2 }) {
                    Text("Siguiente")
                }
            },
            dismissButton = {
                Button(onClick = { stepIndex = 0 }) {
                    Text("Regresar")
                }
            },
            title = { 
                Text("Tiempos estimados") 
            },
            text = {
                Column {
                    OutlinedTextField (
                        value = totalTimeInMinutes.toString(), 
                        onValueChange = { totalTimeInMinutes = it.toIntOrNull() ?: 0 }, 
                        label = { 
                            Text("Tiempo total (en minutos)") 
                        }
                    )
                    
                    OutlinedTextField (
                        value = cookTimeInMinutes.toString(), 
                        onValueChange = { cookTimeInMinutes = it.toIntOrNull() ?: 0 }, 
                        label = { 
                            Text("Tiempo de cocción (en minutos)") 
                        }
                    )
                    
                    OutlinedTextField (
                        value = prepareTimeInMinutes.toString(), 
                        onValueChange = { prepareTimeInMinutes = it.toIntOrNull() ?: 0 }, 
                        label = { 
                            Text("Tiempo de preparación (en minutos)") 
                        }
                    )
                }
            }
        )

        2 -> AlertDialog (
            onDismissRequest = onDismiss,
            confirmButton = {
                Button (
                    onClick = {
                        ingredients.add (
                            mapOf (
                                "name" to ingredientName, 
                                "quantity" to ingredientQuantity
                            )
                        )
                        ingredientName = ""
                        ingredientQuantity = ""
                    }
                ) {
                    Text("Agregar otro ingrediente")
                }
            },
            dismissButton = {
                Button(onClick = { stepIndex = 3 }) {
                    Text("Pasar a pasos")
                }
            },
            title = { 
                Text("Agregar Ingredientes") 
            },
            text = {
                Column {
                    OutlinedTextField (
                        value = ingredientName, 
                        onValueChange = { ingredientName = it }, 
                        label = { 
                            Text("Nombre del Ingrediente") 
                        }
                    )
                    
                    OutlinedTextField (
                        value = ingredientQuantity, 
                        onValueChange = { ingredientQuantity = it }, 
                        label = { 
                            Text("Cantidad") 
                        }
                    )
                }
            }
        )

        3 -> AlertDialog (
            onDismissRequest = onDismiss,
            confirmButton = {
                Button (
                    onClick = {
                        steps.add(stepDescription)
                        stepDescription = ""
                    }
                ) {
                    Text("Agregar otro paso")
                }
            },
            dismissButton = {
                Button (
                    onClick = {
                        viewModel.addRecipeData (
                            name = name,
                            persons = persons,
                            ingredients = ingredients,
                            steps = steps,
                            totalTimeInMinutes = totalTimeInMinutes,
                            cookingTimeInMinutes = cookTimeInMinutes,
                            preparationTimeInMinutes = prepareTimeInMinutes
                        )
                        stepIndex = 4
                    }
                ) {
                    Text("Agregar las categorías a las que pertenece")
                }
            },
            title = { 
                Text("Agregar Pasos") 
            },
            text = {
                Column {
                    OutlinedTextField (
                        value = stepDescription, 
                        onValueChange = { stepDescription = it }, 
                        label = { 
                            Text("Paso") 
                        }
                    )
                }
            }
        )

        4 -> {
            val checkboxStates = rememberSaveable { mutableStateMapOf<String, Boolean>().withDefault { false } }
            AlertDialog (
                onDismissRequest = onDismiss,
                confirmButton = {
                    Button (
                        onClick = {
                            viewModel.createRecipe(categoriesIDS)
                            onCompleteForm()
                        }
                    ) {
                        Text("Guardar receta")
                    }
                },
                dismissButton = {
                    Button(onClick = onDismiss) {
                        Text("Cancelar")
                    }
                },
                title = {
                    Text("Agregar sus categorías")
                },
                text = {
                    Column {
                        categories.forEach { category ->
                            Row {
                                Checkbox (
                                    checked = checkboxStates.getValue(category.id),
                                    onCheckedChange = {
                                        checkboxStates[category.id] = it
                                        if (it) {
                                            categoriesIDS.add(category.id)
                                        } else {
                                            categoriesIDS.remove(category.id)
                                        }
                                    }
                                )
                                Text(text = category.name)
                            }
                        }
                    }
                }
            )
        }
    }
}