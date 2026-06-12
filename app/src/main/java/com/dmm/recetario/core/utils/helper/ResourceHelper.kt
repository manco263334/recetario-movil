package com.dmm.recetario.core.utils.helper

import android.content.Context

class ResourceHelper(private val context: Context) {
    fun getString(resId: Int): String =
        context.getString(resId)

    fun getString(resId: Int, vararg formatArgs: Any): String =
        context.getString(resId, *formatArgs)

    fun getStrings(vararg resIds: Int): List<String> =
        resIds.map { 
            context.getString(it)
        }

    fun getStrings(resId: Int): List<String> =
        context.resources.getStringArray(resId).toList()
}