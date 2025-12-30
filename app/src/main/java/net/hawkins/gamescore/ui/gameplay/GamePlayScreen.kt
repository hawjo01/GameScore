package net.hawkins.gamescore.ui.gameplay

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
import net.hawkins.gamescore.data.model.Game
import net.hawkins.gamescore.ui.component.ConfirmActionDialog
import net.hawkins.gamescore.ui.favorites.SaveFavoriteGame
import net.hawkins.gamescore.utils.isNegativeInt

@Composable
fun GamePlayScreen(
    viewModel: GamePlayViewModel,
    onShowGameDetails: (Game) -> Unit,
    onStartNewGame: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.updateTopAppBar(
            newTitle = uiState.game.name,
            newActions = {
                AppBarActions(
                    hasWinningThreshold = uiState.game.objective.goal != null,
                    determineWinner = { viewModel.onEvent(GamePlayUiEvent.DetermineWinner) },
                    onShowGameDetails = onShowGameDetails,
                    onStartNewGame = onStartNewGame,
                    game = uiState.game,
                    players = uiState.players,
                    saveFavoriteGame = { name ->
                        viewModel.onEvent(
                            GamePlayUiEvent.SaveFavoriteGame(
                                name
                            )
                        )
                    },
                    resetGame = { viewModel.onEvent(GamePlayUiEvent.ResetGame) }
                )
            }
        )
    }
    LaunchedEffect(uiState) {
        viewModel.saveGameProgress()
    }

    GamePlayScreenContent(
        uiState = uiState,
        onEvent = { event: GamePlayUiEvent -> viewModel.onEvent(event) },
        isValidScore = { possibleScore -> viewModel.isValidScore(possibleScore) },
        getScoreColor = { score -> viewModel.getScoreColor(score) },
        modifier = modifier
    )
}

@Composable
private fun GamePlayScreenContent(
    uiState: GamePlayUiState,
    onEvent: (GamePlayUiEvent) -> Unit,
    isValidScore: (String) -> Boolean,
    getScoreColor: (String) -> Color,
    modifier: Modifier
) {
    Column(
        modifier = modifier.fillMaxHeight()
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,

            ) {
            Winner(uiState.winner)
        }
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Game(
                uiState = uiState,
                onEvent = onEvent,
                isValidScore = isValidScore,
                getScoreColor = getScoreColor,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun Winner(winner: String?) {
    if (winner != null) {
        Text(
            text = stringResource(R.string.player_wins, winner),
            color = Color.Red,
            style = MaterialTheme.typography.headlineLarge
        )
    }
}

@Composable
private fun Game(
    uiState: GamePlayUiState,
    onEvent: (GamePlayUiEvent) -> Unit,
    isValidScore: (String) -> Boolean,
    getScoreColor: (String) -> Color,
    modifier: Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        PlayerHeader(uiState.players, onEvent, isValidScore, modifier)
        Rounds(
            players = uiState.players,
            onEvent = onEvent,
            numberOfRounds = uiState.numberOfRounds(),
            isValidScore = isValidScore,
            getScoreColor = getScoreColor,
            modifier = modifier
        )
    }
}

@Composable
private fun PlayerHeader(
    players: List<Player>,
    onEvent: (GamePlayUiEvent) -> Unit,
    isValidScore: (String) -> Boolean,
    modifier: Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
        players.forEachIndexed { seatIndex, player ->
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
                        onAddScore = { score: Int ->
                            onEvent(
                                GamePlayUiEvent.AddScore(
                                    seatIndex,
                                    score
                                )
                            )
                        },
                        isValidScore = isValidScore,
                        playerName = player.name,
                        onDismissRequest = { showNewScoreDialog = false },
                        modifier = modifier
                    )
                }
            }
        }
    }
}

