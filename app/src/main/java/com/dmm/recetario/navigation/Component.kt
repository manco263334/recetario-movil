package com.dmm.recetario.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.dmm.recetario.core.utils.extension.back
import com.dmm.recetario.core.utils.extension.backTo
import com.dmm.recetario.core.utils.extension.navigateTo
import com.dmm.recetario.domain.model.User
import com.dmm.recetario.ui.auth.login.LoginScreen
import com.dmm.recetario.ui.auth.register.RegisterScreen
import com.dmm.recetario.ui.category.CategoryScreen
import com.dmm.recetario.ui.category.CategoryViewModel
import com.dmm.recetario.ui.components.TopBar
import com.dmm.recetario.ui.components.WelcomeHeader
import com.dmm.recetario.ui.components.bottom_bar.BottomBar
import com.dmm.recetario.ui.components.drawer.DrawerContent
import com.dmm.recetario.ui.components.fab.FAB
import com.dmm.recetario.ui.components.refresher.PullToRefresh
import com.dmm.recetario.ui.home.HomeScreen
import com.dmm.recetario.ui.home.HomeViewModel
import com.dmm.recetario.ui.recipe.RecipeScreen
import com.dmm.recetario.ui.recipe.RecipeViewModel
import com.dmm.recetario.ui.settings.SettingsScreen
import kotlinx.coroutines.launch

