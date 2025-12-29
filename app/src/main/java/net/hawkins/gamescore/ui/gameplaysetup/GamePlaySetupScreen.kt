package net.hawkins.gamescore.ui.gameplaysetup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import net.hawkins.gamescore.R
import net.hawkins.gamescore.data.model.Game
import net.hawkins.gamescore.ui.component.ConfirmAction
import net.hawkins.gamescore.ui.favorites.FavoriteGamesCard
import net.hawkins.gamescore.ui.theme.GoGreen
import net.hawkins.gamescore.ui.theme.SkyBlue

enum class GameSetupType(val labelId: Int) {
    FAVORITE(R.string.favorite), MANUAL(R.string.manual)
}

@Composable
fun GamePlaySetupScreen(
    viewModel: GamePlaySetupViewModel,
    onStartGame: (Game, List<String>) -> Unit,
    onNewGameSetup: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.updateTopAppBar {
            AppBarActions(
                uiState = uiState,
                onDeleteGame = { gameId -> viewModel.onEvent(GamePlaySetupUiEvent.DeleteGame(gameId)) },
                modifier = modifier
            )
        }
    }

    GamePlaySetupScreenContent(
        uiState = uiState,
        onEvent = { event: GamePlaySetupUiEvent -> viewModel.onEvent(event) },
        onStartGame = onStartGame,
        onNewGameSetup = onNewGameSetup,
        modifier = modifier
    )
}

@Composable
private fun GamePlaySetupScreenContent(
    uiState: GamePlaySetupUiState,
    onEvent: (GamePlaySetupUiEvent) -> Unit,
    onStartGame: (Game, List<String>) -> Unit,
    onNewGameSetup: () -> Unit,
    modifier: Modifier
) {
    Column {
        GameCard(
            games = uiState.savedGames,
            game = uiState.selectedGame,
            onEvent = onEvent,
            playerNames = uiState.playerNames,
            onNewGameSetup = onNewGameSetup,
            onStartGame = onStartGame,
            modifier = modifier
        )

        var selectedSetupType by remember {
            mutableStateOf(
                if (uiState.favoriteGames.isNotEmpty()) GameSetupType.FAVORITE else GameSetupType.MANUAL
            )
        }

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()
            ) {
                val gameSetupTypes = listOf(GameSetupType.FAVORITE, GameSetupType.MANUAL)

                SingleChoiceSegmentedButtonRow {
                    gameSetupTypes.forEachIndexed { index, gameSetupType ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index, count = gameSetupTypes.size
                            ),
                            onClick = { selectedSetupType = gameSetupType },
                            selected = gameSetupType == selectedSetupType,
                            label = { Text(stringResource(gameSetupType.labelId)) },
                        )
                    }
                }
            }

            if (selectedSetupType == GameSetupType.FAVORITE) {
                FavoriteGamesCard(
                    favoriteGames = uiState.favoriteGames,
                    onEvent = onEvent,
                    onFavoriteSelected = { favoriteGame ->
                        // TODO: Fix this once the entire game is stored in the favorite
                        onEvent(GamePlaySetupUiEvent.SetGame(favoriteGame.game))
                        onEvent(GamePlaySetupUiEvent.SetPlayers(favoriteGame.players))
                    })
            }

            if (selectedSetupType == GameSetupType.MANUAL) {
                ManualGameSelection(
                    favoritePlayers = uiState.favoritePlayerNames,
                    onEvent = onEvent,
                )
            }
        }
    }
}

