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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import net.hawkins.gamescore.ui.AbstractViewModel
import net.hawkins.gamescore.ui.component.ConfirmActionDialog
import net.hawkins.gamescore.ui.gameplay.GamePlayScreen
import net.hawkins.gamescore.ui.gameplay.GamePlayUiEvent
import net.hawkins.gamescore.ui.gameplay.GamePlayViewModel
import net.hawkins.gamescore.ui.gameplaysetup.GamePlaySetupScreen
import net.hawkins.gamescore.ui.gameplaysetup.GamePlaySetupViewModel
import net.hawkins.gamescore.ui.gamesetup.GameSetupScreen
import net.hawkins.gamescore.ui.gamesetup.GameSetupUiEvent
import net.hawkins.gamescore.ui.gamesetup.GameSetupViewModel
import net.hawkins.gamescore.ui.leaderboard.LeaderboardScreen
import net.hawkins.gamescore.ui.leaderboard.LeaderboardUiEvent
import net.hawkins.gamescore.ui.leaderboard.LeaderboardViewModel
import net.hawkins.gamescore.ui.managegames.GameManagementScreen
import net.hawkins.gamescore.ui.managegames.GameManagementViewModel

enum class GameScoreScreen {
    GamePlaySetup,
    GamePlay,
    GameSetup,
    ManageGames,
    Leaderboard
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
        navigationIcon = {
            topAppBar.navigationIcon?.invoke()
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
    modifier: Modifier = Modifier,
    gamePlaySetupViewModel: GamePlaySetupViewModel = viewModel(),
    gamePlayViewModel: GamePlayViewModel = viewModel(),
    gameSetupViewModel: GameSetupViewModel = viewModel(),
    gameManagementViewModel: GameManagementViewModel = viewModel(),
    leaderboardViewModel: LeaderboardViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    Scaffold(
        topBar = {
            GameScoreAppBar(
                viewModel = gamePlayViewModel,
            )
        }
    ) { innerPadding ->
        val (showGameInProgressDialog, setShowGameInProgressDialog) = rememberSaveable {
            mutableStateOf(
                gamePlayViewModel.isGameInProgress()
            )
        }
        if (showGameInProgressDialog) {
            ResumeGameInProgress(
                onResumeGame = {
                    gamePlayViewModel.loadInProgressGame()
                    navController.navigate(GameScoreScreen.GamePlay.name)
                    setShowGameInProgressDialog(false)
                },
                onDismissRequest = { setShowGameInProgressDialog(false) },
                modifier = modifier
            )
        }
        NavHost(
            navController = navController,
            startDestination = GameScoreScreen.GamePlaySetup.name,
            modifier = Modifier.padding(innerPadding)
        ) {

            composable(route = GameScoreScreen.GamePlaySetup.name) {
                GamePlaySetupScreen(
                    viewModel = gamePlaySetupViewModel,
                    onStartGame = { game, playerNames ->
                        gamePlayViewModel.onEvent(
                            GamePlayUiEvent.StartGame(
                                game,
                                playerNames
                            )
                        )
                        navController.navigate(GameScoreScreen.GamePlay.name)
                    },
                    onManageGames = {
                        navController.navigate(GameScoreScreen.ManageGames.name)
                    }
                )
            }

            composable(route = GameScoreScreen.GamePlay.name) {
                BackHandler(true) {
                    // Prevent accidental erasure of game data
                }
                GamePlayScreen(
                    viewModel = gamePlayViewModel,
                    onStartNewGame = {
                        navController.navigate(GameScoreScreen.GamePlaySetup.name)
                    },
                    onShowGameDetails = { game ->
                        gameSetupViewModel.onEvent(GameSetupUiEvent.SetGame(game))
                        navController.navigate(GameScoreScreen.GameSetup.name)
                    },
                    onShowLeaderboard = { game, players ->
                        leaderboardViewModel.onEvent(
                            LeaderboardUiEvent.RefreshLeaderboard(
                                game,
                                players
                            )
                        )
                        navController.navigate(GameScoreScreen.Leaderboard.name)
                    }
                )
            }

            composable(route = GameScoreScreen.GameSetup.name) {
                GameSetupScreen(
                    viewModel = gameSetupViewModel,
                    onCancel = {
                        navController.popBackStack()
                    },
                    onSaveGame = {
                        if (gameSetupViewModel.saveGame()) {
                            navController.popBackStack()
                        }
                    }
                )
            }

            composable(route = GameScoreScreen.ManageGames.name) {
                GameManagementScreen(
                    viewModel = gameManagementViewModel,
                    onBack = {
                        navController.popBackStack()
                    },
                    onCreateNewGame = {
                        gameSetupViewModel.onEvent(GameSetupUiEvent.NewGame)
                        navController.navigate(GameScoreScreen.GameSetup.name)
                    },
                    onViewGame = { game ->
                        gameSetupViewModel.onEvent(GameSetupUiEvent.SetGame(game))
                        navController.navigate(GameScoreScreen.GameSetup.name)
                    }
                )
            }

            composable(route = GameScoreScreen.Leaderboard.name) {
                LeaderboardScreen(
                    viewModel = leaderboardViewModel,
                    onBack = {
                        navController.popBackStack()
                    },
                    modifier = modifier
                )
            }
        }
    }
}

@Composable
private fun ResumeGameInProgress(
    onResumeGame: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier
) {
    ConfirmActionDialog(
        title = stringResource(R.string.game_in_progress),
        description = stringResource(R.string.resume_the_game_in_progress),
        confirmLabel = stringResource(R.string.yes),
        dismissLabel = stringResource(R.string.no),
        onConfirmation = onResumeGame,
        onDismissRequest = onDismissRequest,
        modifier = modifier
    )
}