package net.hawkins.gamescore.ui.gamesetup

import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import net.hawkins.gamescore.data.GameRepository
import net.hawkins.gamescore.data.model.Game
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GameSetupViewModelTest {

    @MockK
    lateinit var gameRepository: GameRepository

    lateinit var viewModel: GameSetupViewModel

    @Before
    fun setUp() {
        gameRepository = mockk<GameRepository>()

        viewModel = GameSetupViewModel(_gameRepository = gameRepository)
    }

    @Test
    fun setGameName() {
        val uiState = viewModel.uiState
        assertEquals("", uiState.value.gameName)
        assertFalse(uiState.value.isValidName)

        viewModel.onEvent(GameSetupUiEvent.SetGameName("Game Name"))
        assertEquals("Game Name", uiState.value.gameName)
        assertTrue(uiState.value.isValidName)

        viewModel.onEvent(GameSetupUiEvent.SetGameName(""))
        assertEquals("", uiState.value.gameName)
        assertFalse(uiState.value.isValidName)
    }

    @Test
    fun setConstraintAllowNegative() {
        val uiState = viewModel.uiState
        assertFalse(uiState.value.gameConstraintPositiveOnly)

        viewModel.onEvent(GameSetupUiEvent.SetConstraintPositiveOnlyScores(true))
        assertTrue(uiState.value.gameConstraintPositiveOnly)
    }

    @Test
    fun setConstraintEqualHandSizes() {
        val uiState = viewModel.uiState
        assertTrue(uiState.value.gameConstraintEqualHandSizes)

        viewModel.onEvent(GameSetupUiEvent.SetConstraintEqualHandSizes(false))
        assertFalse(uiState.value.gameConstraintEqualHandSizes)
    }

    @Test
    fun setConstraintModulus() {
        val uiState = viewModel.uiState
        assertNull(uiState.value.gameConstraintScoreModulus)

        viewModel.onEvent(GameSetupUiEvent.SetConstraintScoreModulus(3))
        assertEquals(3, uiState.value.gameConstraintScoreModulus)
    }

    @Test
    fun setObjectiveType() {
        val uiState = viewModel.uiState
        assertEquals(Game.Objective.Type.HIGH_SCORE, uiState.value.gameObjectiveType)

        viewModel.onEvent(GameSetupUiEvent.SetObjectiveType(Game.Objective.Type.LOW_SCORE))
        assertEquals(Game.Objective.Type.LOW_SCORE, uiState.value.gameObjectiveType)
    }

    @Test
    fun setObjectiveGoal() {
        val uiState = viewModel.uiState
        assertNull(uiState.value.gameObjectiveGoal)

        viewModel.onEvent(GameSetupUiEvent.SetObjectiveGoal(19))
        assertEquals(19, uiState.value.gameObjectiveGoal)
    }

    @Test
    fun setObjectiveRounds() {
        val uiState = viewModel.uiState
        assertNull(uiState.value.gameObjectiveRounds)

        viewModel.onEvent(GameSetupUiEvent.SetObjectiveRounds(11))
        assertEquals(11, uiState.value.gameObjectiveRounds)
    }

    @Test
    fun setRoundObjectiveGoal() {
        val uiState = viewModel.uiState
        assertNull(uiState.value.gameRoundObjectiveGoal)

        viewModel.onEvent(GameSetupUiEvent.SetRoundObjectiveGoal(0))
        assertEquals(0, uiState.value.gameRoundObjectiveGoal)
    }

    @Test
    fun setRoundObjectiveDisplayValue() {
        val uiState = viewModel.uiState
        assertNull(uiState.value.gameRoundObjectiveDisplayValue)

        viewModel.onEvent(GameSetupUiEvent.SetRoundObjectiveDisplayValue("Win"))
        assertEquals("Win", uiState.value.gameRoundObjectiveDisplayValue)
    }

    @Test
    fun setRoundObjectiveDisplayValue_EmptyStringToNull() {
        val uiState = viewModel.uiState
        assertNull(uiState.value.gameRoundObjectiveDisplayValue)

        viewModel.onEvent(GameSetupUiEvent.SetRoundObjectiveDisplayValue(""))
        assertNull(uiState.value.gameRoundObjectiveDisplayValue)
    }

    @Test
    fun setRoundObjectiveDisplayValue_Null() {
        val uiState = viewModel.uiState
        assertNull(uiState.value.gameRoundObjectiveDisplayValue)

        viewModel.onEvent(GameSetupUiEvent.SetRoundObjectiveDisplayValue(null))
        assertNull(uiState.value.gameRoundObjectiveDisplayValue)
    }

    @Test
    fun setRoundObjectiveDisplayColor() {
        val uiState = viewModel.uiState
        assertEquals(Game.Colors.Color.DEFAULT, uiState.value.gameRoundObjectiveDisplayColor)

        viewModel.onEvent(GameSetupUiEvent.SetRoundObjectiveDisplayColor(Game.Colors.Color.RED))
        assertEquals(Game.Colors.Color.RED, uiState.value.gameRoundObjectiveDisplayColor)
    }

    @Test
    fun setDisplayNegative() {
        val uiState = viewModel.uiState
        assertEquals(Game.Colors.Color.DEFAULT, uiState.value.gameColorsNegativeScore)

        viewModel.onEvent(GameSetupUiEvent.SetDisplayNegativeColor(Game.Colors.Color.RED))
        assertEquals(Game.Colors.Color.RED, uiState.value.gameColorsNegativeScore)
    }

    @Test
    fun setDisplayPositive() {
        val uiState = viewModel.uiState
        assertEquals(Game.Colors.Color.DEFAULT, uiState.value.gameColorsPositiveScore)

        viewModel.onEvent(GameSetupUiEvent.SetDisplayPositiveColor(Game.Colors.Color.GREEN))
        assertEquals(Game.Colors.Color.GREEN, uiState.value.gameColorsPositiveScore)
    }

    @Test
    fun setAndResetGame() {
        val game = Game(
            id = 1,
            name = "Game Name",
            objective = Game.Objective(
                type = Game.Objective.Type.LOW_SCORE,
                goal = 500,
                rounds = 5
            ),
            roundObjective = Game.RoundObjective(
                goal = 1,
                displayValue = "Win",
                displayColor = Game.Colors.Color.GREEN
            ),
            constraints = Game.Constraints(
                positiveOnly = true,
                multipleOf = 4,
                equalHandSizes = false
            ),
            color = Game.Colors(
                negativeScore = Game.Colors.Color.GREEN,
                positiveScore = Game.Colors.Color.RED
            )
        )

        viewModel.onEvent(GameSetupUiEvent.SetGame(game))
        val uiState = viewModel.uiState
        assertEquals(1, uiState.value.gameId)
        assertEquals("Game Name", uiState.value.gameName)
        assertEquals(Game.Objective.Type.LOW_SCORE, uiState.value.gameObjectiveType)
        assertEquals(500, uiState.value.gameObjectiveGoal)
        assertEquals(5, uiState.value.gameObjectiveRounds)
        assertEquals(1, uiState.value.gameRoundObjectiveGoal)
        assertEquals("Win", uiState.value.gameRoundObjectiveDisplayValue)
        assertEquals(Game.Colors.Color.GREEN, uiState.value.gameRoundObjectiveDisplayColor)
        assertTrue(uiState.value.gameConstraintPositiveOnly)
        assertEquals(4, uiState.value.gameConstraintScoreModulus)
        assertFalse(uiState.value.gameConstraintEqualHandSizes)
        assertEquals(Game.Colors.Color.GREEN, uiState.value.gameColorsNegativeScore)
        assertEquals(Game.Colors.Color.RED, uiState.value.gameColorsPositiveScore)
        assertTrue(uiState.value.isValidName)

        viewModel.onEvent(GameSetupUiEvent.NewGame)
        assertEquals(0, uiState.value.gameId)
        assertEquals("", uiState.value.gameName)
        assertEquals(Game.Objective.Type.HIGH_SCORE, uiState.value.gameObjectiveType)
        assertNull(uiState.value.gameObjectiveGoal)
        assertNull(uiState.value.gameObjectiveRounds)
        assertNull(uiState.value.gameRoundObjectiveGoal)
        assertNull(uiState.value.gameRoundObjectiveDisplayValue)
        assertEquals(Game.Colors.Color.DEFAULT, uiState.value.gameRoundObjectiveDisplayColor)
        assertFalse(uiState.value.gameConstraintPositiveOnly)
        assertNull(uiState.value.gameConstraintScoreModulus)
        assertTrue(uiState.value.gameConstraintEqualHandSizes)
        assertEquals(Game.Colors.Color.DEFAULT, uiState.value.gameColorsNegativeScore)
        assertEquals(Game.Colors.Color.DEFAULT, uiState.value.gameColorsPositiveScore)
        assertFalse(uiState.value.isValidName)
    }
}