package net.hawkins.gamescore.ui.leaderboard

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import net.hawkins.gamescore.TestData
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LeaderboardPlayScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun leaderboardScreen() {
        val fiveCrowns = TestData.getFiveCrowns()
        val sheldon = TestData.createPlayer("Sheldon", listOf(5, 20, 16))
        val amy = TestData.createPlayer("Amy", listOf(0, 3, 0))
        val players = listOf(sheldon, amy)
        val viewModel = LeaderboardViewModel()
        viewModel.onEvent(LeaderboardUiEvent.RefreshLeaderboard(fiveCrowns, players))
        composeTestRule.setContent {
            LeaderboardScreen(viewModel, {}, Modifier)
        }

        // Verify table headers are displayed
        composeTestRule.onNodeWithText("Rank").assertIsDisplayed()
        composeTestRule.onNodeWithText("Players").assertIsDisplayed()
        composeTestRule.onNodeWithText("Score").assertIsDisplayed()

        // Verify Rank 1
        composeTestRule.onNodeWithText("1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Amy").assertIsDisplayed()
        composeTestRule.onNodeWithText("2").assertIsDisplayed()

        // Verify Rank 2
        composeTestRule.onNodeWithText("2").assertIsDisplayed()
        composeTestRule.onNodeWithText("Sheldon").assertIsDisplayed()
        composeTestRule.onNodeWithText("41").assertIsDisplayed()
    }
}