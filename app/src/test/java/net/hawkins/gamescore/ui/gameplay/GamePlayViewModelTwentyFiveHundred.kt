package net.hawkins.gamescore.ui.gameplay

import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import net.hawkins.gamescore.TestData
import net.hawkins.gamescore.data.FavoriteGameRepository
import net.hawkins.gamescore.data.GameProgressRepository
import net.hawkins.gamescore.data.GameRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GamePlayViewModelTwentyFiveHundred {

    @MockK
    lateinit var favoriteGameRepository: FavoriteGameRepository
    @MockK
    lateinit var gameProgressRepository: GameProgressRepository
    @MockK
    lateinit var gameRepository: GameRepository
    lateinit var viewModel: GamePlayViewModel

    @Before
    fun setUp() {
        favoriteGameRepository = mockk<FavoriteGameRepository>()
        gameProgressRepository = mockk<GameProgressRepository>()
        gameRepository = mockk<GameRepository>()

        viewModel = GamePlayViewModel(
            _favoriteGameRepository = favoriteGameRepository,
            gameProgressRepository = gameProgressRepository,
            _gameRepository = gameRepository
        )
    }

    @Test
    fun determineWinner() {
        val playerNames = listOf("Leonard", "Penny")
        val twentyFiveHundred = TestData.getTwentyFiveHundred()
        viewModel.onEvent(GamePlayUiEvent.StartGame(twentyFiveHundred, playerNames))

        val uiState = viewModel.uiState
        assertNotNull(uiState.value.game)
        assertEquals("2500", uiState.value.game.name)
        assertEquals(2, uiState.value.players.size)
        assertEquals("Leonard", uiState.value.players[0].name)
        assertTrue(uiState.value.players[0].scores.isEmpty())
        assertEquals("Penny", uiState.value.players[1].name)
        assertTrue(uiState.value.players[1].scores.isEmpty())

        viewModel.onEvent(GamePlayUiEvent.AddScore(0, 0))
        assertNull(uiState.value.winner)

        viewModel.onEvent(GamePlayUiEvent.AddScore(1, 6))
        assertNull(uiState.value.winner)

        viewModel.onEvent(GamePlayUiEvent.AddScore(0, 10))
        assertNull(uiState.value.winner)

        viewModel.onEvent(GamePlayUiEvent.AddScore(1, 5))
        assertNull(uiState.value.winner)

        // Round 3
        viewModel.onEvent(GamePlayUiEvent.AddScore(0, 230))
        viewModel.onEvent(GamePlayUiEvent.AddScore(1, 130))
        // Round 4
        viewModel.onEvent(GamePlayUiEvent.AddScore(0, 240))
        viewModel.onEvent(GamePlayUiEvent.AddScore(1, 130))
        // Round 5
        viewModel.onEvent(GamePlayUiEvent.AddScore(0, 350))
        viewModel.onEvent(GamePlayUiEvent.AddScore(1, -25))
        // Round 6
        viewModel.onEvent(GamePlayUiEvent.AddScore(0, 1000))
        viewModel.onEvent(GamePlayUiEvent.AddScore(1, 50))

        assertNull(uiState.value.winner)

        // Round 7
        viewModel.onEvent(GamePlayUiEvent.AddScore(0, 1000))
        viewModel.onEvent(GamePlayUiEvent.AddScore(1, 56))

        assertEquals("Leonard", uiState.value.winner)
    }

    @Test
    fun isManualWinner() {
        val playerNames = listOf("Penny", "Amy")
        val twentyFiveHundred = TestData.getTwentyFiveHundred()
        viewModel.onEvent(GamePlayUiEvent.StartGame(game = twentyFiveHundred, playerNames))

        assertFalse(viewModel.isManualWinner())
    }
}