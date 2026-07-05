package net.hawkins.gamescore.ui.gameplay

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import net.hawkins.gamescore.data.model.Game
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GamePlayScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun gamePlayScreen_displayPlayersAndScores() {
        val players = listOf(
            Player(name = "Sheldon", scores = listOf(Score(10))),
            Player(name = "Leonard", scores = listOf(Score(20)))
        )
        val uiState = GamePlayUiState(
            game = Game(name = "Test Game"),
            players = players
        )

        composeTestRule.setContent {
            GamePlayScreenContent(
                uiState = uiState,
                onEvent = {},
                isValidScore = { true },
                modifier = Modifier
            )
        }

        // Verify player names are displayed
        composeTestRule.onNodeWithText("Sheldon").assertIsDisplayed()
        composeTestRule.onNodeWithText("Leonard").assertIsDisplayed()

        // Verify scores are displayed (scores appear in headers and rounds, so we check they exist)
        composeTestRule.onAllNodesWithText("10").assertCountEquals(2)
        composeTestRule.onAllNodesWithText("20").assertCountEquals(2)
    }

    @Test
    fun gamePlayScreen_displayWinner() {
        val uiState = GamePlayUiState(
            game = Game(name = "Test Game"),
            players = emptyList(),
            winner = "Sheldon"
        )

        composeTestRule.setContent {
            Winner(winner = uiState.winner)
        }

        // Verify winner text is displayed (R.string.player_wins resolves to "%1$s Wins!")
        composeTestRule.onNodeWithText("Sheldon Wins!").assertIsDisplayed()
    }
}
