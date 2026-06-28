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
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dmm.recetario.R
import kotlin.collections.set

@Composable
fun RecipeForm (
    viewModel: RecipeFormViewModel = hiltViewModel(),
    onDismiss: () -> Unit,
    onCompleteForm: () -> Unit
) {
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    var stepIndex by rememberSaveable { mutableIntStateOf(0) }

    val data = viewModel.data

    var ingredientName by rememberSaveable { mutableStateOf("") }
    var ingredientQuantity by rememberSaveable { mutableStateOf("") }
    var stepDescription by rememberSaveable { mutableStateOf("") }

    val categoriesIDS = rememberSaveable { mutableListOf<String>() }

    when (stepIndex) {
        0 -> AlertDialog (
            onDismissRequest = onDismiss,
            confirmButton = {
                Button(onClick = { stepIndex = 1 }) {
                    Text(stringResource(R.string.next))
                }
            },
            dismissButton = {
                Button(onClick = onDismiss) {
                    Text(stringResource(R.string.cancel))
                }
            },
            title = { 
                Text(stringResource(R.string.cancel_recipe))
            },
            text = {
                Column {
                    OutlinedTextField (
                        value = data.name,
                        onValueChange = viewModel::updateName,
                        label = { 
                            Text(stringResource(R.string.recipe_name_label))
                        }
                    )

                    OutlinedTextField (
                        value = data.persons.toString(),
                        onValueChange = viewModel::updatePersons,
                        label = { 
                            Text(stringResource(R.string.persons_label))
                        }
                    )
                }
            }
        )

        1 -> AlertDialog (
            onDismissRequest = onDismiss,
            confirmButton = {
                Button(onClick = { stepIndex = 2 }) {
                    Text(stringResource(R.string.next))
                }
            },
            dismissButton = {
                Button(onClick = { stepIndex = 0 }) {
                    Text(stringResource(R.string.back))
                }
            },
            title = { 
                Text(stringResource(R.string.stimulated_time_label))
            },
            text = {
                Column {
                    OutlinedTextField (
                        value = data.totalTimeInMinutes.toString(),
                        onValueChange = viewModel::updateTotalTimeInMinutes,
                        label = { 
                            Text(stringResource(R.string.total_time_label))
                        }
                    )

                    OutlinedTextField (
                        value = data.cookingTimeInMinutes.toString(),
                        onValueChange = viewModel::updateCookingTimeInMinutes,
                        label = { 
                            Text(stringResource(R.string.cooking_time_label))
                        }
                    )

                    OutlinedTextField (
                        value = data.preparationTimeInMinutes.toString(),
                        onValueChange = viewModel::updatePreparationTimeInMinutes,
                        label = { 
                            Text(stringResource(R.string.preparation_time_label)) 
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
                        viewModel.addIngredient (
                            mapOf (
                                "name" to ingredientName, 
                                "quantity" to ingredientQuantity
                            )
                        )
                        ingredientName = ""
                        ingredientQuantity = ""
                    }
                ) {
                    Text(stringResource(R.string.add_ingredient_label))
                }
            },
            dismissButton = {
                Button(onClick = { stepIndex = 3 }) {
                    Text(stringResource(R.string.go_to_steps))
                }
            },
            title = { 
                Text(stringResource(R.string.add_ingredients))
            },
            text = {
                Column {
                    OutlinedTextField (
                        value = ingredientName, 
                        onValueChange = {
                            ingredientName = it
                        },
                        label = { 
                            Text(stringResource(R.string.ingredient_name_label))
                        }
                    )
                    
                    OutlinedTextField (
                        value = ingredientQuantity, 
                        onValueChange = {
                            ingredientQuantity = it
                        },
                        label = { 
                            Text(stringResource(R.string.quantity_label))
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
                        viewModel.addStep(stepDescription)
                        stepDescription = ""
                    }
                ) {
                    Text(stringResource(R.string.add_step_label))
                }
            },
            dismissButton = {
                Button (
                    onClick = {
                        viewModel.addRecipeData()
                        stepIndex = 4
                    }
                ) {
                    Text(stringResource(R.string.add_categories_label))
                }
            },
            title = { 
                Text(stringResource(R.string.add_steps))
            },
            text = {
                Column {
                    OutlinedTextField (
                        value = stepDescription, 
                        onValueChange = {
                            stepDescription = it
                        },
                        label = { 
                            Text(stringResource(R.string.step_label))
                        }
                    )
                }
            }
        )

        4 -> {
            val checkboxStates = rememberSaveable {
                mutableStateMapOf<String, Boolean>().withDefault { false }
            }
            AlertDialog (
                onDismissRequest = onDismiss,
                confirmButton = {
                    Button (
                        onClick = {
                            viewModel.createRecipe(categoriesIDS)
                            onCompleteForm()
                        }
                    ) {
                        Text(stringResource(R.string.save_recipe))
                    }
                },
                dismissButton = {
                    Button(onClick = onDismiss) {
                        Text(stringResource(R.string.cancel))
                    }
                },
                title = {
                    Text(stringResource(R.string.add_categories))
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