package net.hawkins.cardscore.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import net.hawkins.cardscore.ui.theme.CardScoreTheme


@Composable
fun NewGame(gameViewModel: GameViewModel, modifier: Modifier = Modifier) {
    var newPlayerName by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val hideKeyboard = { keyboardController?.hide() }

    Column(modifier = Modifier
        .fillMaxWidth())
    {
//        Text(
//            text = "Add Players",
//            textAlign = TextAlign.Center,
//            fontSize = 26.sp,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(10.dp)
//        )
        Row(
            modifier = Modifier.padding(10.dp)
        ) {
            Text(
                text = "Players: ",
                fontSize = 26.sp,
                modifier = Modifier.padding(top = 10.dp)
            )
            Text(
                text = gameViewModel.getPlayerNames().joinToString(", "),
                fontSize = 26.sp,
                modifier = Modifier.padding(start = 10.dp, top = 10.dp)
            )
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(10.dp)
        ) {
            OutlinedTextField(
                value = newPlayerName,
                onValueChange = { newPlayerName = it },
                label = { Text(text = "Player Name") },
                singleLine = true,
                shape = shapes.small,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Ascii,
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        hideKeyboard.invoke()
                    }
                ),
            )
            Button(
                onClick = {
                    hideKeyboard.invoke()
                    gameViewModel.addPlayerName(newPlayerName)
                    newPlayerName = ""
                },
                modifier = Modifier.padding(start = 10.dp)
            ) {
                Text(
                    text = "Add"
                )
            }
        }
    }
}

@Composable
fun StartGame(gameViewModel: GameViewModel) {
    TextButton(
        onClick = {
            gameViewModel.startGame()
        },
    ) {
        Text(text = "Start Game")
    }
}

@Preview(showBackground = true)
@Composable
fun NewGamePreview() {
    CardScoreTheme {
        NewGame(viewModel())
    }
}