package net.hawkins.gamescore.ui.leaderboard

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import net.hawkins.gamescore.data.model.Leaderboard
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LeaderboardScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun leaderboardScreen_displayWinner() {
        val gameName = "Seven Crowns"
        val rankings = listOf(
            Leaderboard.Ranking(1, 5, listOf("Sheldon")),
            Leaderboard.Ranking(2, 10, listOf("Penny"))
        )
        val leaderboard = Leaderboard(winner = "Sheldon", gameName = gameName, rankings = rankings)
        val uiState = LeaderboardUiState(leaderboard = leaderboard)

        composeTestRule.setContent {
            LeaderboardScreenContent(
                uiState = uiState,
                modifier = Modifier
            )
        }

        // Verify winner text is displayed
        composeTestRule.onNodeWithText("Sheldon Wins!").assertIsDisplayed()
        
        // Verify table content
        composeTestRule.onNodeWithText("Sheldon").assertIsDisplayed()
        composeTestRule.onNodeWithText("Penny").assertIsDisplayed()
        composeTestRule.onNodeWithText("5").assertIsDisplayed()
        composeTestRule.onNodeWithText("10").assertIsDisplayed()
    }

    @Test
    fun leaderboardScreen_displayNegativeScores() {
        val rankings = listOf(
            Leaderboard.Ranking(1, 10, listOf("Sheldon")),
            Leaderboard.Ranking(2, -5, listOf("Leonard"))
        )
        val leaderboard = Leaderboard(gameName = "Test Game", rankings = rankings)
        // Set displayNegativeScoreInRed to true
        val uiState = LeaderboardUiState(
            leaderboard = leaderboard,
            displayNegativeScoreInRed = true
        )

        composeTestRule.setContent {
            LeaderboardScreenContent(
                uiState = uiState,
                modifier = Modifier
            )
        }

        // Verify negative score is displayed
        composeTestRule.onNodeWithText("-5").assertIsDisplayed()
        composeTestRule.onNodeWithText("Leonard").assertIsDisplayed()
    }
}
