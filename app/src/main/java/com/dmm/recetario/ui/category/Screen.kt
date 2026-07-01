package com.dmm.recetario.ui.category

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dmm.recetario.R
import com.dmm.recetario.core.utils.extension.columns
import com.dmm.recetario.domain.model.Recipe
import com.dmm.recetario.ui.components.WellnessCard
import com.dmm.recetario.ui.components.WellnessCardSkeleton

@Composable
fun CategoryScreen (
    categoryId: String,
    viewModel: CategoryViewModel = hiltViewModel(),
    onRecipeClick: (Recipe) -> Unit
) {
    LaunchedEffect(categoryId) {
        viewModel.loadRecipes(categoryId)
    }

    val uiState = viewModel.uiState
    val recipes by viewModel.recipes.collectAsStateWithLifecycle()

    BoxWithConstraints (
        modifier = Modifier.fillMaxSize()
    ) {
        Crossfade (
            targetState = uiState,
            label = "category_crossfade"
        ) { state ->
            if (state is CategoryUiState.Loading) {
                CategoryContentSkeleton(state.message)
            } else {
                CategoryContent (
                    recipes = recipes,
                    onRecipeClick = onRecipeClick
                )
            }
        }
    }
}

@Composable
private fun BoxWithConstraintsScope.CategoryContent (
    recipes: List<Recipe>,
    onRecipeClick: (Recipe) -> Unit
) {
    LazyVerticalGrid (
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Fixed(columns),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(span = { GridItemSpan(columns) }) {
            Text (
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                text = if (recipes.isNotEmpty())
                        stringResource(R.string.available_recipes)
                    else
                        stringResource(R.string.no_available_recipes)
            )
        }

        items(recipes) { recipe ->
            WellnessCard (
                title = recipe.name,
                image = recipe.icon,
                onClick = {
                    recipe.let(onRecipeClick)
                }
            )
        }
    }
}

@Composable
private fun BoxWithConstraintsScope.CategoryContentSkeleton(loadingMessage: String) {
    LazyVerticalGrid (
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Fixed(columns),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(span = { GridItemSpan(columns) }) {
            Text (
                text = loadingMessage,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
        }

        items(5 * columns) {
            WellnessCardSkeleton()
        }
    }
}