package com.dmm.recetario.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
import com.dmm.recetario.ui.home.HomeScreen
import com.dmm.recetario.ui.recipe.RecipeScreen
import com.dmm.recetario.ui.settings.SettingsScreen

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun AppNavigation (
    user: User?,
    backStack: NavBackStack<NavKey>,
    modifier: Modifier = Modifier
) {
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

            entry<Routes.Home> (
                metadata = ListDetailSceneStrategy.listPane()
            ) {
                HomeScreen (
                    user = user,
                    onCategoryClick = {
                        backStack.navigateTo(Routes.Category(it.id))
                    },
                    onSettingsClick = {
                        backStack.navigateTo(Routes.Settings)
                    },
                    onLogOutSuccess = {
                        backStack.clear()
                        backStack.navigateTo(Routes.Login)
                    },
                    onCompleteForm = {
                        backStack.backTo(Routes.Home)
                    }
                )
            }

            entry<Routes.Category> (
                metadata = ListDetailSceneStrategy.detailPane()
            ) {
                CategoryScreen (
                    user = user,
                    categoryId = it.id,
                    onRecipeClick = {
                        backStack.navigateTo(Routes.Recipe(it.id))
                    },
                    onSettingsClick = {
                        backStack.navigateTo(Routes.Settings)
                    },
                    onLogOutSuccess = {
                        backStack.clear()
                        backStack.navigateTo(Routes.Login)
                    },
                    onHomeClick = {
                        backStack.backTo(Routes.Home)
                    },
                    onCompleteForm = {
                        backStack.backTo(Routes.Category(it.id))
                    },
                )
            }

            entry<Routes.Recipe> (
                metadata = ListDetailSceneStrategy.extraPane()
            ) {
                RecipeScreen (
                    user = user,
                    recipeId = it.id,
                    onSettingsClick = {
                        backStack.navigateTo(Routes.Settings)
                    },
                    onLogOutSuccess = {
                        backStack.clear()
                        backStack.navigateTo(Routes.Login)
                    },
                    onHomeClick = {
                        backStack.backTo(Routes.Home)
                    },
                    onCompleteForm = {
                        backStack.backTo(Routes.Recipe(it.id))
                    }
                )
            }

            entry <Routes.Settings> {
                SettingsScreen (
                    onHomeClick = {
                        backStack.backTo(Routes.Home)
                    },
                    onLogOutSuccess = {
                        backStack.clear()
                        backStack.navigateTo(Routes.Login)
                    }
                )
            }
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