package com.dmm.recetario.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.dmm.recetario.R
import com.dmm.recetario.core.utils.extension.isNeitherNullNorAnonymous
import com.dmm.recetario.domain.model.User

@Composable
fun WelcomeHeader(user: User?) {
    var text = if (user.isNeitherNullNorAnonymous())
            stringResource(R.string.welcome, user.name)
        else
            stringResource(R.string.welcome_anonymous)

    user?.username?.also {
        text += " ($it)"
    }

    Text(text, fontWeight = FontWeight.Bold)
}