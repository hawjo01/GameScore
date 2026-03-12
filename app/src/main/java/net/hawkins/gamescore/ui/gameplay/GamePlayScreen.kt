package net.hawkins.gamescore.ui.gameplay

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Leaderboard
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults.FocusedBorderThickness
import androidx.compose.material3.OutlinedTextFieldDefaults.UnfocusedBorderThickness
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.hawkins.gamescore.R
import net.hawkins.gamescore.data.model.Game
import net.hawkins.gamescore.ui.NavigationEvent
import net.hawkins.gamescore.ui.component.ConfirmActionDialog
import net.hawkins.gamescore.ui.favorites.SaveFavoriteGame
import net.hawkins.gamescore.utils.isNegativeInt
import net.hawkins.gamescore.utils.removeAllWhitespace


@Composable
fun GamePlayScreen(
    viewModel: GamePlayViewModel,
    onShowGameDetails: (Game) -> Unit,
    onStartNewGame: () -> Unit,
    onShowLeaderboard: (Game, List<Player>) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {

        viewModel.onEvent(GamePlayUiEvent.RefreshState)

        viewModel.updateTopAppBar(
            newTitle = uiState.game.name,
            newActions = {
                AppBarActions(
                    showFindWinner = viewModel.isManualWinner(),
                    determineWinner = { viewModel.onEvent(GamePlayUiEvent.DetermineWinner) },
                    onShowGameDetails = onShowGameDetails,
                    onStartNewGame = onStartNewGame,
                    onShowLeaderboard = onShowLeaderboard,
                    game = uiState.game,
                    players = uiState.players,
                    saveFavoriteGame = { name ->
                        viewModel.onEvent(
                            GamePlayUiEvent.SaveFavoriteGame(
                                name
                            )
                        )
                    },
                    resetGame = { viewModel.onEvent(GamePlayUiEvent.ResetGame) },
                    showRoundNumber = uiState.showRoundNumber,
                    onShowRoundNumber = { showRoundNumber ->
                        viewModel.onEvent(
                            GamePlayUiEvent.ShowRoundNumber(
                                showRoundNumber
                            )
                        )
                    },
                    modifier = modifier
                )
            }
        )
    }
    LaunchedEffect(uiState) {
        viewModel.saveGameProgress()
    }

    val navigationEvent by viewModel.navigationEvents.collectAsState(initial = null)

    LaunchedEffect(navigationEvent) {
        when (navigationEvent) {
            is NavigationEvent.NavigateToLeaderboard -> onShowLeaderboard(
                uiState.game,
                uiState.players
            )

            null -> {}
        }
    }

    GamePlayScreenContent(
        uiState = uiState,
        onEvent = { event: GamePlayUiEvent -> viewModel.onEvent(event) },
        isValidScore = { possibleScore -> viewModel.isValidScore(possibleScore) },
        modifier = modifier
    )
}

@Composable
private fun GamePlayScreenContent(
    uiState: GamePlayUiState,
    onEvent: (GamePlayUiEvent) -> Unit,
    isValidScore: (String) -> Boolean,
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
    modifier: Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        PlayerHeader(
            players = uiState.players,
            showRoundNumber = uiState.showRoundNumber,
            modifier = modifier
        )
        Rounds(
            players = uiState.players,
            onEvent = onEvent,
            numberOfRounds = uiState.numberOfRounds(),
            isValidScore = isValidScore,
            showRoundNumber = uiState.showRoundNumber,
            modifier = modifier
        )
    }
}

