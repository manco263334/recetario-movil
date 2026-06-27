package com.dmm.recetario.ui.components.bottom_bar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.dmm.recetario.R
import com.dmm.recetario.navigation.Routes

data class BottomNavItem (
    val name: String,
    val route: Routes,
    val icon: ImageVector,

    // En caso de que se requiera en el futuro
    val badgeCount: Int = 0
)

@Composable
fun bottomNavItems() = listOf (
    BottomNavItem (
        name = stringResource(R.string.home),
        route = Routes.Home,
        icon = Icons.Default.Home
    ),
    BottomNavItem (
        name = stringResource(R.string.categories_model),
        route = Routes.Category(""),
        icon = Icons.Default.Home
    ),
    BottomNavItem (
        name = stringResource(R.string.recipes_model),
        route = Routes.Recipe(""),
        icon = Icons.Default.Home
    ),
    BottomNavItem (
        name = stringResource(R.string.settings),
        route = Routes.Settings,
        icon = Icons.Default.Settings
    )
)