import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import net.hawkins.cardscore.R
import net.hawkins.cardscore.data.Player
import net.hawkins.cardscore.ui.GameViewModel
import net.hawkins.cardscore.ui.theme.CardScoreTheme

@Composable
fun CardScore(gameViewModel: GameViewModel = viewModel(), modifier: Modifier = Modifier) {
    val gameUiState by gameViewModel.uiState.collectAsState()
    val players = gameViewModel.getPlayers()
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxHeight()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Winner(gameViewModel, modifier)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Players(players, Modifier.weight(1f))
        }
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ) {
            NewGame(players)
        }
    }
}

@Composable
fun NewGame(players: List<Player>) {
    val showConfirmDialog = remember { mutableStateOf(false) }
    if (showConfirmDialog.value) {
        ConfirmNewGame(
            onDismissRequest = { showConfirmDialog.value = false },
            onConfirmation = {
                for (player in players) {
                    player.resetScores()
                }
                showConfirmDialog.value = false
            }
        )
    }

    OutlinedButton(
        onClick = {
            showConfirmDialog.value = true
        },
        modifier = Modifier.padding(start = 10.dp)
    ) {
        Text(stringResource(R.string.new_game))
    }
}

@Composable
fun Winner(gameViewModel: GameViewModel, modifier: Modifier = Modifier) {
    val winner = gameViewModel.findWinner()
    if (winner != null) {
        Text(
            text = stringResource(R.string.winner, winner.name),
            fontWeight = FontWeight.Bold,
            fontSize = 40.sp,
            textAlign = TextAlign.Center,
            color = Color.Red
        )
    }
}

@Composable
fun Players(players: List<Player>, modifier: Modifier = Modifier) {
    for ((index, player) in players.withIndex()) {
        Player(player = player, index = index, modifier)
    }
}

@Composable
fun Player(player: Player, index: Int, modifier: Modifier = Modifier) {
    var newScore by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val hideKeyboard = { keyboardController?.hide() }

    Column(
        modifier = modifier
            .padding(start = if (index > 0) 10.dp else 0.dp)
    ) {
        Text(
            text = player.name,
            textAlign = TextAlign.Center,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = player.totalScore().toString(),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            fontSize = 22.sp
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically

        ) {
            OutlinedTextField(
                value = newScore,
                onValueChange = { newScore = it },
                label = { Text(text = "")  },
                singleLine = true,
                shape = shapes.small,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        hideKeyboard.invoke()
                    }
                ),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    focusedTextColor = if (isNegative(newScore)) Color.Red else Color.Unspecified,
                    unfocusedTextColor = if (isNegative(newScore)) Color.Red else Color.Unspecified
                ),
                trailingIcon = {
                    IconButton(
                        onClick = {
                            if (newScore.toIntOrNull() != null) {
                                player.addScore(newScore.toInt())
                                newScore = ""
                                hideKeyboard.invoke()
                            }
                        },
                        enabled = newScore.toIntOrNull() != null
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = stringResource(R.string.add)
                        )
                    }
                },
                modifier = modifier.fillMaxWidth()
            )
        }
        for (score in player.scores) {
            Text(
                text = score.toString()
            )
        }
    }
}

fun isNegative(number: String): Boolean {
    val n = number.toIntOrNull()
    if (n != null) {
        return n < 0
    }
    return false
}

@Composable
fun ConfirmNewGame(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    AlertDialog(
        title = {
            Text(text = stringResource(R.string.new_game_confirm_title))
        },
        text = {
            Text(text = stringResource(R.string.new_game_confirm_description))
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text(stringResource(R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(stringResource(R.string.dismiss))
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun CardScorePreview() {
    CardScoreTheme {
        CardScore()
    }
}