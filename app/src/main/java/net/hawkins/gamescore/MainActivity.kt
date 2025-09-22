package net.hawkins.gamescore

import android.content.Context
import net.hawkins.gamescore.ui.GamePlay
import net.hawkins.gamescore.ui.ResetGame
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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import net.hawkins.gamescore.ui.GameViewModel
import net.hawkins.gamescore.ui.GameSetup
import net.hawkins.gamescore.ui.StartGame
import net.hawkins.gamescore.ui.theme.GameScoreTheme
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.collections.mutableListOf

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GameScoreTheme {
                val gameViewModel: GameViewModel = viewModel()
                val savedPlayers = getSavedPlayerLists(baseContext)
                gameViewModel.setSavedPlayerNames(savedPlayers)
                MainScaffold(gameViewModel, modifier = Modifier)
            }
        }
    }

    fun getSavedPlayerLists(context: Context): List<String> {
        val savedPlayers = mutableListOf<String>()
        try {
            val inputStream = context.assets.open("players.json")
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val gson = Gson()
            val listType = object : TypeToken<ArrayList<String>>() {}.type
            val playerList =
                gson.fromJson<ArrayList<String>>(bufferedReader, listType)
            playerList.forEach { savedPlayers.add(it) }
        } catch (e: Exception) {
            println("An unexpected error occurred: ${e.message}")
        }
        return savedPlayers
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
                        text = "",
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
            GameSetup(gameViewModel)
        } else {
            GamePlay(gameViewModel)
        }
    }
}