@Composable
private fun PlayerHeader(
    players: List<Player>,
    showRoundNumber: Boolean,
    modifier: Modifier
) {
    Row(
        verticalAlignment = Alignment.Bottom,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
        if (showRoundNumber) {
            Column(
                verticalArrangement = Arrangement.Bottom,
                modifier = modifier
            ) {
                val textMeasurer = rememberTextMeasurer()
                val density = LocalDensity.current

                // Measure the width of two specific characters, for example "MM"
                // to approximate a consistent width for two characters
                val textLayoutResult = textMeasurer.measure(
                    text = "##"
                )

                // Convert the measured pixel width to Dp
                val twoCharWidth: Dp = with(density) { textLayoutResult.size.width.toDp() }

                Text(
                    text = "#",
                    modifier.width(twoCharWidth)
                )
            }
        }
        players.forEach { player ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .weight(1f)
                    .padding(start = 0.dp)
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
    showRoundNumber: Boolean,
    modifier: Modifier
) {
    val scrollState = rememberLazyListState()
    LaunchedEffect(numberOfRounds) {
        if (numberOfRounds != 0) scrollState.scrollToItem(numberOfRounds - 1)
    }
    LazyColumn(
        state = scrollState,
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(bottom = 20.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        items(numberOfRounds) { round ->
            Round(
                round = round,
                players = players,
                onEvent = onEvent,
                isValidScore = isValidScore,
                showRoundNumber = showRoundNumber,
                modifier = modifier
            )
        }
        item {
            Round(
                round = numberOfRounds,
                players = players,
                onEvent = onEvent,
                isValidScore = isValidScore,
                showRoundNumber = showRoundNumber,
                modifier = modifier
            )
        }
    }
}

@Composable
fun measureTextHeight(text: String, style: TextStyle): Dp {
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current

    val textLayoutResult = textMeasurer.measure(
        text = text,
        style = style,
    )

    return with(density) {
        textLayoutResult.size.height.toDp()
    }
}

@Composable
fun ScoreInputField(
    initialValue: String,
    onAddScore: (Int) -> Unit,
    isValidScore: (String) -> Boolean,
    modifier: Modifier
) {
    val initialText = getNewScoreText(initialValue)
    var newScore by remember { mutableStateOf(initialText) }
    val (warnInvalidScore, setWarnInvalidScore) = remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val hideKeyboard = { keyboardController?.hide() }

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        val textStyle = MaterialTheme.typography.displayMedium
        val height = measureTextHeight(newScore, textStyle) + 4.dp
        BasicTextField(
            value = newScore,
            onValueChange = { newScore = getNewScoreText(it) },
            modifier = modifier
                .height(height)
                .width(IntrinsicSize.Min),
            textStyle = textStyle.plus(TextStyle(color = if (newScore.isNegativeInt() || newScore == "-") Color.Red else MaterialTheme.colorScheme.onSurface)),
            decorationBox = { innerTextField ->
                OutlinedTextFieldDefaults.DecorationBox(
                    value = newScore,
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
                            colors = OutlinedTextFieldDefaults.colors(),
                            shape = OutlinedTextFieldDefaults.shape,
                            focusedBorderThickness = FocusedBorderThickness,
                            unfocusedBorderThickness = UnfocusedBorderThickness,
                        )
                    }
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (isValidScore(newScore)) {
                        setWarnInvalidScore(false)
                        onAddScore(newScore.removeAllWhitespace().toInt())
                        hideKeyboard.invoke()
                    } else if (newScore.removeAllWhitespace() == "") {
                        setWarnInvalidScore(false)
                        hideKeyboard.invoke()
                    } else {
                        setWarnInvalidScore(true)
                    }
                }
            )
        )
    }
}

private fun getNewScoreText(value: String): String {
    val trimmed = value.removeAllWhitespace()
    val text = if (trimmed == "") {
        "   " // 3 spaces so the input text field has some length
    } else {
        trimmed
    }
    return text
}

@Composable
private fun Round(
    round: Int,
    players: List<Player>,
    onEvent: (GamePlayUiEvent) -> Unit,
    isValidScore: (String) -> Boolean,
    showRoundNumber: Boolean,
    modifier: Modifier
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
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
        if (showRoundNumber) {
            Text(text = (round + 1).toString())
        }
        players.forEachIndexed { seatIndex, player ->
            val score = player.scores.getOrNull(round)
            if (score != null) {
                var showChangeScoreDialog by remember { mutableStateOf(false) }
                var showDeleteScoreDialog by remember { mutableStateOf(false) }

                val scoreText =
                    score.displayValue ?: score.value.toString()
                        .padStart(player.totalScore().toString().length, ' ')

                Text(
                    text = scoreText,
                    style = MaterialTheme.typography.displayMedium,
                    textAlign = TextAlign.Center,
                    color = score.color,
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
                        playerName = player.name,
                        currentScore = score.value,
                        onChangeScore = { newScore ->
                            onEvent(GamePlayUiEvent.ChangeScore(seatIndex, round, newScore))
                        },
                        isValidScore = isValidScore,
                        onDismissRequest = { showChangeScoreDialog = false },
                        modifier = modifier
                    )
                }

                if (showDeleteScoreDialog) {
                    DeleteScore(
                        playerName = player.name,
                        score = score.value,
                        onDeleteScore = {
                            onEvent(GamePlayUiEvent.DeleteScore(seatIndex, round))
                        },
                        onDismissRequest = { showDeleteScoreDialog = false },
                        modifier = modifier
                    )
                }
            } else {
                if (player.scores.size == round) {
                    ScoreInputField(
                        onAddScore = { score: Int ->
                            onEvent(
                                GamePlayUiEvent.AddScore(
                                    seatIndex,
                                    score
                                )
                            )
                        },
                        isValidScore = isValidScore,
                        initialValue = "",
                        modifier = modifier.weight(1f)
                    )
                } else {
                    Spacer(modifier = modifier.weight(1f))
                }
            }
        }

    }
}

