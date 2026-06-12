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
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dmm.recetario.domain.model.Category
import com.dmm.recetario.domain.model.User
import com.dmm.recetario.ui.components.BaseLayoutWithRefresh
import com.dmm.recetario.ui.components.WellnessCard
import com.dmm.recetario.ui.components.WellnessCardSkeleton
import kotlinx.coroutines.launch

@Composable
fun HomeScreen (
    user: User?,
    viewModel: HomeViewModel = hiltViewModel(),
    onCompleteForm: () -> Unit,
    onSettingsClick: () -> Unit,
    onLogOutSuccess: () -> Unit,
    onCategoryClick: (Category) -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val uiState = viewModel.uiState
    val categories by viewModel.categories.collectAsStateWithLifecycle()

    BaseLayoutWithRefresh (
        user = user,
        drawerState = drawerState,
        onRefresh = viewModel::refresh,
        onCompleteForm = onCompleteForm,
        onSettingsClick = onSettingsClick,
        onLogOutSuccess = onLogOutSuccess,
        onHomeClick = {
            scope.launch {
                drawerState.close()
            }
        }
    ) {
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
}

@Composable
private fun HomeContent (
    categories: List<Category>,
    onCategoryClick: (Category) -> Unit,
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
                text = if (categories.isNotEmpty()) "Categorías disponibles"
                    else "No hay categorías",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Bold
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