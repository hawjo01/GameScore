package net.hawkins.gamescore.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import net.hawkins.gamescore.R
import net.hawkins.gamescore.data.FavoriteGames
import net.hawkins.gamescore.data.FavoritePlayers
import net.hawkins.gamescore.game.GameType
import net.hawkins.gamescore.ui.component.ConfirmAction

enum class GameSetupType(val labelId: Int) {
    FAVORITE(R.string.favorite),
    MANUAL(R.string.manual)
}

@Composable
fun GameSetupScreen(
    gameViewModel: GameViewModel,
    favoritePlayers: FavoritePlayers,
    favoriteGames: FavoriteGames,
    onNextButtonClicked: (Int, List<String>) -> Unit
) {
    LaunchedEffect(Unit) {
        gameViewModel.updateAppBarActions { /* No AppBarActions */ }
    }

    var selectedSetupType by remember {
        mutableStateOf(
            if (favoriteGames.games
                    .isNotEmpty()
            ) GameSetupType.FAVORITE else GameSetupType.MANUAL
        )
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            val gameSetupTypes = listOf(GameSetupType.FAVORITE, GameSetupType.MANUAL)

            SingleChoiceSegmentedButtonRow {
                gameSetupTypes.forEachIndexed { index, gameSetupType ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = gameSetupTypes.size
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
            modifier = Modifier.fillMaxWidth()
        ) {
            HorizontalDivider(
                modifier = Modifier
                    .padding(all = 10.dp),
                color = Color.Gray,
                thickness = 5.dp
            )
        }

        if (selectedSetupType == GameSetupType.FAVORITE) {
            FavoriteGameSelection(favoriteGames, onNextButtonClicked)
        }

        if (selectedSetupType == GameSetupType.MANUAL) {
            ManualGameSelection(
                gameViewModel.gameTypes,
                favoritePlayers,
                onNextButtonClicked
            )
        }
    }
}

@Composable
fun ManualGameSelection(
    gameTypes: List<GameType>,
    favoritePlayers: FavoritePlayers,
    onNextButtonClicked: (Int, List<String>) -> Unit,
    modifier: Modifier = Modifier
) {

    var newPlayerName by remember { mutableStateOf("") }
    val playerNames = remember { mutableStateListOf<String>() }
    var gameTypeId by remember { mutableIntStateOf(0) }

    val keyboardController = LocalSoftwareKeyboardController.current
    val hideKeyboard = { keyboardController?.hide() }

    Column(
        modifier = modifier
            .fillMaxWidth()
    )
    {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.padding(start = 10.dp)
        ) {
            Text(
                text = stringResource(R.string.game) + ":",
                style = MaterialTheme.typography.labelMedium,
                modifier = modifier.padding(end = 20.dp)
            )
            GameTypeDropdownMenu(
                gameTypes,
                onChange = { newGameTypeId -> gameTypeId = newGameTypeId })
        }

        Row(
            modifier = modifier.padding(start = 10.dp)
        ) {
            Text(
                text = stringResource(R.string.players) + ":",
                style = MaterialTheme.typography.labelMedium,
                modifier = modifier
                    .padding(top = 10.dp, end = 20.dp)
            )

            playerNames.forEachIndexed { index, name ->
                var showConfirmRemovePlayer by remember { mutableStateOf(false) }

                Text(
                    text = name + if (index + 1 < playerNames.size) ", " else "",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = modifier
                        .padding(top = 10.dp)
                        .clickable(
                            onClick = {
                                showConfirmRemovePlayer = true
                            }
                        )
                )

                if (showConfirmRemovePlayer) {
                    ConfirmRemovePlayer(
                        onDismissRequest = { showConfirmRemovePlayer = false },
                        onConfirmation = {
                            playerNames.removeAt(index)
                            showConfirmRemovePlayer = false
                        }
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
            OutlinedButton(
                onClick = {
                    onNextButtonClicked(gameTypeId, playerNames)
                }
            ) {
                Text(stringResource(R.string.start_game))
            }
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = modifier.fillMaxWidth()
        ) {
            HorizontalDivider(
                modifier = modifier
                    .padding(all = 10.dp),
                color = Color.Gray,
                thickness = 5.dp
            )
        }
        favoritePlayers.getNames().sorted().forEach { name ->
            var showDeleteSavedPlayer by remember { mutableStateOf(false) }
            Row(
                modifier = modifier.padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OutlinedIconButton(
                    onClick = {
                        playerNames.add(name)
                    })
                {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(R.string.add)
                    )
                }
                Text(
                    text = name,
                    modifier = modifier
                        .padding(start = 10.dp)
                        .combinedClickable(
                            onLongClick = {
                                showDeleteSavedPlayer = true
                            },
                            onClick = {}
                        )
                )

                if (showDeleteSavedPlayer) {
                    ConfirmDeleteSavedPlayer(
                        name = name,
                        onDismissRequest = { showDeleteSavedPlayer = false },
                        onConfirmation = {
                            favoritePlayers.removeName(name)
                            showDeleteSavedPlayer = false
                        }
                    )
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
                    }
                ),
                trailingIcon = {
                    Row {
                        IconButton(
                            onClick = {
                                hideKeyboard.invoke()
                                playerNames.add(newPlayerName.trim())
                                newPlayerName = ""
                            },
                            enabled = newPlayerName.isNotBlank()
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = stringResource(R.string.add)
                            )
                        }
                        IconButton(
                            onClick = {
                                hideKeyboard.invoke()
                                favoritePlayers.addName(newPlayerName.trim())
                                newPlayerName = ""
                            },
                            enabled = newPlayerName.isNotBlank()
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Save,
                                contentDescription = stringResource(R.string.save)
                            )
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun FavoriteGameSelection(
    favoriteGames: FavoriteGames,
    onNextButtonClicked: (Int, List<String>) -> Unit,
    modifier: Modifier = Modifier
) {
    val favorites = favoriteGames.games
    if (favorites.isNotEmpty()) {
        val (selectedOption, onOptionSelected) = remember { mutableStateOf(favorites[0]) }
        // Note that Modifier.selectableGroup() is essential to ensure correct accessibility behavior
        Column(Modifier.selectableGroup()) {
            favorites.forEach { favorite ->
                Row(
                    modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .selectable(
                            selected = (favorite == selectedOption),
                            onClick = { onOptionSelected(favorite) },
                            role = Role.RadioButton
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (favorite == selectedOption),
                        onClick = null // null recommended for accessibility with screen readers
                    )
                    Text(
                        text = favorite.name,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = modifier.padding(start = 16.dp)
                    )
                }
            }
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = modifier.fillMaxWidth()
        ) {
            OutlinedButton(
                onClick = {
                    onNextButtonClicked(selectedOption.gameId, selectedOption.playerNames)
                }
            ) {
                Text(stringResource(R.string.start_game))
            }
        }
    }
}

@Composable
fun ConfirmRemovePlayer(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    ConfirmAction(
        title = stringResource(R.string.remove_player),
        onConfirmation = onConfirmation,
        onDismissRequest = onDismissRequest
    )
}

@Composable
fun ConfirmDeleteSavedPlayer(
    name: String,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    ConfirmAction(
        title = stringResource(R.string.delete_saved_player, name),
        onConfirmation = onConfirmation,
        onDismissRequest = onDismissRequest
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameTypeDropdownMenu(
    gameTypes: List<GameType>,
    onChange: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedGameTypeId by remember { mutableIntStateOf(gameTypes[0].getTypeId()) }
    onChange(selectedGameTypeId)
    var selectedGameNameResourceId by remember { mutableStateOf(gameTypes[0].getNameResourceId()) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        Modifier.padding(vertical = 8.dp)
    ) {
        TextField(
            value = stringResource(selectedGameNameResourceId),
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            gameTypes.forEach { gameType ->
                DropdownMenuItem(
                    text = { Text(text = stringResource(gameType.getNameResourceId())) },
                    onClick = {
                        selectedGameNameResourceId = gameType.getNameResourceId()
                        onChange(gameType.getTypeId())
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}