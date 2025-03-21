package net.hawkins.cardscore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import net.hawkins.cardscore.ui.theme.CardScoreTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CardScoreTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CardScore(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }


    }
}

@Composable
fun CardScore(modifier: Modifier = Modifier) {
    var players = remember {
        mutableStateListOf(
            Player("Jen"),
            Player("Josh")
        )
    }
    Column(
        modifier = Modifier.fillMaxHeight()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Players(players, modifier.weight(1f))
        }
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 40.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ) {
            OutlinedButton(
                onClick = {
                    for ((index, player) in players.withIndex()) {
                        player.resetScores()
                    }
                },
                modifier = Modifier.padding(start = 10.dp)

            ) {
                Text("New Game")
            }
        }
    }
}

@Composable
fun Players(players: List<Player>, modifier: Modifier = Modifier) {
    for ((index, player) in players.withIndex()) {
        //var p = remember {mutableStateOf (player) }
        Player(player = player, index = index, modifier)
    }
}

@Composable
fun Player(player: Player, index: Int, modifier: Modifier = Modifier) {
    var newScore by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier
            .padding(start = if (index > 0) 10.dp else 0.dp)
    ) {
        Text(
            text = player.name,
            textAlign = TextAlign.Center,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = stringResource(R.string.total_score) + player.totalScore(),
            textAlign = TextAlign.Justify,
            fontSize = 22.sp
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically

        ) {
            TextField(
                value = newScore,
                onValueChange = { newScore = it },
                //label = { Text(text = "Enter Score")},
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                    }
                ),
                modifier = modifier
            )
            OutlinedButton(
                onClick = {
                    if (newScore.toIntOrNull() != null) {
                        player.addScore(newScore.toInt())
                        newScore = ""
                    }
                },
                modifier = Modifier.padding(start = 10.dp)

            ) {
                Text(text = stringResource(R.string.add))
            }
        }
        for (score in player.scores) {
            Text(
                text = score.toString()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CardScorePreview() {
    CardScoreTheme {
        CardScore()
    }
}