@Composable
private fun Rounds(
    players: List<Player>,
    onEvent: (GamePlayUiEvent) -> Unit,
    numberOfRounds: Int,
    isValidScore: (String) -> Boolean,
    getScoreColor: (String) -> Color,
    modifier: Modifier
) {
    val scrollState = rememberLazyListState()
    LaunchedEffect(numberOfRounds) {
        if (numberOfRounds != 0) scrollState.scrollToItem(numberOfRounds - 1)
    }
    LazyColumn(
        state = scrollState,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        items(numberOfRounds) { round ->
            Round(
                round = round,
                players = players,
                onEvent = onEvent,
                isValidScore = isValidScore,
                getScoreColor = getScoreColor,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun Round(
    round: Int,
    players: List<Player>,
    onEvent: (GamePlayUiEvent) -> Unit,
    isValidScore: (String) -> Boolean,
    getScoreColor: (String) -> Color,
    modifier: Modifier
) {
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
        players.forEachIndexed { seatIndex, player ->
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
                color = getScoreColor(score),
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
                    currentScore = player.scores[round],
                    onChangeScore = { newScore ->
                        onEvent(
                            GamePlayUiEvent.ChangeScore(
                                seatIndex,
                                round,
                                newScore
                            )
                        )
                    },
                    isValidScore = isValidScore,
                    onDismissRequest = { showChangeScoreDialog = false },
                    modifier = modifier
                )
            }

            if (showDeleteScoreDialog) {
                DeleteScore(
                    playerName = player.name,
                    score = player.scores[round],
                    onDeleteScore = { onEvent(GamePlayUiEvent.DeleteScore(seatIndex, round)) },
                    onDismissRequest = { showDeleteScoreDialog = false }
                )
            }
        }
    }
}

@Composable
private fun NewScoreDialog(
    onAddScore: (Int) -> Unit,
    playerName: String,
    isValidScore: (String) -> Boolean,
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
                Row(
                    modifier = modifier.padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    OutlinedTextField(
                        value = newScore,
                        onValueChange = {
                            newScore = it
                            if (isValidScore(newScore)) warnInvalidScore = false
                        },
                        label = {
                            Text(
                                text = stringResource(R.string.score_for, playerName),
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        isError = warnInvalidScore,
                        supportingText = {
                            if (warnInvalidScore) {
                                Text(
                                    text = stringResource(R.string.not_a_valid_score),
                                    color = MaterialTheme.colorScheme.error,
                                )
                            }
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
                                if (isValidScore(newScore)) {
                                    warnInvalidScore = false
                                    onAddScore(newScore.toInt())
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
                            focusedTextColor = if (newScore.isNegativeInt() || newScore == "-") Color.Red else Color.Unspecified,
                            unfocusedTextColor = if (newScore.isNegativeInt() || newScore == "-") Color.Red else Color.Unspecified
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
    playerName: String,
    score: Int,
    onDeleteScore: () -> Unit,
    onDismissRequest: () -> Unit
) {
    ConfirmActionDialog(
        title = stringResource(R.string.delete_score),
        description = stringResource(
            R.string.delete_score_for_player,
            score,
            playerName
        ),
        onConfirmation = {
            onDeleteScore()
            onDismissRequest()
        },
        onDismissRequest = onDismissRequest
    )
}

@Composable
private fun ChangeScore(
    currentScore: Int,
    isValidScore: (String) -> Boolean,
    onChangeScore: (Int) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = modifier.padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            var newScore by remember { mutableStateOf(currentScore.toString()) }
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
                        focusedTextColor = if (newScore.isNegativeInt() || newScore == "-") Color.Red else Color.Unspecified,
                        unfocusedTextColor = if (newScore.isNegativeInt() || newScore == "-") Color.Red else Color.Unspecified
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
                        if (isValidScore(newScore)) {
                            onChangeScore(newScore.toInt())
                            onDismissRequest()
                        }
                    },
                    enabled = isValidScore(newScore),
                    modifier = modifier.padding(8.dp),
                ) {
                    Text(stringResource(R.string.update))
                }
            }
        }
    }
}

@Composable
private fun AppBarActions(
    hasWinningThreshold: Boolean,
    determineWinner: () -> Unit,
    game: Game,
    players: List<Player>,
    onShowGameDetails: (Game) -> Unit,
    onStartNewGame: () -> Unit,
    saveFavoriteGame: (String) -> Unit,
    resetGame: () -> Unit,
) {
    val (dropdownMenuExpanded, setDropdownMenuExpanded) = remember { mutableStateOf(false) }
    val (showSaveFavoriteGame, setShowSaveFavoriteGame) = remember { mutableStateOf(false) }
    val (confirmResetGame, setConfirmResetGame) = remember { mutableStateOf(false) }
    val (confirmStartNewGame, setConfirmStartNewGame) = remember { mutableStateOf(false) }

    IconButton(onClick = { setDropdownMenuExpanded(true) }) {
        Icon(imageVector = Icons.Filled.Menu, contentDescription = stringResource(R.string.menu))
    }
    DropdownMenu(
        expanded = dropdownMenuExpanded,
        onDismissRequest = { setDropdownMenuExpanded(false) }
    ) {
        if (!hasWinningThreshold) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(R.string.find_winner),
                        fontSize = 20.sp
                    )
                },
                onClick = {
                    setDropdownMenuExpanded(false)
                    determineWinner()
                }
            )
        }
        DropdownMenuItem(
            text = {
                Text(
                    text = stringResource(R.string.reset_game),
                    fontSize = 20.sp
                )
            },
            onClick = {
                setConfirmResetGame(true)
                setDropdownMenuExpanded(false)
            }
        )
        DropdownMenuItem(
            text = {
                Text(
                    text = stringResource(R.string.new_game),
                    fontSize = 20.sp
                )
            },
            onClick = {
                setConfirmStartNewGame(true)
                setDropdownMenuExpanded(false)
            }
        )
        HorizontalDivider()
        DropdownMenuItem(
            text = {
                Text(
                    text = stringResource(R.string.save_favorite_game),
                    fontSize = 20.sp
                )
            },
            onClick = {
                setShowSaveFavoriteGame(true)
                setDropdownMenuExpanded(false)
            }
        )
        DropdownMenuItem(
            text = {
                Text(
                    text = stringResource(R.string.game_details),
                    fontSize = 20.sp
                )
            },
            onClick = {
                setDropdownMenuExpanded(false)
                onShowGameDetails(game)
            }
        )
    }

    if (confirmResetGame) {
        ConfirmActionDialog(
            title = stringResource(R.string.new_game_confirm_title),
            description = stringResource(R.string.new_game_confirm_description),
            onDismissRequest = { setConfirmResetGame(false) },
            onConfirmation = {
                setConfirmResetGame(false)
                resetGame()
            }
        )
    }

    if (confirmStartNewGame) {
        ConfirmActionDialog(
            title = stringResource(R.string.start_new_game),
            description = stringResource(R.string.start_a_different_game),
            confirmLabel = stringResource(R.string.yes),
            dismissLabel = stringResource(R.string.no),
            onDismissRequest = { setConfirmStartNewGame(false) },
            onConfirmation = {
                setConfirmStartNewGame(false)
                onStartNewGame()
            }
        )
    }

    if (showSaveFavoriteGame) {
        SaveFavoriteGame(
            game,
            players,
            onDismissRequest = { setShowSaveFavoriteGame(false) },
            onConfirmation = { name ->
                saveFavoriteGame(name)
                setShowSaveFavoriteGame(false)
            }
        )
    }
}

@Preview
@Composable
private fun GamePlayScreenContentPreview() {
    val game = Game(
        name = "2500",
        constraints = Game.Constraints(
            multipleOf = 5,
            equalHandSizes = true
        ),
        objective = Game.Objective(
            goal = 2500
        ),
        color = Game.Colors(
            negativeScore = Game.Colors.Color.RED
        )
    )
    val players = listOf(Player("Sheldon", listOf(90, 25)), Player("Leonard", listOf(-20, 40, 235)))
    val uiState = GamePlayUiState(game = game, players = players)
    GamePlayScreenContent(
        uiState = uiState,
        onEvent = { _ -> },
        isValidScore = { _ -> false },
        getScoreColor = { _ -> Color.Transparent },
        modifier = Modifier
    )
}