@Composable
private fun DeleteScore(
    playerName: String,
    score: Int,
    onDeleteScore: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier
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
        onDismissRequest = onDismissRequest,
        modifier = modifier
    )
}

@Composable
private fun ChangeScore(
    playerName: String,
    currentScore: Int,
    isValidScore: (String) -> Boolean,
    onChangeScore: (Int) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier
) {
    val (newScore, setNewScore) = remember {
        mutableStateOf(
            TextFieldValue(
                text = currentScore.toString(),
                selection = TextRange(currentScore.toString().length) // Set cursor to the end
            )
        )
    }
    val (warnInvalidScore, setWarnInvalidScore) = remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val hideKeyboard = { keyboardController?.hide() }
    val focusRequester = remember { FocusRequester() }

    AlertDialog(
        title = {
            Text(text = stringResource(R.string.change_score))
        },
        text = {
            OutlinedTextField(
                value = newScore,
                onValueChange = { newValue ->
                    setNewScore(newValue)
                    setWarnInvalidScore(!isValidScore(newValue.text))
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
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (isValidScore(newScore.text)) {
                            setWarnInvalidScore(false)
                            onChangeScore(newScore.text.toInt())
                            onDismissRequest()
                            hideKeyboard.invoke()
                        }
                    }
                ),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = if (newScore.text.isNegativeInt() || newScore.text == "-") Color.Red else Color.Unspecified,
                    unfocusedTextColor = if (newScore.text.isNegativeInt() || newScore.text == "-") Color.Red else Color.Unspecified
                ),
                modifier = modifier
                    .focusRequester(focusRequester)
                    .padding(vertical = 20.dp)
            )
        },
        onDismissRequest = {
            setWarnInvalidScore(false)
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (isValidScore(newScore.text)) {
                        setWarnInvalidScore(false)
                        onChangeScore(newScore.text.toInt())
                        onDismissRequest()
                    }
                },
                enabled = newScore.text != currentScore.toString() && isValidScore(newScore.text),
                modifier = modifier.padding(8.dp),
            ) {
                Text(stringResource(R.string.update))
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismissRequest() },
                modifier = modifier.padding(8.dp),
            ) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
private fun AppBarActions(
    showFindWinner: Boolean,
    determineWinner: () -> Unit,
    game: Game,
    players: List<Player>,
    onShowGameDetails: (Game) -> Unit,
    onStartNewGame: () -> Unit,
    saveFavoriteGame: (String) -> Unit,
    resetGame: () -> Unit,
    showRoundNumber: Boolean,
    onShowRoundNumber: (Boolean) -> Unit,
    onShowLeaderboard: (Game, List<Player>) -> Unit,
    modifier: Modifier
) {
    val (dropdownMenuExpanded, setDropdownMenuExpanded) = remember { mutableStateOf(false) }
    val (showSaveFavoriteGame, setShowSaveFavoriteGame) = remember { mutableStateOf(false) }
    val (confirmResetGame, setConfirmResetGame) = remember { mutableStateOf(false) }
    val (confirmStartNewGame, setConfirmStartNewGame) = remember { mutableStateOf(false) }

    Row {
        IconButton(onClick = { onShowLeaderboard(game, players) }) {
            Icon(imageVector = Icons.Outlined.Leaderboard, contentDescription = "Leaderboard")
        }
        IconButton(onClick = { setDropdownMenuExpanded(true) }) {
            Icon(
                imageVector = Icons.Filled.Menu,
                contentDescription = stringResource(R.string.menu)
            )
        }
    }
    DropdownMenu(
        expanded = dropdownMenuExpanded,
        onDismissRequest = { setDropdownMenuExpanded(false) }
    ) {
        if (showFindWinner) {
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
        HorizontalDivider()
        DropdownMenuItem(
            text = {
                Text(
                    text = if (showRoundNumber) {
                        stringResource(R.string.hide_round)
                    } else {
                        stringResource(R.string.show_round)
                    },
                    fontSize = 20.sp
                )
            },
            onClick = {
                setDropdownMenuExpanded(false)
                onShowRoundNumber(!showRoundNumber)
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
            },
            modifier = modifier
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
            },
            modifier = modifier
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
    val players = listOf(
        Player(name = "Sheldon", scores = listOf(Score(90), Score(25))),
        Player(name = "Leonard", scores = listOf(Score(-20), Score(40), Score(235)))
    )
    val uiState = GamePlayUiState(
        game = game,
        players = players,
        showRoundNumber = true
    )
    GamePlayScreenContent(
        uiState = uiState,
        onEvent = { _ -> },
        isValidScore = { _ -> false },
        modifier = Modifier
    )
}