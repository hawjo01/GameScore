package net.hawkins.gamescore

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import net.hawkins.gamescore.data.FavoritePlayers
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
    val appBarActions by gameViewModel.appBarActions

    TopAppBar(
        title = {
            Text(
                text = "",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        actions = { appBarActions.actions?.invoke(this)
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
            composable(route = GameScoreScreen.Setup.name) {
                val context = LocalContext.current

                GameSetupScreen(
                    gameViewModel = viewModel,
                    favoritePlayers = FavoritePlayers(File(context.filesDir, "players.json")),
                    onNextButtonClicked = {
                        navController.navigate(GameScoreScreen.Play.name)
                    }
                )
            }

            composable(route = GameScoreScreen.Play.name) {
                GamePlayScreen(viewModel)
            }
        }
    }
}