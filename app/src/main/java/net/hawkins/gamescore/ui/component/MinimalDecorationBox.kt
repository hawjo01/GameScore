package net.hawkins.gamescore.ui.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.text.input.TextFieldDecorator
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

class MinimalDecorationBox(val warnInvalidScore: Boolean, val text: String) : TextFieldDecorator {

    @Composable
    override fun Decoration(innerTextField: @Composable (() -> Unit)) {
        OutlinedTextFieldDefaults.DecorationBox(
            value = text,
            innerTextField = innerTextField,
            enabled = true,
            singleLine = true,
            visualTransformation = VisualTransformation.None,
            interactionSource = remember { MutableInteractionSource() },
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
            container = {
                OutlinedTextFieldDefaults.Container(
                    enabled = true,
                    isError = warnInvalidScore,
                    interactionSource = remember { MutableInteractionSource() },
                )
            }
        )
    }
}