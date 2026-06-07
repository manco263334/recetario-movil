package com.dmm.recetario.ui.category

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
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dmm.recetario.core.utils.extension.isNeitherNullNorAnonymous
import com.dmm.recetario.domain.model.Recipe
import com.dmm.recetario.domain.model.User
import com.dmm.recetario.ui.components.Toolbar
import com.dmm.recetario.ui.components.WelcomeHeader
import com.dmm.recetario.ui.components.WellnessCard
import com.dmm.recetario.ui.components.drawer.DrawerContent
import com.dmm.recetario.ui.components.fab.FAB
import com.dmm.recetario.ui.components.refresher.PullToRefresh

@Composable
fun CategoryScreen (
    categoryId: String,
    user: User?,
    snackbarHostState: SnackbarHostState,
    onRecipeClick: (Recipe) -> Unit,
    onSettingsClick: (user: User?) -> Unit,
    onLogOutSuccess: () -> Unit,
    onHomeClick: () -> Unit,
    onCompleteForm: () -> Unit,
    viewModel: CategoryViewModel = hiltViewModel(),
) {
    LaunchedEffect(categoryId) {
        viewModel.loadRecipes(categoryId)
    }

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val recipes by viewModel.recipes.collectAsStateWithLifecycle()

    ModalNavigationDrawer (
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            DrawerContent (
                drawerState = drawerState,
                user = user,
                onSettingsClick = onSettingsClick,
                onLogOutSuccess = onLogOutSuccess,
                onHomeClick = onHomeClick
            )
        }
    ) {
        Scaffold (
            topBar = {
                Toolbar (
                    drawerState = drawerState
                ) {
                    WelcomeHeader(user)
                }
            },
            floatingActionButton = {
                if (user.isNeitherNullNorAnonymous()) {
                    FAB(onCompleteForm = onCompleteForm)
                }
            },
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            }
        ) { paddingValues ->
            CategoryContent (
                paddingValues = paddingValues,
                recipes = recipes,
                onRecipeClick = onRecipeClick,
                onRefresh = viewModel::refresh,
                snackbarHostState = snackbarHostState
            )
        }
    }
}

@Composable
private fun CategoryContent (
    paddingValues: PaddingValues,
    recipes: List<Recipe>,
    snackbarHostState: SnackbarHostState,
    onRecipeClick: (Recipe) -> Unit,
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
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item(span = { GridItemSpan(2) }) {
                Text (
                    text = if (recipes.isNotEmpty()) "Recetas disponibles" else "No hay recetas",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    fontWeight = FontWeight.Bold
                )
            }

            items(recipes) { recipe ->
                WellnessCard (
                    title = recipe.name,
                    onClick = {
                        recipe.let(onRecipeClick)
                    }
                )
            }
        }
    }
}