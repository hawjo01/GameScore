package net.hawkins.gamescore.ui.gameplay

import androidx.compose.ui.graphics.Color
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import net.hawkins.gamescore.data.FavoriteGameRepository
import net.hawkins.gamescore.data.GameProgressRepository
import net.hawkins.gamescore.data.model.FavoriteGame
import net.hawkins.gamescore.data.model.Game
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlin.test.assertNotNull

class GamePlayViewModelTest {

    @MockK
    lateinit var favoriteGameRepository: FavoriteGameRepository

    @MockK
    lateinit var gameProgressRepository: GameProgressRepository

    lateinit var sevens: Game

    lateinit var viewModel: GamePlayViewModel

    @Before
    fun setUp() {
        sevens = Game(
            name = "Sevens",
            constraints = Game.Constraints(multipleOf = 7)
        )

        favoriteGameRepository = mockk<FavoriteGameRepository>()
        gameProgressRepository = mockk<GameProgressRepository>()

        viewModel = GamePlayViewModel(
            _favoriteGameRepository = favoriteGameRepository,
            gameProgressRepository = gameProgressRepository
        )
    }

    @Test
    fun startGame() {
        val playerNames = listOf("Leonard", "Penny")
        viewModel.onEvent(GamePlayUiEvent.StartGame(sevens, playerNames))

        val uiState = viewModel.uiState
        assertNotNull(uiState.value.game)
        assertEquals("Sevens", uiState.value.game.name)
        assertEquals(2, uiState.value.players.size)
        assertEquals("Leonard", uiState.value.players[0].name)
        assertTrue(uiState.value.players[0].scores.isEmpty())
        assertEquals("Penny", uiState.value.players[1].name)
        assertTrue(uiState.value.players[1].scores.isEmpty())
    }

    @Test
    fun addScore() {
        val playerNames = listOf("Leonard", "Penny")
        viewModel.onEvent(GamePlayUiEvent.StartGame(sevens, playerNames))

        viewModel.onEvent(GamePlayUiEvent.AddScore(0, 21))

        val uiState = viewModel.uiState
        assertEquals(1, uiState.value.players[0].scores.size)
        assertEquals(21, uiState.value.players[0].scores[0].value)

        viewModel.onEvent(GamePlayUiEvent.AddScore(0, -14))
        assertEquals(2, uiState.value.players[0].scores.size)
        assertEquals(21, uiState.value.players[0].scores[0].value)
        assertEquals(-14, uiState.value.players[0].scores[1].value)
    }

    @Test
    fun changeScore() {
        val playerNames = listOf("Leonard", "Penny")
        viewModel.onEvent(GamePlayUiEvent.StartGame(game = sevens, playerNames))

        viewModel.onEvent(GamePlayUiEvent.AddScore(seatIndex = 0, score = 21))
        viewModel.onEvent(GamePlayUiEvent.AddScore(seatIndex = 0, score = -14))

        viewModel.onEvent(
            GamePlayUiEvent.ChangeScore(
                seatIndex = 0,
                roundIndex = 1,
                newScore = -28
            )
        )

        val uiState = viewModel.uiState
        assertEquals(2, uiState.value.players[0].scores.size)
        assertEquals(-28, uiState.value.players[0].scores[1].value)
    }

    @Test
    fun deleteScore() {
        val playerNames = listOf("Leonard", "Penny")
        viewModel.onEvent(GamePlayUiEvent.StartGame(game = sevens, playerNames))

        viewModel.onEvent(GamePlayUiEvent.AddScore(seatIndex = 0, score = 21))
        viewModel.onEvent(GamePlayUiEvent.AddScore(seatIndex = 0, score = -14))

        viewModel.onEvent(GamePlayUiEvent.DeleteScore(seatIndex = 0, roundIndex = 0))

        val uiState = viewModel.uiState.value
        assertEquals(1, uiState.players[0].scores.size)
        assertEquals(-14, uiState.players[0].scores[0].value)
    }

    @Test
    fun resetGame() {
        val playerNames = listOf("Leonard", "Penny")
        viewModel.onEvent(GamePlayUiEvent.StartGame(game = sevens, playerNames))

        viewModel.onEvent(GamePlayUiEvent.AddScore(seatIndex = 0, score = 21))
        viewModel.onEvent(GamePlayUiEvent.AddScore(seatIndex = 1, score = -14))
        viewModel.onEvent(GamePlayUiEvent.DetermineWinner)

        val uiState = viewModel.uiState
        assertEquals(1, uiState.value.players[0].scores.size)
        assertEquals(1, uiState.value.players[1].scores.size)
        assertEquals("Leonard", uiState.value.winner)

        viewModel.onEvent(GamePlayUiEvent.ResetGame)
        assertTrue(uiState.value.players[0].scores.isEmpty())
        assertTrue(uiState.value.players[1].scores.isEmpty())
        assertNull(uiState.value.winner)
    }

    @Test
    fun updateGame() {
        val playerNames = listOf("Leonard", "Penny")
        viewModel.onEvent(GamePlayUiEvent.StartGame(game = sevens, playerNames))
        viewModel.onEvent(GamePlayUiEvent.AddScore(0, 5))

        val uiState = viewModel.uiState
        assertEquals(Color.Unspecified, uiState.value.players[0].scores[0].color)

        val currentGame = uiState.value.game
        val currentColors = currentGame.color
        val newColors = currentColors.copy(positiveScore = Game.Colors.Color.GREEN)
        val updatedGame = currentGame.copy(color = newColors)

        viewModel.onEvent(GamePlayUiEvent.UpdateGame(updatedGame))
        assertEquals(Color.Green, uiState.value.players[0].scores[0].color)
    }

    @Test
    fun isValidScore() {
        val playerNames = listOf("Leonard", "Penny")
        viewModel.onEvent(GamePlayUiEvent.StartGame(game = sevens, playerNames))

        assertFalse(viewModel.isValidScore("a"))
        assertFalse(viewModel.isValidScore("6"))
        assertTrue(viewModel.isValidScore("0"))
        assertTrue(viewModel.isValidScore("7"))
        assertTrue(viewModel.isValidScore("-7"))
    }

    @Test
    fun saveFavoriteGame() {
        val playerNames = listOf("Leonard", "Penny")
        viewModel.onEvent(GamePlayUiEvent.StartGame(game = sevens, playerNames))

        val slot = slot<FavoriteGame>()
        every { favoriteGameRepository.save(capture(slot)) } answers { slot.captured }

        viewModel.onEvent(GamePlayUiEvent.SaveFavoriteGame("Favorite Name"))

        verify {
            favoriteGameRepository.save(capture(slot))
        }

        assertEquals("Favorite Name", slot.captured.name)
        assertEquals(sevens.name, slot.captured.game.name)
    }
}