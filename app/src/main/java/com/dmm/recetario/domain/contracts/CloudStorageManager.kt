package com.dmm.recetario.domain.contracts

import android.net.Uri

interface CloudStorageManager {
    fun uploadFile(filename: String, filePath: Uri)
}