package net.hawkins.gamescore.ui.managegames

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.hawkins.gamescore.R
import net.hawkins.gamescore.data.model.Game
import net.hawkins.gamescore.ui.component.BackNavigationIcon
import net.hawkins.gamescore.ui.component.ConfirmActionDialog
import net.hawkins.gamescore.ui.theme.Typography

@Composable
fun GameManagementScreen(
    viewModel: GameManagementViewModel,
    onBack: () -> Unit,
    onCreateNewGame: () -> Unit,
    onViewGame: (Game) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.updateTopAppBar(
            newNavigationIcon = {
                BackNavigationIcon(onBack = onBack)
            },
            newActions = {
                AppBarActions(
                    onCreateNewGame = onCreateNewGame
                )
            }
        )
        viewModel.onEvent(GameManagementUiEvent.RefreshState)
    }

    GameManagementScreenContent(
        uiState = uiState,
        onEvent = { event: GameManagementUiEvent -> viewModel.onEvent(event) },
        onViewGame = onViewGame,
        modifier = modifier
    )
}

@Composable
private fun GameManagementScreenContent(
    uiState: GameManagementUiState,
    onViewGame: (Game) -> Unit,
    onEvent: (GameManagementUiEvent) -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier.padding(50.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.games),
                style = Typography.titleMedium
            )
        }
        LazyColumn {
            items(items = uiState.games.sortedBy { game -> game.name }) { game ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, top = 10.dp)
                ) {
                    var showConfirmDeleteGame by remember { mutableStateOf(false) }
                    TextButton(
                        onClick = {
                            onViewGame(game)
                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color.Blue
                        ),
                    )
                    {
                        Text(
                            text = game.name,
                            style = Typography.labelMedium
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = {
                        if (game.id != null) {
                            showConfirmDeleteGame = true
                        }
                    }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = stringResource(R.string.delete),
                            tint = Color.Red
                        )
                    }

                    if (showConfirmDeleteGame) {
                        ConfirmDeleteGame(
                            game = game,
                            onDismissRequest = { showConfirmDeleteGame = false },
                            onConfirmation = { gameId ->
                                onEvent(GameManagementUiEvent.DeleteGame(gameId))
                                showConfirmDeleteGame = false
                            })
                    }
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
    ConfirmActionDialog(
        title = stringResource(R.string.delete_game),
        description = stringResource(R.string.delete_name_question, game.name),
        onConfirmation = { onConfirmation(game.id!!) },
        onDismissRequest = onDismissRequest
    )
}

@Composable
private fun AppBarActions(
    onCreateNewGame: () -> Unit,
) {
    TextButton(
        onClick = onCreateNewGame,
        colors = ButtonDefaults.textButtonColors(
            contentColor = Color.Blue
        )
    ) {
        Text(text = stringResource(R.string.new_game), fontSize = 20.sp)
    }
}

@Preview
@Composable
private fun PreviewGameManagementScreenContent() {
    GameManagementScreenContent(
        uiState = GameManagementUiState(
            games = listOf(
                Game(id = 1, name = "Five Crowns"),
                Game(id = 2, name = "2500")
            )
        ),
        onViewGame = { _ -> },
        onEvent = { _ -> },
        modifier = Modifier
    )
}