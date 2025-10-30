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
import androidx.compose.ui.window.Dialog
import net.hawkins.gamescore.R
import net.hawkins.gamescore.Utils
import net.hawkins.gamescore.game.Game
import net.hawkins.gamescore.game.Player
import net.hawkins.gamescore.game.type.TwentyFiveHundred
import net.hawkins.gamescore.model.FavoriteGame
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
            newTitle = uiState.game.getGameName(),
            newActions = {
                AppBarActions(
                    game = uiState.game,
                    saveFavoriteGame = { favoriteGame -> viewModel.saveFavoriteGame(favoriteGame) })
            }
        )
    }

    GamePlayScreenContent(uiState.game, modifier)
}

@Composable
private fun GamePlayScreenContent(
    game: Game,
    modifier: Modifier
) {
    val game = game
    Column(
        modifier = modifier.fillMaxHeight()
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,

            ) {
            Winner(game)
        }
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Game(game, modifier)
        }
    }
}

@Composable
private fun Winner(game: Game) {
    if (game.hasWinningThreshold()) {
        game.determineWinner()
    }
    val winner = game.getWinner()
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
    game: Game,
    modifier: Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        PlayerHeader(game, modifier)
        Rounds(game, modifier)
    }
}

@Composable
private fun PlayerHeader(
    game: Game,
    modifier: Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
        game.players.forEach { player ->
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
                        game = game,
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
private fun Rounds(game: Game, modifier: Modifier) {
    val scrollState = rememberLazyListState()
    LaunchedEffect(game.numberOfRounds()) {
        if (game.numberOfRounds() != 0) scrollState.scrollToItem(game.numberOfRounds() - 1)
    }
    LazyColumn(
        state = scrollState,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        items(game.numberOfRounds()) { round ->
            Round(game, round, modifier)
        }
    }
}

@Composable
private fun Round(game: Game, round: Int, modifier: Modifier) {
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
        game.players.forEach { player ->
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
                color = if (Utils.isNegativeInt(score) && game.highlightNegativeScore()) Color.Red else Color.Unspecified,
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
                    game = game,
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
    game: Game,
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
                            if (game.isValidScore(newScore)) warnInvalidScore = false
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
                                if (game.isValidScore(newScore)) {
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
    game: Game,
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
                        if (game.isValidScore(newScore)) {
                            player.changeScore(newScore = newScore.toInt(), scoreIndex = round)
                            onDismissRequest()
                        }
                    },
                    enabled = game.isValidScore(newScore),
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
private fun AppBarActions(game: Game, saveFavoriteGame: (FavoriteGame) -> Unit) {

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
        if (!game.hasWinningThreshold()) {
            DropdownMenuItem(
                text = { Text(text = stringResource(R.string.find_winner)) },
                onClick = {
                    game.determineWinner()
                    dropdownMenuExpanded = false
                }
            )
        }
        DropdownMenuItem(
            text = { Text("Favorite Game") },
            onClick = {
                showSaveFavoriteGame = true
                dropdownMenuExpanded = false
            }
        )
        DropdownMenuItem(
            text = { Text(text = stringResource(R.string.reset_game)) },
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
                game.resetGame()
                showResetGameDialog = false
            }
        )
    }

    if (showSaveFavoriteGame) {
        SaveFavoriteGame(
            game,
            onDismissRequest = { showSaveFavoriteGame = false },
            onConfirmation = { name ->
                saveFavoriteGame(
                    FavoriteGame(
                        name = name.trim(),
                        players = game.players.map { player -> player.name },
                        game = game.getGameName()
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
    val game = Game(TwentyFiveHundred, listOf("Sheldon", "Leonard"))
    game.players[0].addScore(90)
    game.players[0].addScore(25)
    game.players[1].addScore(-20)
    game.players[1].addScore(40)
    game.players[1].addScore(235)
    GamePlayScreenContent(
        game,
        Modifier
    )
}