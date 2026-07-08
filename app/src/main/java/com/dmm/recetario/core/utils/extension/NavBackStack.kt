package com.dmm.recetario.core.utils.extension

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

/**
 * Navigates to the [screen] specified.
 * If [screen] is already on the [NavBackStack] then returns instead of adding.
 *
 * @param screen The screen to navigate
 * @return [Boolean] - Whether the [screen] was added or not
 */
fun <T : NavKey> NavBackStack<T>.navigateTo(screen: T): Boolean {
    if (screen in this) {
        this.backTo(screen)
        return false
    }

    return this.add(screen)
}

/**
 * Returns to the last screen on the [NavBackStack].
 * If there's no more screens then returns null.
 */
fun <T : NavKey> NavBackStack<T>.back(): T? {
    if (this.isEmpty()) return null

    return this.removeLastOrNull()
}

/**
 * Returns to the [targetScreen] specified.
 * If [targetScreen] is not present or the [NavBackStack] is empty then returns null.
 *
 * @param targetScreen The screen to return
 * @return [NavBackStack] - A [NavBackStack] with screens that were eliminated or null if no
 *  screens were deleted
 */
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

/**
 * Removes all the screens where its type is the same as the specified on [targetKey].
 *
 * @param [targetKey] The key to eliminate
 * @return [NavBackStack] - A [NavBackStack]
 */
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