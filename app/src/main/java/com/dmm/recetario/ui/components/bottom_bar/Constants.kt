package com.dmm.recetario.ui.components.bottom_bar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.dmm.recetario.navigation.Routes

data class BottomNavItem (
    val name: String,
    val route: Routes,
    val icon: ImageVector,

    // En caso de que se requiera en el futuro
    val badgeCount: Int = 0
)

val bottomNavItems = listOf (
    BottomNavItem (
        name = "Home",
        route = Routes.Home,
        icon = Icons.Default.Home
    ),
    BottomNavItem (
        name = "Categories",
        route = Routes.Category(""),
        icon = Icons.Default.Home
    ),
    BottomNavItem (
        name = "Recipes",
        route = Routes.Recipe(""),
        icon = Icons.Default.Home
    ),
    BottomNavItem (
        name = "Settings",
        route = Routes.Settings,
        icon = Icons.Default.Home
    )
)