package net.hawkins.gamescore

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import net.hawkins.gamescore.game.GamePlay
import net.hawkins.gamescore.ui.AbstractViewModel
import net.hawkins.gamescore.ui.gameplay.GamePlayScreen
import net.hawkins.gamescore.ui.gameplay.GamePlayViewModel
import net.hawkins.gamescore.ui.gameplaysetup.GamePlaySetupScreen
import net.hawkins.gamescore.ui.gameplaysetup.GamePlaySetupViewModel
import net.hawkins.gamescore.ui.gamesetup.GameSetupScreen
import net.hawkins.gamescore.ui.gamesetup.GameSetupUiState
import net.hawkins.gamescore.ui.gamesetup.GameSetupViewModel

enum class GameScoreScreen() {
    GamePlaySetup,
    GamePlay,
    GameSetup
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GameScoreAppBar(
    viewModel: AbstractViewModel,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val topAppBar by viewModel.topAppBar

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = topAppBar.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        actions = {
            topAppBar.actions?.invoke(this)
        },
        scrollBehavior = scrollBehavior,
        modifier = modifier,
    )
}

@Composable
fun GameScoreScreen(
    gamePlaySetupViewModel: GamePlaySetupViewModel = viewModel(),
    gamePlayViewModel: GamePlayViewModel = viewModel(),
    gameSetupViewModel: GameSetupViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    Scaffold(
        topBar = {
            GameScoreAppBar(
                viewModel = gamePlayViewModel,
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = GameScoreScreen.GamePlaySetup.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = GameScoreScreen.GamePlaySetup.name) {
                GamePlaySetupScreen(
                    viewModel = gamePlaySetupViewModel,
                    onStartGame = { game, playerNames ->
                        gamePlayViewModel.setGame(
                            GamePlay(
                                game,
                                playerNames
                            )
                        )
                        navController.navigate(GameScoreScreen.GamePlay.name)
                    },
                    onNewGameSetup = {
                        gameSetupViewModel.resetGame()
                        navController.navigate(GameScoreScreen.GameSetup.name)
                    }
                )
            }

            composable(route = GameScoreScreen.GamePlay.name) {
                BackHandler(true) {
                    // Prevent accidental erasure of game data
                }
                GamePlayScreen(
                    viewModel = gamePlayViewModel,
                    onShowGameDetails = { game ->
                        gameSetupViewModel.setGame(game)
                        gameSetupViewModel.setMode(GameSetupUiState.Mode.VIEW)
                        navController.navigate(GameScoreScreen.GameSetup.name)
                    }
                )
            }

            composable(route = GameScoreScreen.GameSetup.name) {
                GameSetupScreen(
                    viewModel = gameSetupViewModel,
                    onCancel = {
                        navController.popBackStack()
                    },
                    onModifyGame = {
                        val game = gameSetupViewModel.getGame()
                        gamePlayViewModel.updateGame(game)
                        navController.popBackStack()
                    },
                    onSaveNewGame = {
                        val game = gameSetupViewModel.saveGame()
                        gamePlaySetupViewModel.reloadGames()
                        gamePlaySetupViewModel.setGame(game)
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}