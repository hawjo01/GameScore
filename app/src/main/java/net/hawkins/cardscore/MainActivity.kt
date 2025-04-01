package net.hawkins.cardscore

import CardScore
import ResetGame
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel
import net.hawkins.cardscore.ui.GameViewModel
import net.hawkins.cardscore.ui.NewGame
import net.hawkins.cardscore.ui.StartGame
import net.hawkins.cardscore.ui.theme.CardScoreTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CardScoreTheme {
                MainScaffold(viewModel(), modifier = Modifier)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(gameViewModel: GameViewModel, modifier: Modifier = Modifier) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    actions = {
                        if (gameViewModel.playersReady()) {
                            ResetGame(gameViewModel)
                        } else {
                            StartGame(gameViewModel)
                        }
                    },
                    scrollBehavior = scrollBehavior
                )
        },
    ) { innerPadding ->
        Game(
            gameViewModel = gameViewModel,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun Game(gameViewModel: GameViewModel, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        if (!gameViewModel.playersReady()) {
            NewGame(gameViewModel)
        } else {
            CardScore(gameViewModel)
        }
    }
}
