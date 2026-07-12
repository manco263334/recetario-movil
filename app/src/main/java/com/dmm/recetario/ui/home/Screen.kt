package com.dmm.recetario.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
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
import com.dmm.recetario.core.utils.extension.columns
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

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val message = when (uiState) {
            is HomeUiState.Loading -> uiState.message
            else -> if (categories.isNotEmpty())
                stringResource(R.string.available_categories)
            else
                stringResource(R.string.no_available_categories)
        }
        val columns = this.columns

        LazyVerticalGrid (
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Fixed(columns),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                this@LazyVerticalGrid.HomeContent (
                    columns = columns,
                    message = message,
                    categories = categories,
                    onCategoryClick = onCategoryClick
                )

                if (uiState is HomeUiState.Loading) {
                    this@LazyVerticalGrid.HomeContentSkeleton(columns = columns)
                }
            }
        }
    }
}

@Composable
private fun LazyGridScope.HomeContent (
    columns: Int,
    message: String,
    categories: List<Category>,
    onCategoryClick: (Category) -> Unit
) {
    item(span = { GridItemSpan(columns) }) {
        Text (
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            text = message
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

@Composable
private fun LazyGridScope.HomeContentSkeleton(columns: Int) {
    items(5 * columns) {
        WellnessCardSkeleton(showDescription = false)
    }
}