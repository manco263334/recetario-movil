package com.dmm.recetario.ui.recipe

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dmm.recetario.R
import com.dmm.recetario.domain.model.Recipe
import com.dmm.recetario.domain.model.User
import com.dmm.recetario.ui.components.BaseLayoutWithRefresh

@Composable
fun RecipeScreen (
    user: User?,
    recipeId: String,
    viewModel: RecipeViewModel = hiltViewModel(),
    onHomeClick: () -> Unit,
    onCompleteForm: () -> Unit,
    onSettingsClick: () -> Unit,
    onLogOutSuccess: () -> Unit
) {
    LaunchedEffect(recipeId) {
        viewModel.loadRecipe(recipeId)
    }

    val uiState = viewModel.uiState
    val recipe by viewModel.recipe.collectAsStateWithLifecycle()

    BaseLayoutWithRefresh (
        user = user,
        onHomeClick = onHomeClick,
        onRefresh = viewModel::refresh,
        onCompleteForm = onCompleteForm,
        onSettingsClick = onSettingsClick,
        onLogOutSuccess = onLogOutSuccess
    ) {
        Crossfade (
            targetState = uiState,
            label = "recipe_crossfade"
        ) { state ->
            if (state is RecipeUiState.Loading) {
                RecipeContentSkeleton(state.message)
            } else {
                RecipeContent(recipe)
            }
        }
    }
}

@Composable
private fun RecipeContent(recipe: Recipe?) {
    val scrollState = rememberScrollState()

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
            .verticalScroll(scrollState)
    ) {
        if (recipe == null) {
            Text (
                style = MaterialTheme.typography.bodyLarge,
                text = stringResource(R.string.recipe_not_found),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        } else {
            Card (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = recipe.name, style = MaterialTheme.typography.headlineMedium)

                    Spacer(modifier = Modifier.height(8.dp))
                    Text (
                        pluralStringResource (
                            R.plurals.total_time_description,
                            recipe.totalTimeInMinutes,
                            recipe.totalTimeInMinutes
                        )
                    )
                    Text (
                        pluralStringResource (
                            R.plurals.preparation_time_description,
                            recipe.preparationTimeInMinutes,
                            recipe.preparationTimeInMinutes
                        )
                    )
                    Text (
                        pluralStringResource (
                            R.plurals.cooking_time_description,
                            recipe.cookingTimeInMinutes,
                            recipe.cookingTimeInMinutes
                        )
                    )
                }
            }

            Card (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text (
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge,
                        text = stringResource(R.string.ingredients_model)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    recipe.ingredients.forEach { ingredient ->
                        Text (text = "* ${ingredient["quantity"] ?: "N/A"} de ${ingredient["name"] 
                            ?: "N/A"}")

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            Card (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text (
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge,
                        text = stringResource(R.string.steps_label)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    var index = 0

                    recipe.steps.forEach { step ->
                        Text(text = "${++index}. $step")
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            Card (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text (
                        text = stringResource (
                            R.string.rating,
                            recipe.stars
                        ),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    }
}

@Composable
private fun RecipeContentSkeleton(loadingMessage: String) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text (
            text = loadingMessage,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}