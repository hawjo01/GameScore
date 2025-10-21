package net.hawkins.gamescore.ui

import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import net.hawkins.gamescore.R
import net.hawkins.gamescore.Utils
import net.hawkins.gamescore.favorites.FavoriteGame
import net.hawkins.gamescore.favorites.FavoriteGames
import net.hawkins.gamescore.data.Player
import net.hawkins.gamescore.ui.component.ConfirmAction
import net.hawkins.gamescore.ui.theme.Typography

@Composable
fun GamePlayScreen(
    gameViewModel: GameViewModel,
    favoriteGames: FavoriteGames,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        gameViewModel.updateTopAppBar(
            newTitle = gameViewModel.getGameType().getName(),
            newActions = { GamePlayAppBarActions(gameViewModel, favoriteGames) }
        )
    }

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
        color = Color.Red,
        style = MaterialTheme.typography.headlineLarge
    )
}

@Composable
fun Players(gameViewModel: GameViewModel, modifier: Modifier = Modifier) {
    for ((index, player) in gameViewModel.getPlayers().withIndex()) {
        key(player.name) {
            Player(gameViewModel = gameViewModel, player = player, index = index, modifier)
        }
    }
}

@Composable
fun Player(
    gameViewModel: GameViewModel,
    player: Player,
    index: Int,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(start = if (index > 0) 10.dp else 0.dp)
    ) {
        var showNewScoreDialog by remember { mutableStateOf(false) }
        Text(
            text = player.name,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    showNewScoreDialog = true
                }
        )
        if (showNewScoreDialog) {
            NewScoreDialog(
                gameViewModel = gameViewModel,
                player = player,
                onDismissRequest = { showNewScoreDialog = false }
            )
        }
        Text(
            text = player.totalScore().toString(),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.fillMaxWidth()
        )
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth(.9f)
                .padding(4.dp),
            color = Color.Gray,
            thickness = 5.dp
        )
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
                    var showChangeScoreDialog by remember { mutableStateOf(false) }

                    Text(
                        text = score.toString().padStart(5, ' '),
                        style = MaterialTheme.typography.displayMedium,
                        color = if (Utils.isNegativeInt(score) && gameViewModel.highlightNegativeScore()) Color.Red else Color.Unspecified,
                        modifier = Modifier.clickable {
                            showChangeScoreDialog = true
                        }
                    )
                    if (showChangeScoreDialog) {
                        ChangeScore(
                            gameViewModel = gameViewModel,
                            player = player,
                            scoreIndex = index,
                            onDismissRequest = { showChangeScoreDialog = false }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun NewScoreDialog(
    gameViewModel: GameViewModel,
    player: Player,
    onDismissRequest: () -> Unit
) {
    var newScore by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val hideKeyboard = { keyboardController?.hide() }
    val focusRequester = remember { FocusRequester() }
    var warnInvalidScore by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (warnInvalidScore) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 10.dp),
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
                    modifier = Modifier
                        .padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    OutlinedTextField(
                        value = newScore,
                        onValueChange = {
                            newScore = it
                            if (gameViewModel.isValidScore(newScore)) warnInvalidScore = false
                        },
                        label = { Text(text = stringResource(R.string.score_for, player.name)) },
                        textStyle = TextStyle(textAlign = TextAlign.Center),
                        singleLine = true,
                        shape = shapes.small,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if (gameViewModel.isValidScore(newScore)) {
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
                        modifier = Modifier
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
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    value = newScore,
                    onValueChange = { newScore = it },
                    label = { Text(text = stringResource(R.string.change_score)) },
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
fun ConfirmResetGame(
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
fun GamePlayAppBarActions(gameViewModel: GameViewModel, favoriteGames: FavoriteGames) {

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
        if (!gameViewModel.hasWinningThreshold()) {
            DropdownMenuItem(
                text = { Text(text = stringResource(R.string.find_winner)) },
                onClick = {
                    gameViewModel.determineWinner()
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
                gameViewModel.resetGame()
                showResetGameDialog = false
            }
        )
    }

    if (showSaveFavoriteGame) {
        SaveFavoriteGame(
            gameViewModel,
            onDismissRequest = { showSaveFavoriteGame = false },
            onConfirmation = { name ->
                favoriteGames.add(
                    FavoriteGame(
                        name = name.trim(),
                        players = gameViewModel.getPlayers().map { player -> player.name },
                        game = gameViewModel.getGameType().getName()
                    )
                )
                showSaveFavoriteGame = false
            }
        )
    }
}

@Composable
fun SaveFavoriteGame(
    gameViewModel: GameViewModel,
    onDismissRequest: () -> Unit,
    onConfirmation: (String) -> Unit
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            var favoriteName by remember { mutableStateOf("") }
            val keyboardController = LocalSoftwareKeyboardController.current
            val hideKeyboard = { keyboardController?.hide() }

            Row(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Save Favorite Game?",
                    style = Typography.titleMedium
                )
            }

            Row(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                OutlinedTextField(
                    value = favoriteName,
                    onValueChange = { favoriteName = it },
                    label = { Text(text = stringResource(R.string.name)) },
                    singleLine = true,
                    shape = shapes.small,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Ascii,
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            hideKeyboard.invoke()
                        }
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Unspecified,
                        unfocusedTextColor = Color.Unspecified
                    ),
                    modifier = Modifier.padding(top = 10.dp)
                )
            }

            Row(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                val text = remember {
                    mutableStateOf(
                        gameViewModel.getPlayers()
                            .joinToString(separator = ", ") { player -> player.name })
                }
                OutlinedTextField(
                    value = text.value,
                    onValueChange = { newText -> text.value = newText },
                    label = { Text(text = stringResource(R.string.players)) },
                    singleLine = true,
                    readOnly = true,
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
                        focusedTextColor = Color.Unspecified,
                        unfocusedTextColor = Color.Unspecified
                    ),
                    modifier = Modifier.padding(top = 10.dp)
                )
            }

            Row(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                OutlinedTextField(
                    value = gameViewModel.getGameType().getName(),
                    onValueChange = {},
                    label = { Text(text = stringResource(R.string.game)) },
                    singleLine = true,
                    readOnly = true,
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
                        focusedTextColor = Color.Unspecified,
                        unfocusedTextColor = Color.Unspecified
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
                        onConfirmation(favoriteName)
                    },
                    enabled = favoriteName.isNotBlank(),
                    modifier = Modifier.padding(8.dp),
                ) {
                    Text(stringResource(R.string.save))
                }
            }
        }
    }
}