package com.dmm.recetario.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import com.dmm.recetario.core.utils.helper.ResourceHelper
import com.dmm.recetario.domain.model.User

@Composable
fun WelcomeHeader(user: User?, resourceHelper: ResourceHelper) {
    var text = "Bienvenido, "
    text += user?.name ?: "Invitado"

    user?.username?.also {
        text += " ($it)"
    }

    Text(text, fontWeight = FontWeight.Bold)
}