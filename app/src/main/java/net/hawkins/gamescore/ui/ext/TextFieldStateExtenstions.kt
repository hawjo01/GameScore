package net.hawkins.gamescore.ui.ext

import androidx.compose.foundation.text.input.TextFieldState
import net.hawkins.gamescore.utils.isNegativeInt

fun TextFieldState.setText(text: String) {
    edit {
        replace(0, length, text)
    }
}

fun TextFieldState.isNegativeNumber(): Boolean {
    return this.text.isNegativeInt() || this.text == "-"
}
