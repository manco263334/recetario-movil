package com.dmm.recetario.ui.home

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dmm.recetario.R
import com.dmm.recetario.domain.model.Category
import com.dmm.recetario.ui.components.WellnessCard
import com.dmm.recetario.ui.components.WellnessCardSkeleton

@Composable
fun HomeScreen (
    viewModel: HomeViewModel = hiltViewModel(),
    onCategoryClick: (Category) -> Unit
) {
    val uiState = viewModel.uiState
    val categories by viewModel.categories.collectAsStateWithLifecycle()

    Crossfade (
        targetState = uiState,
        label = "home_crossfade"
    ) { state ->
        if (state is HomeUiState.Loading) {
            HomeContentSkeleton(state.message)
        } else {
            HomeContent (
                categories = categories,
                onCategoryClick = onCategoryClick
            )
        }
    }
}

@Composable
private fun HomeContent (
    categories: List<Category>,
    onCategoryClick: (Category) -> Unit
) {
    LazyVerticalGrid (
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(span = { GridItemSpan(2) }) {
            Text (
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                text = if (categories.isNotEmpty())
                        stringResource(R.string.available_categories)
                    else
                        stringResource(R.string.no_available_categories)
            )
        }

        items(categories) { category ->
            WellnessCard (
                title = category.name,
                image = category.icon,
                onClick = {
                    onCategoryClick(category)
                }
            )
        }
    }
}

@Composable
private fun HomeContentSkeleton(loadingMessage: String) {
    LazyVerticalGrid (
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(span = { GridItemSpan(2) }) {
            Text (
                text = loadingMessage,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        items(10) {
            WellnessCardSkeleton()
        }
    }
}