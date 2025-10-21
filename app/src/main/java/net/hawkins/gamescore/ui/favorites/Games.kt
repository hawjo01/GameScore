package net.hawkins.gamescore.ui.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import net.hawkins.gamescore.favorites.FavoriteGames
import net.hawkins.gamescore.ui.component.ConfirmAction
import net.hawkins.gamescore.ui.theme.DeleteRed
import net.hawkins.gamescore.ui.theme.GoGreen
import net.hawkins.gamescore.ui.theme.InfoBlue
import net.hawkins.gamescore.ui.theme.Typography

@Composable
fun FavoriteGamesCard(
    favoriteGames: FavoriteGames,
    onStartButtonClick: (String, List<String>) -> Unit,
    modifier: Modifier = Modifier
) {
    Card (
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent),
        modifier = modifier.padding(all = 10.dp)) {

        val favorites = favoriteGames.getGames()
        if (favorites.isNotEmpty()) {
            var showInfoIndex by remember { mutableIntStateOf(-1) }
            var showDeleteIndex by remember { mutableIntStateOf(-1) }

            favorites.forEachIndexed { index, favorite ->
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = modifier.fillMaxWidth()
                ){
                    Column {
                        Text(
                            text = favorite.name,
                            style = Typography.labelMedium
                        )
                    }
                    Column {
                        Row {
                            IconButton(onClick = {
                                onStartButtonClick(favorite.game, favorite.players)
                            }) {
                                Icon(
                                    Icons.Filled.PlayArrow,
                                    tint = GoGreen,
                                    contentDescription = "Play ${favorite.name}"
                                )
                            }


                            IconButton(onClick = {
                                showInfoIndex = index
                            }) {
                                Icon(
                                    Icons.Filled.Info,
                                    tint = InfoBlue,
                                    contentDescription = "Info about ${favorite.name}"
                                )
                            }

                            IconButton(onClick = { showDeleteIndex = index }) {
                                Icon(
                                    Icons.Filled.Delete,
                                    tint = DeleteRed,
                                    contentDescription = "Delete ${favorite.name}"
                                )
                            }
                        }
                    }
                }
            }

            if (showInfoIndex >= 0) {
                Dialog(
                    onDismissRequest = { showInfoIndex = -1 }
                ) {
                    Card(
                        modifier = Modifier
                            .padding(16.dp),
                        shape = RoundedCornerShape(16.dp),
                    ) {
                        Column(
                            horizontalAlignment = Alignment.Start,
                            modifier = modifier.padding(start = 10.dp)
                        ) {

                            Row(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp)
                            ) {
                                Text(
                                    text = favorites[showInfoIndex].name,
                                    style = Typography.titleMedium
                                )
                            }

                            Row(modifier = modifier.padding(top = 20.dp)) {
                                Text(
                                    text = "Game",
                                    style = Typography.labelMedium
                                )
                            }
                            Row {
                                Text(text = "" + favorites[showInfoIndex].game)
                            }

                            Row(modifier = modifier.padding(top = 10.dp)) {
                                Text(
                                    text = "Players",
                                    style = Typography.labelMedium
                                )
                            }
                            Row {
                                Text(
                                    text = favorites[showInfoIndex].players.joinToString(
                                        separator = ", "
                                    ) { name -> name })
                            }


                            Row(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                TextButton(
                                    onClick = {
                                        showInfoIndex = -1
                                    },
                                    modifier = Modifier.padding(8.dp),
                                ) {
                                    Text(text = "OK")
                                }
                            }
                        }
                    }
                }
            }

            if (showDeleteIndex >= 0) {
                ConfirmAction(
                    title = "Delete Favorite Game",
                    description = "Delete '" + favorites[showDeleteIndex].name + "'",
                    onConfirmation = {
                        favoriteGames.remove(favorites[showDeleteIndex])
                        showDeleteIndex = -1
                    },
                    onDismissRequest = { showDeleteIndex = -1 },
                )
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

