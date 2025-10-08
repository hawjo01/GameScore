package net.hawkins.gamescore.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
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
import androidx.lifecycle.viewmodel.compose.viewModel
import net.hawkins.gamescore.R
import net.hawkins.gamescore.ui.theme.GameScoreTheme


@Composable
fun GameSetup(gameViewModel: GameViewModel) {
    var newPlayerName by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val hideKeyboard = { keyboardController?.hide() }
    val savedPlayerNames = gameViewModel.getSavedPlayerNames()

    Column(
        modifier = Modifier
            .fillMaxWidth()
    )
    {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 10.dp)
        ) {
            Text(
                text = stringResource(R.string.game) + ":",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(end = 20.dp)
            )
            GameTypeDropdownMenu(gameViewModel)
        }

        Row(
            modifier = Modifier.padding(start = 10.dp)
        ) {
            Text(
                text = stringResource(R.string.players) + ":",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier
                    .padding(top = 10.dp, end = 20.dp)
            )

            gameViewModel.getPlayers().forEachIndexed { index, player ->
                var showConfirmRemovePlayer by remember { mutableStateOf(false) }
                var showSavePlayerName by remember { mutableStateOf(false) }

                Text(
                    text = player.name + if (index + 1 < gameViewModel.getPlayers().size) ", " else "",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .clickable(
                            onClick = {
                                showConfirmRemovePlayer = true
                            }
                        )
                )

                if (showSavePlayerName) {
                    ConfirmSavePlayer(
                        onDismissRequest = { showSavePlayerName = false },
                        onConfirmation = {
                            gameViewModel.savePlayerName(player.name)
                            showSavePlayerName = false
                        }
                    )
                }

                if (showConfirmRemovePlayer) {
                    ConfirmRemovePlayer(
                        onDismissRequest = { showConfirmRemovePlayer = false },
                        onConfirmation = {
                            gameViewModel.removePlayer(index)
                            showConfirmRemovePlayer = false
                        }
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
        savedPlayerNames.sorted().forEach { name ->
            var showDeleteSavedPlayer by remember { mutableStateOf(false) }
            Row(
                modifier = Modifier.padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OutlinedIconButton(
                    onClick = {
                        gameViewModel.addPlayer(name)
                    })
                {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(R.string.add)
                    )
                }
                Text(
                    text = name,
                    modifier = Modifier
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
                            gameViewModel.removeSavedPlayerName(name)
                            showDeleteSavedPlayer = false
                        }
                    )
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(10.dp)
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
                                gameViewModel.addPlayer(newPlayerName.trim())
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
                                gameViewModel.savePlayerName(newPlayerName.trim())
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
fun ConfirmRemovePlayer(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    AlertDialog(
        title = {
            Text(text = stringResource(R.string.remove_player))
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

@Composable
fun ConfirmSavePlayer(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    AlertDialog(
        title = {
            Text(text = stringResource(R.string.save_player))
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

@Composable
fun ConfirmDeleteSavedPlayer(
    name: String,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    AlertDialog(
        title = {
            Text(text = stringResource(R.string.delete_saved_player, name))
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameTypeDropdownMenu(
    gameViewModel: GameViewModel
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        Modifier.padding(vertical = 8.dp)
    ) {
        TextField(
            value = stringResource(gameViewModel.getGameType().getNameId()),
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
            gameViewModel.getGameTypes().forEach { gameType ->
                DropdownMenuItem(
                    text = { Text(text = stringResource(gameType.getNameId())) },
                    onClick = {
                        gameViewModel.setGameType(gameType)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}

@Composable
fun StartGame(gameViewModel: GameViewModel) {
    TextButton(
        onClick = {
            gameViewModel.startGame()
        },
    ) {
        Text(text = stringResource(R.string.start_game))
    }
}

@Preview(showBackground = true)
@Composable
fun NewGamePreview() {
    GameScoreTheme {
        GameSetup(viewModel())
    }
}