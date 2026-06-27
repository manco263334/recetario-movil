package com.dmm.recetario.ui.components.bottom_bar

import androidx.compose.material3.BottomAppBar
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey

@Composable
fun BottomBar (
    selectedKey: NavKey,
    onSelectKey: (NavKey) -> Unit
) {
    BottomAppBar {
        bottomNavItems.forEach { item ->
            BottomBarItem (
                item = item,
                isSelected = selectedKey == item.route,
                onClick = onSelectKey
            )
        }
    }
}

@Composable
private fun BottomBarItem (
    item: BottomNavItem,
    isSelected: Boolean,
    onClick: (NavKey) -> Unit
) {

}