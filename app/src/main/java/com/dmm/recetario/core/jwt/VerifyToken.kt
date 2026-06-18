package com.dmm.recetario.core.jwt

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.codec.binary.Base64
import org.json.JSONObject

fun isTokenExpired(token: String): Boolean {
    if (token.isBlank()) return true

    val parts = token.split('.')
    if (parts.size != 3) return true

    val payload = parts[1]
    val decodedPayload = String(Base64.decodeBase64(payload))
    val jsonPayload = JSONObject(decodedPayload)
    val expirationTime = jsonPayload.getLong("exp")
    val currentTime = System.currentTimeMillis() / 1000

    return currentTime > expirationTime
}