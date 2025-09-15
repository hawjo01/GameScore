package net.hawkins.gamescore.ui

import android.content.ClipDescription
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.mimeTypes
import androidx.compose.ui.draganddrop.toAndroidDragEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.Typeface
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import net.hawkins.gamescore.R
import net.hawkins.gamescore.Utils
import net.hawkins.gamescore.data.Player
import net.hawkins.gamescore.ui.theme.GameScoreTheme

@Composable
fun GamePlay(gameViewModel: GameViewModel, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxHeight()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,

            ) {
            Winner(gameViewModel)
        }
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Players(gameViewModel, Modifier.weight(1f))
        }
    }
}

@Composable
fun Winner(gameViewModel: GameViewModel) {
    if (gameViewModel.hasWinningThreshold()) {
        gameViewModel.determineWinner()
    }
    val winner = gameViewModel.getWinner()
    if (winner != null) {
        Winner(winner)
    }
}

@Composable
fun Winner(winner: Player) {
    Text(
        text = stringResource(R.string.player_wins, winner.name),
        fontWeight = FontWeight.Bold,
        fontSize = 40.sp,
        textAlign = TextAlign.Center,
        color = Color.Red
    )
}

@Composable
fun Players(gameViewModel: GameViewModel, modifier: Modifier = Modifier) {
    val players by remember { mutableStateOf( gameViewModel.getPlayers()) }
    for ((index, player) in gameViewModel.getPlayers().withIndex()) {
        key(player.name) {
            Player(gameViewModel = gameViewModel, player = player, index = index, modifier)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Player(
    gameViewModel: GameViewModel,
    player: Player,
    index: Int,
    modifier: Modifier = Modifier
) {
    var newScore by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val hideKeyboard = { keyboardController?.hide() }

    val callback = remember {
        object : DragAndDropTarget {
            override fun onDrop(event: DragAndDropEvent): Boolean {
                val from = event.toAndroidDragEvent().clipData.getItemAt(0).text
                val fromIndex = from.toString().toInt()
                println("Entered onDrop with index = " + index + ", and from = " + fromIndex)
//                if (fromIndex < index) {
                    gameViewModel.movePlayer(fromIndex, index)
//                }
                return true
            }
            override fun onEntered(event: DragAndDropEvent) {
                println("Entered onEntered with onEntered = " + index)
                super.onEntered(event)
            }
            override fun onExited(event: DragAndDropEvent) {
                println("Entered onExited with onExited = " + index)
                super.onExited(event)
            }

            override fun onEnded(event: DragAndDropEvent) {
                println("Entered onEnded drag event")
                super.onEnded(event)
            }
        }
    }
    Column(
        modifier = modifier
            .padding(start = if (index > 0) 10.dp else 0.dp)
            .dragAndDropTarget(
                shouldStartDragAndDrop = { event -> event.mimeTypes().contains(ClipDescription.MIMETYPE_TEXT_PLAIN) },
                target = callback
            ),
    ) {
        val playerName = player.name
        println("Composing for player = " + playerName)
        Text(
            text = playerName,
            textAlign = TextAlign.Center,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = player.totalScore().toString(),
            textAlign = TextAlign.Center,
            fontSize = 24.sp,
            modifier = Modifier.fillMaxWidth()
//                .dragAndDropSource {
//                    detectTapGestures(
//                        onLongPress = {
//                            startTransfer(
//                                DragAndDropTransferData(
//                                    ClipData(
//                                        "Player",
//                                        arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
//                                        ClipData.Item(index.toString())
//                                    )
//                                )
//                            )
//                        }
//                    )
//                }
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = newScore,
                onValueChange = { newScore = it },
                label = { Text(text = "") },
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
                    focusedTextColor = if (Utils.isNegativeInt(newScore)) Color.Red else Color.Unspecified,
                    unfocusedTextColor = if (Utils.isNegativeInt(newScore)) Color.Red else Color.Unspecified
                ),
                trailingIcon = {
                    IconButton(
                        onClick = {
                            if (gameViewModel.isValidScore(newScore)) {
                                player.addScore(newScore.toInt())
                                newScore = ""
                                hideKeyboard.invoke()
                            }
                        },
                        enabled = gameViewModel.isValidScore(newScore)
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
        Row(
            modifier = Modifier.padding(top = 8.dp)
        ) {
            val scrollState = rememberLazyListState()
            LaunchedEffect(player.scores.size) {
                if (player.scores.isNotEmpty()) scrollState.scrollToItem(player.scores.size - 1)
            }

            LazyColumn(
                state = scrollState,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(weight = 1f, fill = false)
                    .fillMaxWidth()
            ) {
                itemsIndexed(player.scores) { index, score ->
                    var showChangeScore by remember { mutableStateOf(false) }

                    Text(
                        text = score.toString().padStart(5, ' '),
                        textAlign = TextAlign.Right,
                        fontFamily = FontFamily(Typeface(android.graphics.Typeface.MONOSPACE)),
                        fontSize = 20.sp,
                        color = gameViewModel.getScoreColor(score),
                        modifier = Modifier.clickable {
                            showChangeScore = true
                        }
                    )
                    if (showChangeScore) {
                        ChangeScore(
                            gameViewModel = gameViewModel,
                            player = player,
                            scoreIndex = index,
                            onDismissRequest = { showChangeScore = false }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChangeScore(
    gameViewModel: GameViewModel,
    player: Player,
    scoreIndex: Int,
    onDismissRequest: () -> Unit
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            var newScore by remember { mutableStateOf(player.scores[scoreIndex].toString()) }
            val keyboardController = LocalSoftwareKeyboardController.current
            val hideKeyboard = { keyboardController?.hide() }

            Row(
                modifier = Modifier.padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    value = newScore,
                    onValueChange = { newScore = it },
                    label = { Text(text = "Change Score") },
                    singleLine = true,
                    shape = shapes.small,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            hideKeyboard.invoke()
                        }
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = if (Utils.isNegativeInt(newScore)) Color.Red else Color.Unspecified,
                        unfocusedTextColor = if (Utils.isNegativeInt(newScore)) Color.Red else Color.Unspecified
                    ),
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                TextButton(
                    onClick = { onDismissRequest() },
                    modifier = Modifier.padding(8.dp),
                ) {
                    Text(stringResource(R.string.cancel))
                }
                TextButton(
                    onClick = {
                        if (gameViewModel.isValidScore(newScore)) {
                            player.changeScore(newScore = newScore.toInt(), scoreIndex = scoreIndex)
                            onDismissRequest()
                        }
                    },
                    enabled = gameViewModel.isValidScore(newScore),
                    modifier = Modifier.padding(8.dp),
                ) {
                    Text(stringResource(R.string.update))
                }
            }
        }
    }
}

@Composable
fun ResetGame(gameViewModel: GameViewModel) {
    val showConfirmDialog = remember { mutableStateOf(false) }
    if (showConfirmDialog.value) {
        ConfirmResetGame(
            onDismissRequest = { showConfirmDialog.value = false },
            onConfirmation = {
                gameViewModel.resetGame()
                showConfirmDialog.value = false
            }
        )
    }

    if (!gameViewModel.hasWinningThreshold()) {
        TextButton(
            onClick = { gameViewModel.determineWinner() }
        ) {
            Text(text = stringResource(R.string.find_winner))
        }
    }

    TextButton(
        onClick = {
            showConfirmDialog.value = true
        },
    ) {
        Text(text = stringResource(R.string.new_game))
    }
}

@Composable
fun ConfirmResetGame(
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
fun GamePlayPreview() {
    GameScoreTheme {
        GamePlay(viewModel())
    }
}