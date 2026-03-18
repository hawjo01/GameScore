package net.hawkins.gamescore.ui.ext

import androidx.compose.foundation.text.input.TextFieldState

fun TextFieldState.setText(text: String) {
    edit {
        replace(0, length, text)
    }
}
