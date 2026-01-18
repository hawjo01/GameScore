package net.hawkins.gamescore.ui.gameplaysetup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
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
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.hawkins.gamescore.R
import net.hawkins.gamescore.data.model.Game
import net.hawkins.gamescore.ui.component.ConfirmActionDialog
import net.hawkins.gamescore.ui.favorites.FavoriteGamesCard
import net.hawkins.gamescore.ui.theme.GoGreen

enum class GameSetupType(val labelId: Int) {
    FAVORITES(R.string.favorites), PLAYERS(R.string.players)
}

@Composable
fun GamePlaySetupScreen(
    viewModel: GamePlaySetupViewModel,
    onStartGame: (Game, List<String>) -> Unit,
    onManageGames: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.updateTopAppBar {
            AppBarActions(
                onManageGames = onManageGames,
                modifier = modifier
            )
        }
        viewModel.onEvent(GamePlaySetupUiEvent.RefreshState)
    }

    GamePlaySetupScreenContent(
        uiState = uiState,
        onEvent = { event: GamePlaySetupUiEvent -> viewModel.onEvent(event) },
        onStartGame = onStartGame,
        modifier = modifier
    )
}

@Composable
private fun GamePlaySetupScreenContent(
    uiState: GamePlaySetupUiState,
    onEvent: (GamePlaySetupUiEvent) -> Unit,
    onStartGame: (Game, List<String>) -> Unit,
    modifier: Modifier
) {
    Column {
        GameCard(
            games = uiState.savedGames,
            selectedGame = uiState.selectedGame,
            onEvent = onEvent,
            playerNames = uiState.playerNames,
            onStartGame = onStartGame,
            modifier = modifier
        )

        var selectedSetupType by remember {
            mutableStateOf(
                if (uiState.favoriteGames.isNotEmpty()) GameSetupType.FAVORITES else GameSetupType.PLAYERS
            )
        }

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()
            ) {
                val gameSetupTypes = listOf(GameSetupType.FAVORITES, GameSetupType.PLAYERS)

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
            if (selectedSetupType == GameSetupType.FAVORITES) {
                FavoriteGamesCard(
                    favoriteGames = uiState.favoriteGames,
                    onEvent = onEvent,
                    onFavoriteSelected = { favoriteGame ->
                        // TODO: Fix this once the entire game is stored in the favorite
                        onEvent(GamePlaySetupUiEvent.SetGame(favoriteGame.game))
                        onEvent(GamePlaySetupUiEvent.SetPlayers(favoriteGame.players))
                    })
            }

            if (selectedSetupType == GameSetupType.PLAYERS) {
                PlayersCard(
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
    selectedGame: Game,
    onEvent: (GamePlaySetupUiEvent) -> Unit,
    playerNames: List<String>,
    onStartGame: (Game, List<String>) -> Unit,
    modifier: Modifier
) {
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
                    text = stringResource(R.string.game),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = modifier.padding(end = 30.dp)
                )
                GameDropdownMenu(
                    games = games,
                    selectedGame = selectedGame,
                    onEvent = onEvent,
                    modifier = modifier
                )
            }

            Row(
                modifier = modifier.padding(start = 10.dp)
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.players),
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
                                    },
                                    modifier = modifier
                                )
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
                        onStartGame(selectedGame, playerNames)
                    },
                    enabled = selectedGame.name.isNotEmpty() && playerNames.isNotEmpty()
                ) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = stringResource(R.string.start_game),
                        tint = if (selectedGame.name.isNotEmpty() && playerNames.isNotEmpty()) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GameDropdownMenu(
    games: List<Game>,
    selectedGame: Game,
    onEvent: (GamePlaySetupUiEvent) -> Unit,
    modifier: Modifier
) {
    val (expanded, setExpanded) = remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { setExpanded(!expanded) },
        modifier = modifier.fillMaxWidth(.9f)
    ) {
        val displayText =
            if (selectedGame.name != "") selectedGame.name else stringResource(R.string.select_game)

        TextButton(
            onClick = { setExpanded(true) },
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.textButtonColors(
                contentColor = Color.Blue
            ),
        ) {
            Text(
                text = displayText,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { setExpanded(false) },
            modifier = modifier.exposedDropdownSize()
        ) {
            games.sortedBy { game -> game.name }.forEach { game ->
                DropdownMenuItem(
                    text = { Text(text = game.name) },
                    onClick = {
                        onEvent(GamePlaySetupUiEvent.SetGame(game))
                        setExpanded(false)
                    }
                )
            }
        }
    }
}

@Composable
private fun PlayersCard(
    favoritePlayers: List<String>,
    onEvent: (GamePlaySetupUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        modifier = modifier.padding(all = 10.dp)
    ) {
        PlayerSelection(
            favoritePlayers = favoritePlayers,
            onEvent = onEvent,
            modifier = modifier
        )
    }
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
        Text(text = stringResource(R.string.players))
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
                            },
                            modifier = modifier
                        )
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
    onConfirmation: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier
) {
    ConfirmActionDialog(
        title = stringResource(R.string.remove_player),
        onConfirmation = onConfirmation,
        onDismissRequest = onDismissRequest,
        modifier = modifier
    )
}

@Composable
private fun ConfirmDeleteSavedPlayer(
    name: String,
    onConfirmation: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier
) {
    ConfirmActionDialog(
        title = stringResource(R.string.delete_saved_player, name),
        onConfirmation = onConfirmation,
        onDismissRequest = onDismissRequest,
        modifier = modifier
    )
}

@Composable
private fun AppBarActions(
    onManageGames: () -> Unit,
    modifier: Modifier
) {
    TextButton(
        onClick = onManageGames,
        colors = ButtonDefaults.textButtonColors(
            contentColor = Color.Blue
        ),
        modifier = modifier
    ) {
        Text(text = stringResource(R.string.games), fontSize = 20.sp)
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
        modifier = Modifier,
    )
}
