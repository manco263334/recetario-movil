package com.dmm.recetario.ui.components.bottom_bar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey

@Composable
fun BottomBar (
    selectedKey: NavKey,
    onSelectKey: (NavKey) -> Unit
) {
    BottomAppBar {
        val items = bottomNavItems()

        items.forEach { item ->
            BottomBarItem (
                item = item,
                isSelected = selectedKey::class == item.route::class,
                onClick = onSelectKey
            )
        }
    }
}

@Composable
private fun RowScope.BottomBarItem (
    item: BottomNavItem,
    isSelected: Boolean,
    onClick: (NavKey) -> Unit
) {
    NavigationBarItem (
        selected = isSelected,
        icon = {
            Icon(imageVector = item.icon, contentDescription = item.name)
        },
        label = {
            Text(item.name)
        },
        onClick = {
            onClick(item.route)
        }
    )
}