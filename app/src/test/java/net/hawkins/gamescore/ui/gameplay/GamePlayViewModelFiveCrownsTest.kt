package net.hawkins.gamescore.ui.gameplay

import androidx.compose.ui.graphics.Color
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import net.hawkins.gamescore.data.FavoriteGameRepository
import net.hawkins.gamescore.data.GameProgressRepository
import net.hawkins.gamescore.data.model.Game
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GamePlayViewModelFiveCrownsTest {

    @MockK
    lateinit var favoriteGameRepository: FavoriteGameRepository

    @MockK
    lateinit var gameProgressRepository: GameProgressRepository

    lateinit var viewModel: GamePlayViewModel

    lateinit var fiveCrowns: Game

    @Before
    fun setUp() {
        favoriteGameRepository = mockk<FavoriteGameRepository>()
        gameProgressRepository = mockk<GameProgressRepository>()

        fiveCrowns = Game(
            name = "Five Crowns",
            objective = Game.Objective(
                type = Game.Objective.Type.LOW_SCORE,
                rounds = 11

            ),
            roundObjective = Game.RoundObjective(
                goal = 0,
                displayValue = "Win",
                displayColor = Game.Colors.Color.GREEN
            ),
            constraints = Game.Constraints(
                positiveOnly = true
            )
        )

        viewModel = GamePlayViewModel(
            _favoriteGameRepository = favoriteGameRepository,
            gameProgressRepository = gameProgressRepository
        )
    }

    @Test
    fun determineWinner() {
        val playerNames = listOf("Leonard", "Penny")
        viewModel.onEvent(GamePlayUiEvent.StartGame(fiveCrowns, playerNames))

        val uiState = viewModel.uiState
        assertNotNull(uiState.value.game)
        assertEquals("Five Crowns", uiState.value.game.name)
        assertEquals(2, uiState.value.players.size)
        assertEquals("Leonard", uiState.value.players[0].name)
        assertTrue(uiState.value.players[0].scores.isEmpty())
        assertEquals("Penny", uiState.value.players[1].name)
        assertTrue(uiState.value.players[1].scores.isEmpty())

        viewModel.onEvent(GamePlayUiEvent.AddScore(0, 0))
        assertNull(uiState.value.winner)
        assertEquals("Win", uiState.value.players[0].scores[0].displayValue)
        assertEquals(Color.Green, uiState.value.players[0].scores[0].color)

        viewModel.onEvent(GamePlayUiEvent.AddScore(1, 5))
        assertNull(uiState.value.winner)
        assertNull(uiState.value.players[1].scores[0].displayValue)
        assertEquals(Color.Unspecified, uiState.value.players[1].scores[0].color)


        viewModel.onEvent(GamePlayUiEvent.AddScore(0, 10))
        assertNull(uiState.value.winner)

        viewModel.onEvent(GamePlayUiEvent.AddScore(1, 0))
        assertNull(uiState.value.winner)

        // Round 3
        viewModel.onEvent(GamePlayUiEvent.AddScore(0, 5))
        viewModel.onEvent(GamePlayUiEvent.AddScore(1, 10))
        // Round 4
        viewModel.onEvent(GamePlayUiEvent.AddScore(0, 5))
        viewModel.onEvent(GamePlayUiEvent.AddScore(1, 10))
        // Round 5
        viewModel.onEvent(GamePlayUiEvent.AddScore(0, 0))
        viewModel.onEvent(GamePlayUiEvent.AddScore(1, 0))
        // Round 6
        viewModel.onEvent(GamePlayUiEvent.AddScore(0, 3))
        viewModel.onEvent(GamePlayUiEvent.AddScore(1, 16))
        // Round 7
        viewModel.onEvent(GamePlayUiEvent.AddScore(0, 0))
        viewModel.onEvent(GamePlayUiEvent.AddScore(1, 0))
        // Round 8
        viewModel.onEvent(GamePlayUiEvent.AddScore(0, 6))
        viewModel.onEvent(GamePlayUiEvent.AddScore(1, 0))
        // Round 9
        viewModel.onEvent(GamePlayUiEvent.AddScore(0, 5))
        viewModel.onEvent(GamePlayUiEvent.AddScore(1, 2))
        // Round 10
        viewModel.onEvent(GamePlayUiEvent.AddScore(0, 23))
        viewModel.onEvent(GamePlayUiEvent.AddScore(1, 0))

        assertNull(uiState.value.winner)

        // Round 11
        viewModel.onEvent(GamePlayUiEvent.AddScore(0, 5))
        viewModel.onEvent(GamePlayUiEvent.AddScore(1, 4))

        assertEquals("Penny", uiState.value.winner)
    }

    @Test
    fun isManualWinner() {
        val playerNames = listOf("Rajesh", "Howard")
        viewModel.onEvent(GamePlayUiEvent.StartGame(game = fiveCrowns, playerNames))

        assertFalse(viewModel.isManualWinner())
    }
}