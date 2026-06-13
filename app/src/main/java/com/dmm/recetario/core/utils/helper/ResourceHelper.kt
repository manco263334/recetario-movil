package com.dmm.recetario.core.utils.helper

import android.content.Context
import androidx.annotation.ArrayRes
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class ResourceHelper @Inject constructor (
    @param:ApplicationContext
    private val context: Context
) {
    fun getString(@StringRes resId: Int): String =
        context.getString(resId)

    fun getString(@StringRes resId: Int, vararg formatArgs: Any): String =
        context.getString(resId, *formatArgs)

    fun getStrings(@StringRes vararg resIds: Int): List<String> =
        resIds.map { 
            context.getString(it)
        }

    fun getStrings(@ArrayRes resId: Int): List<String> =
        context.resources.getStringArray(resId).toList()

    fun getPluralString(@PluralsRes resId: Int, quantity: Int) =
        context.resources.getQuantityString(resId, quantity)

    fun getPluralString(@PluralsRes resId: Int, quantity: Int, vararg formatArgs: Any) =
        context.resources.getQuantityString(resId, quantity, formatArgs)
}