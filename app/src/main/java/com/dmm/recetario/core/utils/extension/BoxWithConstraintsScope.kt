package com.dmm.recetario.core.utils.extension

import androidx.compose.foundation.layout.BoxWithConstraintsScope

fun BoxWithConstraintsScope.calculateColumns(): Int {
    val paneWidth = maxWidth.value.toInt()

    return if (paneWidth < 400) 1 else paneWidth / 200
}