package net.hawkins.gamescore.ui.gamesetup

import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertNull
import junit.framework.Assert.assertTrue
import net.hawkins.gamescore.data.model.Game
import org.junit.Test

class GameSetupUiStateTest {

    @Test
    fun noArgsConstructor() {
        val uiState = GameSetupUiState()
        assertEquals(0, uiState.gameId)
        assertEquals("", uiState.gameName)
        assertNull(uiState.gameObjectiveGoal)
        assertNull(uiState.gameObjectiveRounds)
        assertEquals(Game.Objective.Type.HIGH_SCORE, uiState.gameObjectiveType)
        assertNull(uiState.gameRoundObjectiveGoal)
        assertNull(uiState.gameRoundObjectiveDisplayValue)
        assertEquals(Game.Colors.Color.DEFAULT, uiState.gameRoundObjectiveDisplayColor)
        assertEquals(true, uiState.gameConstraintEqualHandSizes)
        assertEquals(false, uiState.gameConstraintPositiveOnly)
        assertNull(uiState.gameConstraintScoreModulus)
        assertEquals(Game.Colors.Color.DEFAULT, uiState.gameColorsNegativeScore)
        assertEquals(Game.Colors.Color.DEFAULT, uiState.gameColorsPositiveScore)
        assertFalse(uiState.isValidName)
    }

    @Test
    fun toGame() {
        val uiState = GameSetupUiState(
            gameId = 1,
            gameName = "Game Name",
            gameObjectiveType = Game.Objective.Type.LOW_SCORE,
            gameObjectiveGoal = 500,
            gameObjectiveRounds = 5,
            gameRoundObjectiveGoal = 1,
            gameRoundObjectiveDisplayValue = "Win",
            gameRoundObjectiveDisplayColor = Game.Colors.Color.GREEN,
            gameConstraintScoreModulus = 4,
            gameConstraintPositiveOnly = true,
            gameConstraintEqualHandSizes = false,
            gameColorsNegativeScore = Game.Colors.Color.GREEN,
            gameColorsPositiveScore = Game.Colors.Color.RED
        )

        val game = uiState.toGame()
        assertEquals(1, game.id)
        assertEquals("Game Name", game.name)
        assertEquals(Game.Objective.Type.LOW_SCORE, game.objective.type)
        assertEquals(500, game.objective.goal)
        assertEquals(5, game.objective.rounds)
        assertEquals(1, game.roundObjective.goal)
        assertEquals("Win", game.roundObjective.displayValue)
        assertEquals(Game.Colors.Color.GREEN, game.roundObjective.displayColor)
        assertEquals(4, game.constraints.multipleOf)
        assertTrue(game.constraints.positiveOnly)
        assertFalse(game.constraints.equalHandSizes)
        assertEquals(Game.Colors.Color.GREEN, game.color.negativeScore)
        assertEquals(Game.Colors.Color.RED, game.color.positiveScore)
    }
}