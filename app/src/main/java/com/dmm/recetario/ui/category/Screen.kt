package com.dmm.recetario.ui.category

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dmm.recetario.core.utils.helper.ResourceHelper
import com.dmm.recetario.domain.model.Recipe
import com.dmm.recetario.domain.model.User
import com.dmm.recetario.ui.components.BaseLayoutWithRefresh
import com.dmm.recetario.ui.components.WellnessCard
import com.dmm.recetario.ui.components.WellnessCardSkeleton

@Composable
fun CategoryScreen (
    user: User?,
    categoryId: String,
    resourceHelper: ResourceHelper,
    viewModel: CategoryViewModel = hiltViewModel(),
    onHomeClick: () -> Unit,
    onCompleteForm: () -> Unit,
    onLogOutSuccess: () -> Unit,
    onSettingsClick: () -> Unit,
    onRecipeClick: (Recipe) -> Unit
) {
    LaunchedEffect(categoryId) {
        viewModel.loadRecipes(categoryId)
    }

    val uiState = viewModel.uiState
    val recipes by viewModel.recipes.collectAsStateWithLifecycle()

    BaseLayoutWithRefresh (
        user = user,
        resourceHelper = resourceHelper,
        onHomeClick = onHomeClick,
        onRefresh = viewModel::refresh,
        onCompleteForm = onCompleteForm,
        onSettingsClick = onSettingsClick,
        onLogOutSuccess = onLogOutSuccess
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
                    resourceHelper = resourceHelper,
                    onRecipeClick = onRecipeClick
                )
            }
        }
    }
}

@Composable
private fun CategoryContent (
    recipes: List<Recipe>,
    resourceHelper: ResourceHelper,
    onRecipeClick: (Recipe) -> Unit
) {
    LazyVerticalGrid (
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(span = { GridItemSpan(2) }) {
            Text (
                text = if (recipes.isNotEmpty()) "Recetas disponibles"
                    else "No hay recetas",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Bold
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
private fun CategoryContentSkeleton(loadingMessage: String) {
    LazyVerticalGrid (
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(span = { GridItemSpan(2) }) {
            Text (
                text = loadingMessage,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Bold
            )
        }

        items(10) {
            WellnessCardSkeleton()
        }
    }
}