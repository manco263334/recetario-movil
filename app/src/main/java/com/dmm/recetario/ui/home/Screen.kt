package com.dmm.recetario.ui.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.SnackbarHostState
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
import com.dmm.recetario.ui.components.BaseLayout
import com.dmm.recetario.ui.components.WellnessCard
import com.dmm.recetario.ui.components.WellnessCardSkeleton
import com.dmm.recetario.ui.components.refresher.PullToRefresh
import kotlinx.coroutines.launch

@Composable
fun HomeScreen (
    user: User?,
    snackbarHostState: SnackbarHostState,
    onCategoryClick: (Category) -> Unit,
    onSettingsClick: (User?) -> Unit,
    onLogOutSuccess: () -> Unit,
    onCompleteForm: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val uiState = viewModel.uiState
    val categories by viewModel.categories.collectAsStateWithLifecycle()

    BaseLayout (
        user = user,
        drawerState = drawerState,
        onSettingsClick = onSettingsClick,
        onLogOutSuccess = onLogOutSuccess,
        onCompleteForm = onCompleteForm,
        onHomeClick = {
            scope.launch {
                drawerState.close()
            }
        }
    ) { paddingValues ->
        Crossfade (
            targetState = uiState,
            label = "home_crossfade"
        ) { state ->
            if (state is HomeUiState.Loading) {
                HomeContentSkeleton (
                    paddingValues,
                    state.message
                )
            } else {
                HomeContent (
                    paddingValues = paddingValues,
                    categories = categories,
                    onCategoryClick = onCategoryClick,
                    onRefresh = viewModel::refresh,
                    snackbarHostState = snackbarHostState
                )
            }
        }
    }
}

@Composable
private fun HomeContent (
    paddingValues: PaddingValues,
    categories: List<Category>,
    snackbarHostState: SnackbarHostState,
    onCategoryClick: (Category) -> Unit,
    onRefresh: suspend () -> Unit
) {
    PullToRefresh (
        onRefresh = onRefresh,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        snackbarHostState = snackbarHostState
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
                    onClick = { onCategoryClick(category) }
                )
            }
        }
    }
}

@Composable
private fun HomeContentSkeleton (
    paddingValues: PaddingValues,
    loadingMessage: String
) {
    LazyVerticalGrid (
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
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