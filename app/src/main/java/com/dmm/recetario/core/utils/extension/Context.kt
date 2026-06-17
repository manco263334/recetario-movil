package com.dmm.recetario.core.utils.extension

import android.content.Context
import java.io.File
import java.text.DateFormat.getDateTimeInstance
import java.util.Date

fun Context.createImageFile(): File {
    val timestamp = getDateTimeInstance().format(Date())
    val imageFileName = "JPEG_${timestamp}_"
    return File.createTempFile(imageFileName, ".jpg", externalCacheDir)
}