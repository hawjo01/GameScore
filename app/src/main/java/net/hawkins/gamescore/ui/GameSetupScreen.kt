package net.hawkins.gamescore.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Card
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
import androidx.compose.ui.window.Dialog
import net.hawkins.gamescore.R
import net.hawkins.gamescore.model.FavoriteGame
import net.hawkins.gamescore.game.type.GameType
import net.hawkins.gamescore.game.type.Games
import net.hawkins.gamescore.ui.component.ConfirmAction
import net.hawkins.gamescore.ui.favorites.FavoriteGamesCard
import net.hawkins.gamescore.ui.theme.GoGreen
import net.hawkins.gamescore.ui.theme.SkyBlue

@Suppress("unused")
enum class GameSetupType(val labelId: Int) {
    FAVORITE(R.string.favorite), MANUAL(R.string.manual)
}

@Composable
fun GameSetupScreen(
    viewModel: GameSetupViewModel,
    onStartGame: (String, List<String>) -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        viewModel.updateTopAppBar { /* No AppBarActions */ }
    }

    val uiState by viewModel.uiState.collectAsState()
    GameSetupScreenContent(
        uiState = uiState,
        onStartGame = onStartGame,
        onSetGame = { gameName -> viewModel.setGameName(gameName) },
        onAddPlayer = { playerName -> viewModel.addPlayer(playerName) },
        onRemovePlayer = { index -> viewModel.removePlayer(index) },
        onSetPlayers = { playerNames -> viewModel.setPlayers(playerNames) },
        saveFavoritePlayer = { playerName -> viewModel.addFavoritePlayer(playerName) },
        deleteFavoritePlayer = { playerName -> viewModel.deleteFavoritePlayer(playerName) },
        deleteFavoriteGame = { favoriteGame -> viewModel.deleteFavoriteGame(favoriteGame) },
        modifier = modifier
    )
}

@Composable
private fun GameSetupScreenContent(
    uiState: GameSetupUiState,
    onStartGame: (String, List<String>) -> Unit,
    onSetGame: (String) -> Unit,
    onAddPlayer: (String) -> Unit,
    onRemovePlayer: (Int) -> Unit,
    onSetPlayers: (List<String>) -> Unit,
    saveFavoritePlayer: (String) -> Unit,
    deleteFavoritePlayer: (String) -> Unit,
    deleteFavoriteGame: (FavoriteGame) -> Unit,
    modifier: Modifier
) {
    Column {
        GameCard(
            gameName = uiState.gameName,
            playerNames = uiState.playerNames,
            onStartGame = onStartGame,
            onRemovePlayer = onRemovePlayer,
            onChangeGame = onSetGame,
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
                    onDeleteFavoriteGame = deleteFavoriteGame,
                    onFavoriteSelected = { favoriteGame ->
                        onSetGame(favoriteGame.game)
                        onSetPlayers(favoriteGame.players)
                    })
            }

            if (selectedSetupType == GameSetupType.MANUAL) {
                ManualGameSelection(
                    favoritePlayers = uiState.favoritePlayerNames,
                    addPlayer = onAddPlayer,
                    saveFavoritePlayer = saveFavoritePlayer,
                    deleteFavoritePlayer = deleteFavoritePlayer
                )
            }
        }
    }
}

@Composable
private fun GameCard(
    gameName: String,
    playerNames: List<String>,
    onStartGame: (String, List<String>) -> Unit,
    onRemovePlayer: (Int) -> Unit,
    onChangeGame: (String) -> Unit,
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
                    text = if (gameName != "") gameName else stringResource(R.string.select_game),
                    style = MaterialTheme.typography.bodyMedium.plus(
                        TextStyle(
                            color = SkyBlue, textDecoration = TextDecoration.Underline
                        )
                    ),
                    modifier = modifier.clickable(
                        onClick = { showGameSelectionDialog = true }))
            }

            if (showGameSelectionDialog) {
                GameSelectionDialog(
                    onClickAction = { gameType ->
                        onChangeGame(gameType.getName())
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
                                        }))

                            if (showConfirmRemovePlayer) {
                                ConfirmRemovePlayer(
                                    onDismissRequest = { showConfirmRemovePlayer = false },
                                    onConfirmation = {
                                        onRemovePlayer(index)
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
                        onStartGame(gameName, playerNames)
                    },
                    enabled = gameName.isNotEmpty() && playerNames.isNotEmpty()
                ) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = stringResource(R.string.start_game),
                        tint = if (gameName.isNotEmpty() && playerNames.isNotEmpty()) {GoGreen} else {Color.Transparent},
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
    addPlayer: (String) -> Unit,
    saveFavoritePlayer: (String) -> Unit,
    deleteFavoritePlayer: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    PlayerSelection(
        favoritePlayers = favoritePlayers,
        addPlayer = addPlayer,
        saveFavoritePlayer = saveFavoritePlayer,
        deleteFavoritePlayer = deleteFavoritePlayer,
        modifier = modifier
    )
}

@Composable
private fun PlayerSelection(
    favoritePlayers: List<String>,
    addPlayer: (String) -> Unit,
    saveFavoritePlayer: (String) -> Unit,
    deleteFavoritePlayer: (String) -> Unit,
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
    favoritePlayers.sorted().forEach { name ->
        var showDeleteSavedPlayer by remember { mutableStateOf(false) }
        Row(
            modifier = modifier.padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedIconButton(
                onClick = {
                    addPlayer(name)
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
                    }, onClick = {}))

            if (showDeleteSavedPlayer) {
                ConfirmDeleteSavedPlayer(
                    name = name,
                    onDismissRequest = { showDeleteSavedPlayer = false },
                    onConfirmation = {
                        deleteFavoritePlayer(name)
                        showDeleteSavedPlayer = false
                    })
            }
        }
    }
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
                            addPlayer(newPlayerName.trim())
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
                            saveFavoritePlayer(newPlayerName.trim())
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
    onClickAction: (GameType) -> Unit, onDismissRequest: () -> Unit, modifier: Modifier
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
                Games.TYPES.forEach { gameType ->
                    Text(
                        text = gameType.getName(),
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 10.dp)
                            .clickable(
                                onClick = { onClickAction(gameType) }),
                        style = MaterialTheme.typography.labelMedium.plus(
                            TextStyle(
                                color = SkyBlue, textDecoration = TextDecoration.Underline
                            )
                        ))
                }
            }
        }
    }
}

@Preview
@Composable
private fun GameSetupScreenContentPreview() {
    val uiState = GameSetupUiState("", listOf("Sheldon", "Leonard"))
    GameSetupScreenContent(
        uiState = uiState,
        onStartGame = { game, players -> },
        onSetPlayers = { players -> },
        onSetGame = { game -> },
        onAddPlayer = { player -> },
        onRemovePlayer = { index -> },
        modifier = Modifier,
        saveFavoritePlayer = { playerName -> },
        deleteFavoritePlayer = { playerName -> },
        deleteFavoriteGame = { game -> })
}