package net.hawkins.cardscore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    var players by remember {
        mutableStateOf(
            arrayOf(
                Player("Jen", arrayListOf<Int>()),
                Player("Josh", arrayListOf<Int>())
            )
        )
    }
    Column() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Players(players, modifier.weight(1f))
        }
    }
}

@Composable
fun Players(players: Array<Player>, modifier: Modifier = Modifier) {
    for ((index, player) in players.withIndex()) {
        Player(player = player, index = index, modifier)
    }
}

@Composable
fun Player(player: Player, index: Int, modifier: Modifier = Modifier) {
    var newScore by remember { mutableStateOf("") }
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
            text = "Total Score: " + player.totalScore(),
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = modifier
            )
            OutlinedButton(
                onClick = {
                    player.scores.add(newScore.toInt())
                    newScore = ""
                },
                modifier = Modifier.padding(start = 10.dp)

            ) {
                Text("Add")
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

data class Player(val name: String, val scores: ArrayList<Int> = arrayListOf()) {

    fun totalScore(): Int {
        var total = 0
        for (score in this.scores) {
            total += score
        }
        return total
    }
}