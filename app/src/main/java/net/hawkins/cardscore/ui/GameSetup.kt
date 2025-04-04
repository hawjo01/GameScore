package net.hawkins.cardscore.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MenuAnchorType
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import net.hawkins.cardscore.R
import net.hawkins.cardscore.ui.theme.CardScoreTheme


@Composable
fun GameSetup(gameViewModel: GameViewModel, modifier: Modifier = Modifier) {
    var newPlayerName by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val hideKeyboard = { keyboardController?.hide() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    )
    {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(10.dp)
        ) {
            Text(
                text = stringResource(R.string.game) + ":",
                fontSize = 26.sp,
                modifier = Modifier
                    .padding(top = 10.dp, end = 20.dp)
            )
            GameTypeDropdownMenu(gameViewModel)
        }

        Row(
            modifier = Modifier.padding(10.dp)
        ) {
            Text(
                text = stringResource(R.string.players) + ":",
                fontSize = 26.sp,
                modifier = Modifier
                    .padding(top = 10.dp, end = 20.dp)
            )

            val i = gameViewModel.getPlayerNames().iterator()
            while (i.hasNext()) {
                val playerName = i.next()
                var showConfirmRemovePlayer by remember { mutableStateOf(false) }

                Text(
                    text = playerName + if (i.hasNext()) ", " else "",
                    fontSize = 26.sp,
                    modifier = Modifier
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
                            gameViewModel.removePlayerName(playerName)
                            showConfirmRemovePlayer = false
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
                label = { Text(text = stringResource(R.string.add_player)) },
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
                    IconButton(
                        onClick = {
                            hideKeyboard.invoke()
                            gameViewModel.addPlayerName(newPlayerName.trim())
                            newPlayerName = ""
                        },
                        enabled = newPlayerName.isNotBlank()
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = stringResource(R.string.add)
                        )
                    }
                }
            )
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(10.dp)
        ) {
            DropDownMenu(
                options = gameViewModel.getSavedPlayerNameLists(),
                optionSelected = {
                    gameViewModel.addPlayerNames(it)
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
        Modifier
            .padding(vertical = 8.dp)
    ) {
        TextField(
            readOnly = true,
            value = stringResource(gameViewModel.getGameType().getNameId()),
            onValueChange = {},
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            gameViewModel.getGameTypes().forEach {
                gameType ->
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownMenu(
    optionSelected: (option: List<String>) -> Unit,
    options: List<List<String>>,
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        Modifier
            .padding(vertical = 8.dp)
    ) {
        TextField(
            readOnly = true,
            value = stringResource(R.string.add_players),
            onValueChange = {},
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(text = selectionOption.joinToString(", ")) },
                    onClick = {
                        optionSelected(selectionOption)
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
    CardScoreTheme {
        GameSetup(viewModel())
    }
}