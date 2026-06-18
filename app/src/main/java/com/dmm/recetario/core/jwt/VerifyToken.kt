package com.dmm.recetario.core.jwt

import android.util.Log
import org.json.JSONObject
import kotlin.io.encoding.Base64
import kotlin.io.encoding.Base64.Default.withPadding

fun isTokenExpired(token: String): Boolean {
    if (token.isBlank()) return true

    val parts = token.split('.')

    if (parts.size != 3) return true

    return try {
        val payload = parts[1]

        val noPadded = withPadding(option = Base64.PaddingOption.ABSENT)
        val decodedBytes = noPadded.decode(payload)

        val decodedPayload = String(decodedBytes)

        val jsonPayload = JSONObject(decodedPayload)

        val expirationTime = jsonPayload.getLong("exp")

        val currentTime = System.currentTimeMillis() / 1000

        currentTime > expirationTime
    } catch (e: Exception) {
        Log.e("VerifyToken", "Error decoding token: ${e.message}")
        // If decoding or parsing fails, treat token as expired/invalid
        true
    }
}