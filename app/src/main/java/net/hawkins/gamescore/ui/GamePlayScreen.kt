package net.hawkins.gamescore.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import net.hawkins.gamescore.R
import net.hawkins.gamescore.Utils
import net.hawkins.gamescore.data.model.FavoriteGame
import net.hawkins.gamescore.game.GamePlay
import net.hawkins.gamescore.game.GamePlay.Player
import net.hawkins.gamescore.data.GameRepository
import net.hawkins.gamescore.ui.component.ConfirmAction
import net.hawkins.gamescore.ui.favorites.SaveFavoriteGame

@Composable
fun GamePlayScreen(
    viewModel: GamePlayViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.updateTopAppBar(
            newTitle = uiState.gamePlay.getGameName(),
            newActions = {
                AppBarActions(
                    gamePlay = uiState.gamePlay,
                    saveFavoriteGame = { favoriteGame -> viewModel.saveFavoriteGame(favoriteGame) })
            }
        )
    }

    GamePlayScreenContent(uiState.gamePlay, modifier)
}

@Composable
private fun GamePlayScreenContent(
    gamePlay: GamePlay,
    modifier: Modifier
) {
    Column(
        modifier = modifier.fillMaxHeight()
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,

            ) {
            Winner(gamePlay)
        }
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Game(gamePlay, modifier)
        }
    }
}

@Composable
private fun Winner(gamePlay: GamePlay) {
    if (gamePlay.hasWinningThreshold()) {
        gamePlay.determineWinner()
    }
    val winner = gamePlay.getWinner()
    if (winner != null) {
        Text(
            text = stringResource(R.string.player_wins, winner.name),
            color = Color.Red,
            style = MaterialTheme.typography.headlineLarge
        )
    }
}

@Composable
private fun Game(
    gamePlay: GamePlay,
    modifier: Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        PlayerHeader(gamePlay, modifier)
        Rounds(gamePlay, modifier)
    }
}

@Composable
private fun PlayerHeader(
    gamePlay: GamePlay,
    modifier: Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
        gamePlay.players.forEach { player ->
            var showNewScoreDialog by remember { mutableStateOf(false) }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .weight(1f)
                    .padding(start = 0.dp)
                    .clickable {
                        showNewScoreDialog = true
                    }
            ) {
                Text(
                    text = player.name,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                    modifier = modifier.fillMaxWidth()
                )
                Text(
                    text = player.totalScore().toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                    modifier = modifier.fillMaxWidth()
                )
                HorizontalDivider(
                    modifier = modifier
                        .fillMaxWidth(.9f)
                        .padding(vertical = 4.dp),
                    color = Color.Gray,
                    thickness = 5.dp
                )
                if (showNewScoreDialog) {
                    NewScoreDialog(
                        gamePlay = gamePlay,
                        player = player,
                        onDismissRequest = { showNewScoreDialog = false },
                        modifier = modifier
                    )
                }
            }
        }
    }
}

@Composable
private fun Rounds(gamePlay: GamePlay, modifier: Modifier) {
    val scrollState = rememberLazyListState()
    LaunchedEffect(gamePlay.numberOfRounds()) {
        if (gamePlay.numberOfRounds() != 0) scrollState.scrollToItem(gamePlay.numberOfRounds() - 1)
    }
    LazyColumn(
        state = scrollState,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        items(gamePlay.numberOfRounds()) { round ->
            Round(gamePlay, round, modifier)
        }
    }
}

