package com.dmm.recetario.core.jwt

import org.json.JSONObject
import kotlin.io.encoding.Base64

fun isTokenExpired(token: String): Boolean {
    if (token.isBlank()) return true

    val parts = token.split('.')
    if (parts.size != 3) return true

    val payload = parts[1]
    val decodedPayload = String(Base64.decode(payload))
    val jsonPayload = JSONObject(decodedPayload)
    val expirationTime = jsonPayload.getLong("exp")
    val currentTime = System.currentTimeMillis() / 1000

    return currentTime > expirationTime
}