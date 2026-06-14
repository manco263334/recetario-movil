package com.dmm.recetario.ui.components.fab

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dmm.recetario.R
import com.dmm.recetario.ui.components.form.category.CategoryForm
import com.dmm.recetario.ui.components.form.recipe.RecipeForm

@Composable
fun FAB (
    viewModel: FABViewModel = hiltViewModel(),
    onCompleteForm: () -> Unit
) {
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    var showMenu by rememberSaveable { mutableStateOf(false) }
    var selectedOption by rememberSaveable { mutableStateOf<String?>(null) }

    Box(contentAlignment = Alignment.BottomEnd) {
        Column (horizontalAlignment = Alignment.End) {
            if (showMenu) {
                Card (
                    modifier = Modifier.padding(8.dp),
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column {
                        Text (
                            modifier = Modifier.padding(8.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            text = stringResource(R.string.what_will_you_add)
                        )
                        DropdownMenuItem (
                            text = {
                                Text(stringResource(R.string.category_model))
                            },
                            onClick = {
                                selectedOption = "Category"
                                showMenu = false
                            }
                        )
                        if (categories.isNotEmpty()) {
                            DropdownMenuItem (
                                text = {
                                    Text(stringResource(R.string.recipe_model))
                                },
                                onClick = {
                                    selectedOption = "Recipe"
                                    showMenu = false
                                }
                            )
                        }
                    }
                }
            }

            FloatingActionButton (
                modifier = Modifier.padding(16.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                onClick = {
                    showMenu = !showMenu
                }
            ) {
                Icon (
                    Icons.Default.Add,
                    contentDescription = stringResource(R.string.add)
                )
            }
        }
    }

    when (selectedOption) {
        "Category" -> CategoryForm (
            onDismiss = {
                selectedOption = null
            },
            onCompleteForm = onCompleteForm
        )
        "Recipe" -> RecipeForm (
            onDismiss =  {
                selectedOption = null
            },
            onCompleteForm = onCompleteForm
        )
    }
}