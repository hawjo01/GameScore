package net.hawkins.gamescore.ui.gamesetup

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GameSetupScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun gameSetupScreen_displayAllSections() {
        val uiState = GameSetupUiState(
            gameName = "Test Game",
            isValidName = true
        )

        composeTestRule.setContent {
            GameSetupScreenContent(
                uiState = uiState,
                onEvent = {},
                modifier = Modifier
            )
        }

        // Verify key sections and labels are displayed
        composeTestRule.onNodeWithText("Name").assertIsDisplayed()
        composeTestRule.onNodeWithText("Game Objective").assertIsDisplayed()
        composeTestRule.onNodeWithText("Round Objective").assertIsDisplayed()
        composeTestRule.onNodeWithText("Score Constraints").assertIsDisplayed()
        composeTestRule.onNodeWithText("Display Colors").assertIsDisplayed()
        
        // Verify some specific fields
        composeTestRule.onNodeWithText("Goal").assertIsDisplayed()
        composeTestRule.onNodeWithText("Type").assertIsDisplayed()
        composeTestRule.onNodeWithText("Only Positive").assertIsDisplayed()
    }
}
