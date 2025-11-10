package net.hawkins.gamescore.ui.favorites

import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import net.hawkins.gamescore.R
import net.hawkins.gamescore.data.model.FavoriteGame
import net.hawkins.gamescore.game.GamePlay
import net.hawkins.gamescore.ui.component.ConfirmAction
import net.hawkins.gamescore.ui.theme.DeleteRed
import net.hawkins.gamescore.ui.theme.SkyBlue
import net.hawkins.gamescore.ui.theme.Typography

@Composable
fun FavoriteGamesCard(
    favoriteGames: List<FavoriteGame>,
    onFavoriteSelected: (FavoriteGame) -> Unit,
    onDeleteFavoriteGame: (FavoriteGame) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        modifier = modifier.padding(all = 10.dp)
    ) {
        if (favoriteGames.isNotEmpty()) {
            var showDeleteIndex by remember { mutableIntStateOf(-1) }

            favoriteGames.forEachIndexed { index, favorite ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(
                            text = favorite.name,
                            modifier = modifier
                                .padding(horizontal = 20.dp, vertical = 10.dp)
                                .clickable(
                                    onClick = { onFavoriteSelected(favorite) }
                                ),
                            style = MaterialTheme.typography.labelMedium.plus(
                                TextStyle(
                                    color = SkyBlue,
                                    textDecoration = TextDecoration.Underline
                                )
                            )
                        )
                    }
                    Column {
                        IconButton(onClick = { showDeleteIndex = index }) {
                            Icon(
                                Icons.Filled.Delete,
                                tint = DeleteRed,
                                contentDescription = "Delete ${favorite.name}"
                            )
                        }
                    }
                }
                if (showDeleteIndex >= 0) {
                    ConfirmAction(
                        title = "Delete Favorite Game",
                        description = "Delete '" + favoriteGames[showDeleteIndex].name + "'",
                        onConfirmation = {
                            onDeleteFavoriteGame(favoriteGames[showDeleteIndex])
                            showDeleteIndex = -1
                        },
                        onDismissRequest = { showDeleteIndex = -1 },
                    )
                }
            }
        } else {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = modifier.fillMaxWidth()
            ) {
                Text(
                    text = "No Favorite Games",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
fun SaveFavoriteGame(
    gamePlay: GamePlay,
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
                        gamePlay.players
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
                    value = gamePlay.getGameName(),
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