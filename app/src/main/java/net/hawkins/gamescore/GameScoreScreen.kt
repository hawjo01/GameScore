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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import net.hawkins.gamescore.favorites.FavoriteGames
import net.hawkins.gamescore.favorites.FavoritePlayers
import net.hawkins.gamescore.ui.GamePlayScreen
import net.hawkins.gamescore.ui.GameSetupScreen
import net.hawkins.gamescore.ui.GameViewModel
import java.io.File

enum class GameScoreScreen() {
    Setup,
    Play
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScoreAppBar(
    gameViewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val topAppBar by gameViewModel.topAppBar

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
fun GameScoreApp(
    viewModel: GameViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            GameScoreAppBar(
                gameViewModel = viewModel,
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = GameScoreScreen.Setup.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            val favoriteGames = FavoriteGames(File(context.filesDir, "favorite-games.json"))

            composable(route = GameScoreScreen.Setup.name) {

                GameSetupScreen(
                    gameViewModel = viewModel,
                    favoritePlayers = FavoritePlayers(
                        File(
                            context.filesDir,
                            "favorite-players.json"
                        )
                    ),
                    favoriteGames = favoriteGames,
                    onNextButtonClicked = { game, players ->
                        viewModel.setGameType(game)
                        viewModel.setPlayers(players)
                        navController.navigate(GameScoreScreen.Play.name)
                    }
                )
            }

            composable(route = GameScoreScreen.Play.name) {
                BackHandler(true) {
                    // Prevent accidental erasure of game data
                }
                GamePlayScreen(
                    gameViewModel = viewModel,
                    favoriteGames = favoriteGames
                )
            }
        }
    }
}