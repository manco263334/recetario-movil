package com.dmm.recetario.ui.recipe

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
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
import com.dmm.recetario.ui.components.drawer.DrawerContent
import com.dmm.recetario.ui.components.fab.FAB
import com.dmm.recetario.ui.components.refresher.PullToRefresh

@Composable
fun RecipeScreen (
    recipeId: String,
    user: User?,
    snackbarHostState: SnackbarHostState,
    onSettingsClick: (user: User?) -> Unit,
    onLogOutSuccess: () -> Unit,
    onHomeClick: () -> Unit,
    onCompleteForm: () -> Unit,
    viewModel: RecipeViewModel = hiltViewModel(),
) {
    LaunchedEffect(recipeId) {
        viewModel.loadRecipe(recipeId)
    }

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val recipe by viewModel.recipe.collectAsStateWithLifecycle()

    ModalNavigationDrawer (
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            DrawerContent (
                drawerState = drawerState,
                user = user,
                onSettingsClick = onSettingsClick,
                onLogOutSuccess = onLogOutSuccess,
                onHomeClick =  onHomeClick
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
            RecipeContent (
                paddingValues = paddingValues,
                recipe = recipe,
                onRefresh = viewModel::refresh,
                snackbarHostState = snackbarHostState
            )
        }
    }
}

@Composable
private fun RecipeContent (
    paddingValues: PaddingValues,
    recipe: Recipe?,
    snackbarHostState: SnackbarHostState,
    onRefresh: suspend () -> Unit
) {
    PullToRefresh (
        onRefresh = onRefresh,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        snackbarHostState = snackbarHostState
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            if (recipe == null) {
                Text (
                    text = "Cargando receta...",
                    style = MaterialTheme.typography.bodyLarge
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
                        Text(text = "Tiempo total: ${recipe.totalTimeInMinutes} min")
                        Text(text = "Preparación: ${recipe.preparationTimeInMinutes} min")
                        Text(text = "Cocción: ${recipe.cookingTimeInMinutes} min")
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
                            text = "Ingredientes",
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        recipe.ingredients.forEach { ingredient ->
                            Text (text = "* ${ingredient["quantity"] ?: "N/A"} de ${ingredient["name"] ?: "N/A"}")
                            
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
                            text = "Pasos",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
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
                            text = "Calificación: ${recipe.stars} ⭐",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            }
        }
    }
}