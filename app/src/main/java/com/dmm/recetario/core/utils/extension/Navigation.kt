package com.dmm.recetario.core.utils.extension

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

fun <T : NavKey> NavBackStack<T>.navigateTo(screen: T): Boolean {
    return this.add(screen)
}

fun <T : NavKey> NavBackStack<T>.back(): T? {
    if (this.isEmpty()) return null

    return this.removeLastOrNull()
}

fun <T : NavKey> NavBackStack<T>.backTo(targetScreen: T) {
    if (this.isEmpty()) return

    if (targetScreen !in this) return

    while (this.isNotEmpty() && this.last() != targetScreen) {
        this.removeLastOrNull()
    }
}

fun <T : NavKey, K : T> NavBackStack<T>.dropScreensByKey(targetKey: K): NavBackStack<T> {
    if (targetKey !in this) return this

    val screens = NavBackStack<T>()

    while (this.isNotEmpty()) {
        val screen = this.removeLastOrNull() ?: break

        if (screen != targetKey) {
            screens.add(screen)
        }
    }

    return screens.also { it.reverse() }
}

fun <T : NavKey, K : T> NavBackStack<T>.dropScreensByKeys(vararg targetKeys: K): NavBackStack<T> {
    if (!targetKeys.any { it in this }) return this

    val screens = NavBackStack<T>()

    while (this.isNotEmpty()) {
        val screen = this.removeLastOrNull() ?: break

        if (screen !in targetKeys) {
            screens.add(screen)
        }
    }

    return screens.also { it.reverse() }
}