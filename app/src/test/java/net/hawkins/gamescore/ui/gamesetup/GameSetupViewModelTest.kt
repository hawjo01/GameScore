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

        viewModel.onEvent(GameSetupUiEvent.SetGameName("Game Name"))
        assertEquals("Game Name", uiState.value.game.name)
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
    fun setMode() {
        val uiState = viewModel.uiState
        assertEquals(GameSetupUiState.Mode.NEW, uiState.value.mode)

        viewModel.onEvent(GameSetupUiEvent.SetScreenMode(GameSetupUiState.Mode.VIEW))
        assertEquals(GameSetupUiState.Mode.VIEW, uiState.value.mode)

        viewModel.onEvent(GameSetupUiEvent.SetScreenMode(GameSetupUiState.Mode.EDIT))
        assertEquals(GameSetupUiState.Mode.EDIT, uiState.value.mode)
    }

    @Test
    fun setAndGetAndResetGame() {
        val game = Game(
            id = 1,
            name = "Game Name",
            objective = Game.Objective(
                type = Game.Objective.Type.LOW_SCORE,
                goal = 500
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
        val storedGame = viewModel.getGame()
        assertEquals(1, storedGame.id)
        assertEquals("Game Name", storedGame.name)
        assertEquals(Game.Objective.Type.LOW_SCORE, storedGame.objective.type)
        assertEquals(500, storedGame.objective.goal)
        assertTrue(storedGame.constraints.positiveOnly)
        assertEquals(4, storedGame.constraints.multipleOf)
        assertFalse(storedGame.constraints.equalHandSizes)
        assertEquals(Game.Colors.Color.GREEN, storedGame.color.negativeScore)
        assertEquals(Game.Colors.Color.RED, storedGame.color.positiveScore)

        viewModel.onEvent(GameSetupUiEvent.ResetGame)
        val uiState = viewModel.uiState
        assertNull(uiState.value.game.id)
        assertEquals("", uiState.value.game.name)
        assertEquals(Game.Objective.Type.HIGH_SCORE, uiState.value.game.objective.type)
        assertNull(uiState.value.game.objective.goal)
        assertFalse(uiState.value.game.constraints.positiveOnly)
        assertNull(uiState.value.game.constraints.multipleOf)
        assertTrue(uiState.value.game.constraints.equalHandSizes)
        assertEquals(Game.Colors.Color.DEFAULT, uiState.value.game.color.negativeScore)
        assertEquals(Game.Colors.Color.DEFAULT, uiState.value.game.color.positiveScore)
    }
}