package com.dmm.recetario.core.utils.extension

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

fun <T : NavKey> NavBackStack<T>.navigateTo(screen: T): Boolean {
    if (screen in this) {
        this.backTo(screen)
        return false
    }

    return this.add(screen)
}

fun <T : NavKey> NavBackStack<T>.back(): T? {
    if (this.isEmpty()) return null

    return this.removeLastOrNull()
}

fun <T : NavKey> NavBackStack<T>.backTo(targetScreen: T): NavBackStack<T>? {
    if (this.isEmpty()) return null

    if (targetScreen !in this) return null

    val droppedScreens = NavBackStack<T>()

    while (this.isNotEmpty() && this.last() != targetScreen) {
        val screen = this.removeLastOrNull() ?: break
        droppedScreens.add(screen)
    }

    return droppedScreens.also{ it.reverse() }
}

fun <T : NavKey, K : T> NavBackStack<T>.dropScreensByKey(targetKey: K): NavBackStack<K>? {
    if (targetKey !in this) return null

    val droppedScreens = NavBackStack<K>()
    val remainingScreens = NavBackStack<T>()

    while (this.isNotEmpty()) {
        val screen = this.removeLastOrNull() ?: break

        if (screen != targetKey) {
            remainingScreens.add(screen)
        } else {
            droppedScreens.add(screen)
        }
    }

    remainingScreens.reverse()
    this.addAll(remainingScreens)

    return droppedScreens.also { it.reverse() }
}

fun <T : NavKey, K : T> NavBackStack<T>.dropScreensByKeys(vararg targetKeys: K): NavBackStack<K>? {
    if (!targetKeys.any { it in this }) return null

    val droppedScreens = NavBackStack<K>()
    val remainingScreens = NavBackStack<T>()

    while (this.isNotEmpty()) {
        val screen = this.removeLastOrNull() ?: break

        if (screen !in targetKeys) {
            remainingScreens.add(screen)
        } else {
            @Suppress("UNCHECKED_CAST")
            droppedScreens.add(screen as K)
        }
    }

    remainingScreens.reverse()
    this.addAll(remainingScreens)

    return droppedScreens.also { it.reverse() }
}