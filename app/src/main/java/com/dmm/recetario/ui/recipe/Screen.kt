package com.dmm.recetario.ui.recipe

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import com.dmm.recetario.ui.components.shimmerLoading

@Composable
fun RecipeScreen (
    recipeId: String,
    viewModel: RecipeViewModel = hiltViewModel(),
) {
    LaunchedEffect(recipeId) {
        viewModel.loadRecipe(recipeId)
    }

    val uiState = viewModel.uiState
    val recipe by viewModel.recipe.collectAsStateWithLifecycle()

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
private fun RecipeContentSkeleton(loadingMessage: String, isLoading: Boolean = true) {
    val scrollState = rememberScrollState()

    Column (
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Text (
            text = loadingMessage,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        repeat(3) {
            Box (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .shimmerLoading(isLoading)
            ) {
                Column (
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text (
                        text = "",
                        modifier = Modifier
                            .fillMaxWidth()
                            .shimmerLoading(isLoading),
                        style = MaterialTheme.typography.titleLarge
                    )

                    repeat(3) {
                        Text (
                            text = "",
                            modifier = Modifier
                                .fillMaxWidth()
                                .shimmerLoading(isLoading),
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                }
            }
        }
    }
}