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
        assertEquals("", uiState.value.game.name)
        assertFalse(uiState.value.isValidName)

        viewModel.onEvent(GameSetupUiEvent.SetGameName("Game Name"))
        assertEquals("Game Name", uiState.value.game.name)
        assertTrue(uiState.value.isValidName)

        viewModel.onEvent(GameSetupUiEvent.SetGameName(""))
        assertEquals("", uiState.value.game.name)
        assertFalse(uiState.value.isValidName)
    }

    @Test
    fun setConstraintAllowNegative() {
        val uiState = viewModel.uiState
        assertFalse(uiState.value.game.constraints.positiveOnly)

        viewModel.onEvent(GameSetupUiEvent.SetConstraintPositiveOnlyScores(true))
        assertTrue(uiState.value.game.constraints.positiveOnly)
    }

    @Test
    fun setConstraintEqualHandSizes() {
        val uiState = viewModel.uiState
        assertTrue(uiState.value.game.constraints.equalHandSizes)

        viewModel.onEvent(GameSetupUiEvent.SetConstraintEqualHandSizes(false))
        assertFalse(uiState.value.game.constraints.equalHandSizes)
    }

    @Test
    fun setConstraintModulus() {
        val uiState = viewModel.uiState
        assertNull(uiState.value.game.constraints.multipleOf)

        viewModel.onEvent(GameSetupUiEvent.SetConstraintScoreModulus(3))
        assertEquals(3, uiState.value.game.constraints.multipleOf)
    }

    @Test
    fun setObjectiveType() {
        val uiState = viewModel.uiState
        assertEquals(Game.Objective.Type.HIGH_SCORE, uiState.value.game.objective.type)

        viewModel.onEvent(GameSetupUiEvent.SetObjectiveType(Game.Objective.Type.LOW_SCORE))
        assertEquals(Game.Objective.Type.LOW_SCORE, uiState.value.game.objective.type)
    }

    @Test
    fun setObjectiveGoal() {
        val uiState = viewModel.uiState
        assertNull(uiState.value.game.objective.goal)

        viewModel.onEvent(GameSetupUiEvent.SetObjectiveGoal(19))
        assertEquals(19, uiState.value.game.objective.goal)
    }

    @Test
    fun setObjectiveRounds() {
        val uiState = viewModel.uiState
        assertNull(uiState.value.game.objective.rounds)

        viewModel.onEvent(GameSetupUiEvent.SetObjectiveRounds(11))
        assertEquals(11, uiState.value.game.objective.rounds)
    }

    @Test
    fun setRoundObjectiveGoal() {
        val uiState = viewModel.uiState
        assertNull(uiState.value.game.roundObjective.goal)

        viewModel.onEvent(GameSetupUiEvent.SetRoundObjectiveGoal(0))
        assertEquals(0, uiState.value.game.roundObjective.goal)
    }

    @Test
    fun setRoundObjectiveDisplayValue() {
        val uiState = viewModel.uiState
        assertNull(uiState.value.game.roundObjective.displayValue)

        viewModel.onEvent(GameSetupUiEvent.SetRoundObjectiveDisplayValue("Win"))
        assertEquals("Win", uiState.value.game.roundObjective.displayValue)
    }

    @Test
    fun setRoundObjectiveDisplayValue_EmptyStringToNull() {
        val uiState = viewModel.uiState
        assertNull(uiState.value.game.roundObjective.displayValue)

        viewModel.onEvent(GameSetupUiEvent.SetRoundObjectiveDisplayValue(""))
        assertNull(uiState.value.game.roundObjective.displayValue)
    }

    @Test
    fun setRoundObjectiveDisplayValue_Null() {
        val uiState = viewModel.uiState
        assertNull(uiState.value.game.roundObjective.displayValue)

        viewModel.onEvent(GameSetupUiEvent.SetRoundObjectiveDisplayValue(null))
        assertNull(uiState.value.game.roundObjective.displayValue)
    }

    @Test
    fun setRoundObjectiveDisplayColor() {
        val uiState = viewModel.uiState
        assertEquals(Game.Colors.Color.DEFAULT, uiState.value.game.roundObjective.displayColor)

        viewModel.onEvent(GameSetupUiEvent.SetRoundObjectiveDisplayColor(Game.Colors.Color.RED))
        assertEquals(Game.Colors.Color.RED, uiState.value.game.roundObjective.displayColor)
    }

    @Test
    fun setDisplayNegative() {
        val uiState = viewModel.uiState
        assertEquals(Game.Colors.Color.DEFAULT, uiState.value.game.color.negativeScore)

        viewModel.onEvent(GameSetupUiEvent.SetDisplayNegativeColor(Game.Colors.Color.RED))
        assertEquals(Game.Colors.Color.RED, uiState.value.game.color.negativeScore)
    }

    @Test
    fun setDisplayPositive() {
        val uiState = viewModel.uiState
        assertEquals(Game.Colors.Color.DEFAULT, uiState.value.game.color.positiveScore)

        viewModel.onEvent(GameSetupUiEvent.SetDisplayPositiveColor(Game.Colors.Color.GREEN))
        assertEquals(Game.Colors.Color.GREEN, uiState.value.game.color.positiveScore)
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
        assertEquals(1, uiState.value.game.id)
        assertEquals("Game Name", uiState.value.game.name)
        assertEquals(Game.Objective.Type.LOW_SCORE, uiState.value.game.objective.type)
        assertEquals(500, uiState.value.game.objective.goal)
        assertEquals(5, uiState.value.game.objective.rounds)
        assertEquals(1, uiState.value.game.roundObjective.goal)
        assertEquals("Win", uiState.value.game.roundObjective.displayValue)
        assertEquals(Game.Colors.Color.GREEN, uiState.value.game.roundObjective.displayColor)
        assertTrue(uiState.value.game.constraints.positiveOnly)
        assertEquals(4, uiState.value.game.constraints.multipleOf)
        assertFalse(uiState.value.game.constraints.equalHandSizes)
        assertEquals(Game.Colors.Color.GREEN, uiState.value.game.color.negativeScore)
        assertEquals(Game.Colors.Color.RED, uiState.value.game.color.positiveScore)
        assertTrue(uiState.value.isValidName)

        viewModel.onEvent(GameSetupUiEvent.NewGame)
        assertEquals(0, uiState.value.game.id)
        assertEquals("", uiState.value.game.name)
        assertEquals(Game.Objective.Type.HIGH_SCORE, uiState.value.game.objective.type)
        assertNull(uiState.value.game.objective.goal)
        assertNull(uiState.value.game.objective.rounds)
        assertNull(uiState.value.game.roundObjective.goal)
        assertNull(uiState.value.game.roundObjective.displayValue)
        assertEquals(Game.Colors.Color.DEFAULT, uiState.value.game.roundObjective.displayColor)
        assertFalse(uiState.value.game.constraints.positiveOnly)
        assertNull(uiState.value.game.constraints.multipleOf)
        assertTrue(uiState.value.game.constraints.equalHandSizes)
        assertEquals(Game.Colors.Color.DEFAULT, uiState.value.game.color.negativeScore)
        assertEquals(Game.Colors.Color.DEFAULT, uiState.value.game.color.positiveScore)
        assertFalse(uiState.value.isValidName)
    }
}