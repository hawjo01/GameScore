package net.hawkins.gamescore.ui.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import net.hawkins.gamescore.R
import net.hawkins.gamescore.data.model.FavoriteGame
import net.hawkins.gamescore.data.model.Game
import net.hawkins.gamescore.ui.component.ConfirmActionDialog
import net.hawkins.gamescore.ui.gameplay.Player
import net.hawkins.gamescore.ui.gameplaysetup.GamePlaySetupUiEvent
import net.hawkins.gamescore.ui.theme.DeleteRed
import net.hawkins.gamescore.ui.theme.Typography

@Composable
fun FavoriteGamesCard(
    favoriteGames: List<FavoriteGame>,
    onEvent: (GamePlaySetupUiEvent) -> Unit,
    onFavoriteSelected: (FavoriteGame) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        modifier = modifier.padding(all = 10.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        ) {
            Text(text = stringResource(R.string.favorite_games))
        }
        if (favoriteGames.isNotEmpty()) {
            favoriteGames.sortedBy { game -> game.name }.forEach { favorite ->
                var showDeleteFavoriteGame by remember { mutableStateOf(false) }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = modifier.fillMaxWidth()
                ) {
                    Column {
                        TextButton(
                            onClick = { onFavoriteSelected(favorite) },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = Color.Blue
                            ),
                        ) {
                            Text(
                                text = favorite.name,
                                modifier = modifier
                                    .padding(horizontal = 20.dp, vertical = 10.dp),
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                    Column {
                        IconButton(onClick = { showDeleteFavoriteGame = true }) {
                            Icon(
                                Icons.Filled.Delete,
                                tint = DeleteRed,
                                contentDescription = stringResource(
                                    R.string.delete_name_question,
                                    favorite.name
                                )
                            )
                        }
                    }
                }
                if (showDeleteFavoriteGame) {
                    ConfirmActionDialog(
                        title = stringResource(R.string.delete_favorite_game),
                        description = stringResource(R.string.delete_name_question, favorite.name),
                        onConfirmation = {
                            onEvent(GamePlaySetupUiEvent.DeleteFavoriteGame(favorite.id!!))
                            showDeleteFavoriteGame = false
                        },
                        onDismissRequest = { showDeleteFavoriteGame = false },
                    )
                }
            }
        } else {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.no_favorite_games),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
fun SaveFavoriteGame(
    game: Game,
    players: List<Player>,
    onDismissRequest: () -> Unit,
    onConfirmation: (String) -> Unit
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            var favoriteName by remember { mutableStateOf(buildFavoriteName(game, players)) }
            val keyboardController = LocalSoftwareKeyboardController.current
            val hideKeyboard = { keyboardController?.hide() }

            Row(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.save_favorite_game),
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
                    isError = favoriteName.isBlank(),
                    supportingText = {
                        if (favoriteName.isBlank()) {
                            Text(
                                text = stringResource(R.string.required),
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
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
                        players
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
                    value = game.name,
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

internal fun buildFavoriteName(
    game: Game,
    players: List<Player>
): String {
    val sb = StringBuilder()
    sb.append(game.name)
    sb.append(" - ")
    sb.append(players.joinToString { player -> player.name })
    return sb.toString()
}