@Composable
private fun Round(gamePlay: GamePlay, round: Int, modifier: Modifier) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .background(
                if (round % 2 != 0) {
                    if (isSystemInDarkTheme()) {
                        Color.DarkGray
                    } else {
                        Color.LightGray
                    }
                } else {
                    Color.Transparent
                }
            )
    ) {
        gamePlay.players.forEach { player ->
            var showChangeScoreDialog by remember { mutableStateOf(false) }
            var showDeleteScoreDialog by remember { mutableStateOf(false) }

            val score = if (player.scores.size >= round + 1) {
                player.scores[round].toString()
            } else {
                ""
            }
            Text(
                text = score.padStart(player.totalScore().toString().length, ' '),
                style = MaterialTheme.typography.displayMedium,
                textAlign = TextAlign.Center,
                color = if (Utils.isNegativeInt(score) && gamePlay.highlightNegativeScore()) Color.Red else Color.Unspecified,
                modifier = modifier
                    .weight(1f)
                    .combinedClickable(onLongClick = {
                        showDeleteScoreDialog = true
                    }, onClick = {
                        showChangeScoreDialog = true
                    })
            )

            if (showChangeScoreDialog) {
                ChangeScore(
                    gamePlay = gamePlay,
                    player = player,
                    round = round,
                    onDismissRequest = { showChangeScoreDialog = false },
                    modifier = modifier
                )
            }

            if (showDeleteScoreDialog) {
                DeleteScore(
                    player = player,
                    round = round,
                    onDismissRequest = { showDeleteScoreDialog = false }
                )
            }

        }
    }
}

@Composable
private fun NewScoreDialog(
    gamePlay: GamePlay,
    player: Player,
    onDismissRequest: () -> Unit,
    modifier: Modifier
) {
    var newScore by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val hideKeyboard = { keyboardController?.hide() }
    val focusRequester = remember { FocusRequester() }
    var warnInvalidScore by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = modifier.padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (warnInvalidScore) {
                    Row(
                        modifier = modifier.padding(horizontal = 10.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(R.string.not_a_valid_score),
                            color = Color.Red,
                            textAlign = TextAlign.Center,
                        )
                    }
                }

                Row(
                    modifier = modifier.padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    OutlinedTextField(
                        value = newScore,
                        onValueChange = {
                            newScore = it
                            if (gamePlay.isValidScore(newScore)) warnInvalidScore = false
                        },
                        label = {
                            Text(
                                text = stringResource(R.string.score_for, player.name),
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        textStyle = MaterialTheme.typography.labelSmall.plus(TextStyle(textAlign = TextAlign.Center)),
                        singleLine = true,
                        shape = shapes.small,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if (gamePlay.isValidScore(newScore)) {
                                    warnInvalidScore = false
                                    player.addScore(newScore.toInt())
                                    newScore = ""
                                    hideKeyboard.invoke()
                                    onDismissRequest()
                                } else if (newScore == "") {
                                    warnInvalidScore = false
                                    hideKeyboard.invoke()
                                    onDismissRequest()
                                } else {
                                    warnInvalidScore = true
                                }
                            }
                        ),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = if (Utils.isNegativeInt(newScore) || newScore == "-") Color.Red else Color.Unspecified,
                            unfocusedTextColor = if (Utils.isNegativeInt(newScore) || newScore == "-") Color.Red else Color.Unspecified
                        ),
                        modifier = modifier
                            .focusRequester(focusRequester)
                            .padding(vertical = 20.dp)
                    )
                }
            }
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
        }
    }
}

@Composable
private fun DeleteScore(
    player: Player,
    round: Int,
    onDismissRequest: () -> Unit
) {
    ConfirmAction(
        title = stringResource(R.string.delete_score),
        description = stringResource(
            R.string.delete_score_for_player,
            player.scores[round],
            player.name
        ),
        onConfirmation = {
            player.deleteScore(round)
            onDismissRequest()
        },
        onDismissRequest = onDismissRequest
    )
}