@Composable
fun AppNavigation (
    user: User?,
    backStack: NavBackStack<NavKey>,
    modifier: Modifier = Modifier
) {
    val viewModels = getViewModels()
    val currentKey = backStack.lastOrNull() ?: Routes.Login

    val layoutConfig = remember(currentKey) {
        when (currentKey) {
            is Routes.Login, is Routes.Register -> {
                LayoutConfig()
            }

            is Routes.Home -> {
                val homeViewModel = viewModels["home"] as HomeViewModel

                LayoutConfig (
                    hasBottomBar = true,
                    onRefresh = homeViewModel::refresh,
                    onCompleteForm = {
                        backStack.backTo(Routes.Home)
                    }
                )
            }

            is Routes.Category -> {
                val categoryViewModel = viewModels["category"] as CategoryViewModel

                LayoutConfig (
                    hasBottomBar = true,
                    onRefresh = categoryViewModel::refresh,
                    onCompleteForm = {
                        backStack.backTo(Routes.Category(currentKey.id))
                    }
                )
            }

            is Routes.Recipe -> {
                val recipeViewModel = viewModels["recipe"] as RecipeViewModel

                LayoutConfig (
                    hasBottomBar = true,
                    onRefresh = recipeViewModel::refresh,
                    onCompleteForm = {
                        backStack.backTo(Routes.Recipe(currentKey.id))
                    }
                )
            }

            is Routes.Settings -> {
                LayoutConfig(hasFab = false, hasRefresh = false, hasBottomBar = true)
            }

            else -> LayoutConfig()
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    val closeDrawerAndDo: (call: () -> Unit) -> () -> Unit = {
        coroutineScope.launch {
            if (drawerState.isOpen) {
                drawerState.close()
            }
        }

        it
    }

    val onHomeClick: () -> Unit = closeDrawerAndDo {
        backStack.navigateTo(Routes.Home)
    }
    val onSettingsClick: () -> Unit = closeDrawerAndDo {
        backStack.navigateTo(Routes.Settings)
    }
    val onLogOutSuccess: () -> Unit = closeDrawerAndDo {
        backStack.clear()
        backStack.navigateTo(Routes.Login)
    }
    val onCategoryClick: (String) -> Unit = {
        backStack.navigateTo(Routes.Category(it))
    }
    val onRecipeClick: (String) -> Unit = {
        backStack.navigateTo(Routes.Recipe(it))
    }

    if (layoutConfig.showBaseLayout) {
        val onSelectKey: (NavKey) -> Unit = {
            when(it) {
                is Routes.Home -> onHomeClick()
                is Routes.Settings -> onSettingsClick()
                is Routes.Category -> onCategoryClick(it.id)
                is Routes.Recipe -> onRecipeClick(it.id)

                else -> {}
            }
        }

        ModalNavigationDrawer (
            drawerState = drawerState,
            drawerContent = {
                DrawerContent (
                    user = user,
                    drawerState = drawerState,
                    snackbarHostState = snackbarHostState,
                    onHomeClick = onHomeClick,
                    onSettingsClick = onSettingsClick,
                    onLogOutSuccess = onLogOutSuccess
                )
            }
        ) {
            Scaffold (
                topBar = {
                    TopBar(drawerState = drawerState) {
                        WelcomeHeader(user = user)
                    }
                },
                bottomBar = {
                    if (layoutConfig.hasBottomBar) {
                        BottomBar (
                            selectedKey = currentKey,
                            onSelectKey = onSelectKey
                        )
                    }
                },
                floatingActionButton = {
                    if (layoutConfig.hasFab && layoutConfig.onCompleteForm != null) {
                        FAB(onCompleteForm = layoutConfig.onCompleteForm)
                    }
                },
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
            ) { paddingValues ->
                if (layoutConfig.hasRefresh && layoutConfig.onRefresh != null) {
                    PullToRefresh (
                        onRefresh = layoutConfig.onRefresh,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        snackbarHostState = snackbarHostState
                    ) {
                        AppNavDisplay (
                            modifier = modifier,
                            backStack = backStack,
                            viewModels = viewModels
                        )
                    }
                } else {
                    Box(modifier = Modifier.padding(paddingValues)) {
                        AppNavDisplay (
                            modifier = modifier,
                            backStack = backStack,
                            viewModels = viewModels
                        )
                    }
                }
            }
        }
    } else {
        AppNavDisplay (
            modifier = modifier,
            backStack = backStack,
            viewModels = viewModels
        )
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun AppNavDisplay (
    modifier: Modifier,
    backStack: NavBackStack<NavKey>,
    viewModels: Map<String, ViewModel>
) {
    val homeViewModel = viewModels["home"] as HomeViewModel
    val categoryViewModel = viewModels["category"] as CategoryViewModel
    val recipeViewModel = viewModels["recipe"] as RecipeViewModel

    NavDisplay (
        modifier = modifier,
        backStack = backStack,
        onBack = backStack::back,
        entryDecorators = listOf (
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        sceneStrategies = listOf (
            rememberListDetailSceneStrategy()
        ),
        entryProvider = entryProvider {
            entry<Routes.Login> {
                LoginScreen (
                    onNavigateToHome = {
                        backStack.clear()
                        backStack.navigateTo(Routes.Home)
                    },
                    onNavigateToRegister = {
                        backStack.navigateTo(Routes.Register)
                    }
                )
            }

            entry<Routes.Register> {
                RegisterScreen (
                    onNavigateToHome = {
                        backStack.clear()
                        backStack.navigateTo(Routes.Home)
                    },
                    onNavigateToLogin = {
                        backStack.backTo(Routes.Login)
                    }
                )
            }

            entry<Routes.Home>(metadata = ListDetailSceneStrategy.listPane()) {
                HomeScreen (
                    viewModel = homeViewModel,
                    onCategoryClick = {
                        backStack.navigateTo(Routes.Category(it.id))
                    }
                )
            }

            entry<Routes.Category>(metadata = ListDetailSceneStrategy.detailPane()) { category ->
                CategoryScreen (
                    categoryId = category.id,
                    viewModel = categoryViewModel,
                    onRecipeClick = { recipe ->
                        backStack.navigateTo(Routes.Recipe(recipe.id))
                    }
                )
            }

            entry<Routes.Recipe>(metadata = ListDetailSceneStrategy.extraPane()) {
                RecipeScreen(recipeId = it.id, viewModel = recipeViewModel)
            }

            entry<Routes.Settings> { SettingsScreen() }
        },
        transitionSpec = {
            slideInHorizontally (
                initialOffsetX = { it },
                animationSpec = tween(250)
            ) togetherWith slideOutHorizontally (
                targetOffsetX = { -it },
                animationSpec = tween(250)
            )
        },
        popTransitionSpec = {
            slideInHorizontally (
                initialOffsetX = { -it },
                animationSpec = tween(250)
            ) togetherWith slideOutHorizontally (
                targetOffsetX = { it },
                animationSpec = tween(250)
            )
        },
        predictivePopTransitionSpec = {
            slideInHorizontally (
                initialOffsetX = { -it },
                animationSpec = tween(250)
            ) togetherWith slideOutHorizontally (
                targetOffsetX = { it },
                animationSpec = tween(250)
            )
        }
    )
}