@Composable
private fun GameCard(
    games: List<Game>,
    game: Game,
    onEvent: (GamePlaySetupUiEvent) -> Unit,
    onNewGameSetup: () -> Unit,
    playerNames: List<String>,
    onStartGame: (Game, List<String>) -> Unit,
    modifier: Modifier
) {
    var showGameSelectionDialog by remember { mutableStateOf(false) }
    Card(
        modifier = modifier.padding(all = 20.dp)
    ) {
        Column(
            modifier = modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier.padding(start = 10.dp, top = 10.dp)
            ) {
                Text(
                    text = stringResource(R.string.game) + ":",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = modifier.padding(end = 30.dp)
                )
                Text(
                    text = if (game.name != "") game.name else stringResource(R.string.select_game),
                    style = MaterialTheme.typography.bodyMedium.plus(
                        TextStyle(
                            color = SkyBlue, textDecoration = TextDecoration.Underline
                        )
                    ),
                    modifier = modifier.clickable(
                        onClick = { showGameSelectionDialog = true })
                )
            }

            if (showGameSelectionDialog) {
                GameSelectionDialog(
                    games = games,
                    onNewGameSetup = onNewGameSetup,
                    onClickAction = { game ->
                        onEvent(GamePlaySetupUiEvent.SetGame(game))
                        showGameSelectionDialog = false
                    }, onDismissRequest = { showGameSelectionDialog = false }, modifier = modifier
                )
            }

            Row(
                modifier = modifier.padding(start = 10.dp)
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.players) + ":",
                        style = MaterialTheme.typography.labelMedium,
                        modifier = modifier.padding(top = 10.dp, end = 10.dp)
                    )
                }
                Column {
                    FlowRow {
                        playerNames.forEachIndexed { index, name ->
                            var showConfirmRemovePlayer by remember { mutableStateOf(false) }

                            Text(
                                text = name + if (index + 1 < playerNames.size) ", " else "",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = modifier
                                    .padding(top = 10.dp)
                                    .clickable(
                                        onClick = {
                                            showConfirmRemovePlayer = true
                                        })
                            )

                            if (showConfirmRemovePlayer) {
                                ConfirmRemovePlayer(
                                    onDismissRequest = { showConfirmRemovePlayer = false },
                                    onConfirmation = {
                                        onEvent(GamePlaySetupUiEvent.RemovePlayer(index))
                                        showConfirmRemovePlayer = false
                                    })
                            }
                        }
                    }
                }
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            ) {
                IconButton(
                    onClick = {
                        onStartGame(game, playerNames)
                    },
                    enabled = game.name.isNotEmpty() && playerNames.isNotEmpty()
                ) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = stringResource(R.string.start_game),
                        tint = if (game.name.isNotEmpty() && playerNames.isNotEmpty()) {
                            GoGreen
                        } else {
                            Color.Transparent
                        },
                        modifier = modifier.size(60.dp)

                    )
                }
            }
        }
    }
}

@Composable
private fun ManualGameSelection(
    favoritePlayers: List<String>,
    onEvent: (GamePlaySetupUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    PlayerSelection(
        favoritePlayers = favoritePlayers,
        onEvent = onEvent,
        modifier = modifier
    )
}

@Composable
private fun PlayerSelection(
    favoritePlayers: List<String>,
    onEvent: (GamePlaySetupUiEvent) -> Unit,
    modifier: Modifier
) {
    var newPlayerName by remember { mutableStateOf("") }

    val keyboardController = LocalSoftwareKeyboardController.current
    val hideKeyboard = { keyboardController?.hide() }

    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
    ) {
        HorizontalDivider(
            modifier = modifier.padding(all = 20.dp), color = Color.Gray, thickness = 5.dp
        )
    }
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
    ) {
        Text(text = stringResource(R.string.choose_players))
    }
    Row {
        LazyColumn {
            items(favoritePlayers.sorted(), key = { it }) { name ->
                var showDeleteSavedPlayer by remember { mutableStateOf(false) }
                Row(
                    modifier = modifier.padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    OutlinedIconButton(
                        onClick = {
                            onEvent(GamePlaySetupUiEvent.AddPlayer(name))
                        }) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = stringResource(R.string.add)
                        )
                    }
                    Text(
                        text = name,
                        modifier = modifier
                            .padding(start = 10.dp)
                            .combinedClickable(onLongClick = {
                                showDeleteSavedPlayer = true
                            }, onClick = {})
                    )

                    if (showDeleteSavedPlayer) {
                        ConfirmDeleteSavedPlayer(
                            name = name,
                            onDismissRequest = { showDeleteSavedPlayer = false },
                            onConfirmation = {
                                onEvent(GamePlaySetupUiEvent.DeleteFavoritePlayer(name))
                                showDeleteSavedPlayer = false
                            })
                    }
                }
            }
            item {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier.padding(10.dp)
                ) {
                    OutlinedTextField(
                        value = newPlayerName,
                        onValueChange = { newPlayerName = it },
                        label = { Text(text = stringResource(R.string.player_name)) },
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
                            }),
                        trailingIcon = {
                            Row {
                                IconButton(
                                    onClick = {
                                        hideKeyboard.invoke()
                                        onEvent(GamePlaySetupUiEvent.AddPlayer(newPlayerName.trim()))
                                        newPlayerName = ""
                                    }, enabled = newPlayerName.isNotBlank()
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Add,
                                        contentDescription = stringResource(R.string.add)
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        hideKeyboard.invoke()
                                        onEvent(GamePlaySetupUiEvent.AddFavoritePlayer(newPlayerName.trim()))
                                        newPlayerName = ""
                                    }, enabled = newPlayerName.isNotBlank()
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Save,
                                        contentDescription = stringResource(R.string.save)
                                    )
                                }
                            }
                        })
                }
            }
        }
    }


}