@Composable
private fun ChangeScore(
    gamePlay: GamePlay,
    player: Player,
    round: Int,
    onDismissRequest: () -> Unit,
    modifier: Modifier
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = modifier.padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            var newScore by remember { mutableStateOf(player.scores[round].toString()) }
            val keyboardController = LocalSoftwareKeyboardController.current
            val hideKeyboard = { keyboardController?.hide() }

            Row(
                modifier = modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    value = newScore,
                    onValueChange = { newScore = it },
                    label = {
                        Text(
                            text = stringResource(R.string.change_score),
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    textStyle = MaterialTheme.typography.labelSmall.plus(TextStyle(textAlign = TextAlign.Center)),
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
                        focusedTextColor = if (Utils.isNegativeInt(newScore) || newScore == "-") Color.Red else Color.Unspecified,
                        unfocusedTextColor = if (Utils.isNegativeInt(newScore) || newScore == "-") Color.Red else Color.Unspecified
                    ),
                    modifier = modifier.padding(top = 10.dp)
                )
            }
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                TextButton(
                    onClick = { onDismissRequest() },
                    modifier = modifier.padding(8.dp),
                ) {
                    Text(stringResource(R.string.cancel))
                }
                TextButton(
                    onClick = {
                        if (gamePlay.isValidScore(newScore)) {
                            player.changeScore(newScore = newScore.toInt(), scoreIndex = round)
                            onDismissRequest()
                        }
                    },
                    enabled = gamePlay.isValidScore(newScore),
                    modifier = modifier.padding(8.dp),
                ) {
                    Text(stringResource(R.string.update))
                }
            }
        }
    }
}

@Composable
private fun ConfirmResetGame(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    ConfirmAction(
        title = stringResource(R.string.new_game_confirm_title),
        description = stringResource(R.string.new_game_confirm_description),
        onConfirmation = onConfirmation,
        onDismissRequest = onDismissRequest
    )
}

@Composable
private fun AppBarActions(gamePlay: GamePlay, saveFavoriteGame: (FavoriteGame) -> Unit) {

    var dropdownMenuExpanded by remember { mutableStateOf(false) }
    var showSaveFavoriteGame by remember { mutableStateOf(false) }
    var showResetGameDialog by remember { mutableStateOf(false) }

    IconButton(onClick = { dropdownMenuExpanded = true }) {
        Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menu")
    }
    DropdownMenu(
        expanded = dropdownMenuExpanded,
        onDismissRequest = { dropdownMenuExpanded = false }
    ) {
        if (!gamePlay.hasWinningThreshold()) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(R.string.find_winner),
                        fontSize = 20.sp
                    )
                },
                onClick = {
                    gamePlay.determineWinner()
                    dropdownMenuExpanded = false
                }
            )
        }
        DropdownMenuItem(
            text = {
                Text(
                    text = "Favorite Game",
                    fontSize = 20.sp
                )
            },
            onClick = {
                showSaveFavoriteGame = true
                dropdownMenuExpanded = false
            }
        )
        DropdownMenuItem(
            text = {
                Text(
                    text = stringResource(R.string.reset_game),
                    fontSize = 20.sp
                )
            },
            onClick = {
                showResetGameDialog = true
                dropdownMenuExpanded = false
            }
        )
    }

    if (showResetGameDialog) {
        ConfirmResetGame(
            onDismissRequest = { showResetGameDialog = false },
            onConfirmation = {
                gamePlay.resetGame()
                showResetGameDialog = false
            }
        )
    }

    if (showSaveFavoriteGame) {
        SaveFavoriteGame(
            gamePlay,
            onDismissRequest = { showSaveFavoriteGame = false },
            onConfirmation = { name ->
                saveFavoriteGame(
                    FavoriteGame(
                        name = name.trim(),
                        players = gamePlay.players.map { player -> player.name },
                        game = gamePlay.getGameName()
                    )
                )
                showSaveFavoriteGame = false
            }
        )
    }
}

@Preview
@Composable
private fun GamePlayScreenContentPreview() {
    val gamePlay = GamePlay(GameRepository.getByName("2500"), listOf("Sheldon", "Leonard"))
    gamePlay.players[0].addScore(90)
    gamePlay.players[0].addScore(25)
    gamePlay.players[1].addScore(-20)
    gamePlay.players[1].addScore(40)
    gamePlay.players[1].addScore(235)
    GamePlayScreenContent(
        gamePlay,
        Modifier
    )
}