@Composable
private fun ConfirmRemovePlayer(
    onDismissRequest: () -> Unit, onConfirmation: () -> Unit
) {
    ConfirmAction(
        title = stringResource(R.string.remove_player),
        onConfirmation = onConfirmation,
        onDismissRequest = onDismissRequest
    )
}

@Composable
private fun ConfirmDeleteSavedPlayer(
    name: String, onDismissRequest: () -> Unit, onConfirmation: () -> Unit
) {
    ConfirmAction(
        title = stringResource(R.string.delete_saved_player, name),
        onConfirmation = onConfirmation,
        onDismissRequest = onDismissRequest
    )
}

@Composable
private fun GameSelectionDialog(
    games: List<Game>,
    onNewGameSetup: () -> Unit,
    onClickAction: (Game) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = modifier.padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = modifier.padding(top = 10.dp)
                ) {
                    Text(
                        text = stringResource(R.string.select_game),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "New Game",
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                        .clickable(
                            onClick = onNewGameSetup
                        ),
                    style = MaterialTheme.typography.labelMedium.plus(
                        TextStyle(
                            color = SkyBlue, textDecoration = TextDecoration.Underline
                        )
                    )
                )
                games.sortedBy { game -> game.name }.forEach { game ->
                    Text(
                        text = game.name,
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 10.dp)
                            .clickable(
                                onClick = { onClickAction(game) }),
                        style = MaterialTheme.typography.labelMedium.plus(
                            TextStyle(
                                color = SkyBlue, textDecoration = TextDecoration.Underline
                            )
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun ConfirmDeleteGame(
    game: Game,
    onDismissRequest: () -> Unit,
    onConfirmation: (Int) -> Unit
) {
    ConfirmAction(
        title = "Delete Game",
        description = "Delete '" + game.name + "'?",
        onConfirmation = { onConfirmation(game.id!!) },
        onDismissRequest = onDismissRequest
    )
}

@Composable
private fun ManageGamesDialog(
    onDismissRequest: () -> Unit,
    uiState: GamePlaySetupUiState,
    onDeleteGame: (Int) -> Unit,
    modifier: Modifier
) {
    Dialog(
        onDismissRequest = { onDismissRequest() },
    ) {
        Card(
            modifier = modifier.padding(50.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = modifier.fillMaxWidth()
            ) {
                Text("Manage Games")
            }
            LazyColumn {
                items(items = uiState.savedGames.sortedBy { game -> game.name }) { game ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp, top = 10.dp)
                    ) {
                        var showConfirmDeleteGame by remember { mutableStateOf(false) }
                        Text(text = game.name)
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(onClick = {
                            if (game.id != null) {
                                showConfirmDeleteGame = true
                            }
                        }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Delete",
                                tint = Color.Red
                            )
                        }

                        if (showConfirmDeleteGame) {
                            ConfirmDeleteGame(
                                game = game,
                                onDismissRequest = { showConfirmDeleteGame = false },
                                onConfirmation = { gameId ->
                                    onDeleteGame(gameId)
                                    showConfirmDeleteGame = false
                                })
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AppBarActions(
    uiState: GamePlaySetupUiState,
    onDeleteGame: (Int) -> Unit,
    modifier: Modifier
) {

    val (dropdownMenuExpanded, setDropdownMenuExpanded) = remember { mutableStateOf(false) }
    val (showManageGames, setShowManageGames) = remember { mutableStateOf(false) }

    IconButton(onClick = { setDropdownMenuExpanded(true) }) {
        Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menu")
    }
    DropdownMenu(
        expanded = dropdownMenuExpanded,
        onDismissRequest = { setDropdownMenuExpanded(false) }
    ) {
        DropdownMenuItem(
            text = {
                Text(
                    text = "Manage Games",
                    fontSize = 20.sp
                )
            },
            onClick = {
                setShowManageGames(true)
                setDropdownMenuExpanded(false)
            }
        )
    }

    if (showManageGames) {
        ManageGamesDialog(
            onDismissRequest = { setShowManageGames(false) },
            uiState = uiState,
            onDeleteGame = onDeleteGame,
            modifier = modifier
        )
    }
}


@Preview
@Composable
private fun GamePlaySetupScreenContentPreview() {
    val uiState = GamePlaySetupUiState(playerNames = listOf("Sheldon", "Leonard"))
    GamePlaySetupScreenContent(
        uiState = uiState,
        onEvent = { _ -> },
        onStartGame = { _, _ -> },
        onNewGameSetup = {},
        modifier = Modifier,
    )
}
