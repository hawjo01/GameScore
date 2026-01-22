package net.hawkins.gamescore.ui.gameplay

import androidx.compose.ui.graphics.Color
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import net.hawkins.gamescore.Assertions.Companion.assertEquals
import net.hawkins.gamescore.TestData
import net.hawkins.gamescore.data.FavoriteGameRepository
import net.hawkins.gamescore.data.GameProgressRepository
import net.hawkins.gamescore.data.GameRepository
import net.hawkins.gamescore.data.model.FavoriteGame
import net.hawkins.gamescore.data.model.Game
import net.hawkins.gamescore.data.model.GameProgress
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GamePlayViewModelTest {

    @MockK
    lateinit var favoriteGameRepository: FavoriteGameRepository

    @MockK
    lateinit var gameProgressRepository: GameProgressRepository

    @MockK
    lateinit var gameRepository: GameRepository
    lateinit var sevens: Game
    lateinit var viewModel: GamePlayViewModel

    @Before
    fun setUp() {
        sevens = Game(
            id = -1,
            name = "Sevens",
            constraints = Game.Constraints(multipleOf = 7)
        )

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
    fun refreshState() {
        val playerNames = listOf("Leonard", "Penny")
        viewModel.onEvent(GamePlayUiEvent.StartGame(game = sevens, playerNames))
        viewModel.onEvent(GamePlayUiEvent.AddScore(0, 5))

        val uiState = viewModel.uiState
        assertEquals(Color.Unspecified, uiState.value.players[0].scores[0].color)

        val currentGame = uiState.value.game
        val currentColors = currentGame.color
        val newColors = currentColors.copy(positiveScore = Game.Colors.Color.GREEN)
        val updatedGame = currentGame.copy(id = -1, color = newColors)

        every { gameRepository.getById(any()) } returns updatedGame
        viewModel.onEvent(GamePlayUiEvent.RefreshState)
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

    @Test
    fun isManualWinner() {
        val playerNames = listOf("Leonard", "Penny")
        viewModel.onEvent(GamePlayUiEvent.StartGame(game = sevens, playerNames))

        assertTrue(viewModel.isManualWinner())
    }

    @Test
    fun showRoundNumber() {
        assertFalse(viewModel.uiState.value.showRoundNumber)

        viewModel.onEvent(GamePlayUiEvent.ShowRoundNumber(true))
        assertTrue(viewModel.uiState.value.showRoundNumber)

        viewModel.onEvent(GamePlayUiEvent.ShowRoundNumber(false))
        assertFalse(viewModel.uiState.value.showRoundNumber)
    }

    @Test
    fun isGameInProgress_False() {
        every { gameProgressRepository.getById(any()) }.answers { null }

        assertFalse(viewModel.isGameInProgress())
    }

    @Test
    fun isGameInProgress_ManualWinner_True() {
        every { gameProgressRepository.getById(any()) }.answers {
            GameProgress(
                game = sevens,
                players = listOf(
                    TestData.createPlayer("Sheldon", listOf(0, 7)),
                    TestData.createPlayer("Leonard", listOf(7, 21))
                ),
                winner = null
            )
        }

        assertTrue(viewModel.isGameInProgress())
    }

    @Test
    fun loadInProgressGame_NoInProgressGame() {
        every { gameProgressRepository.getById(any()) }.answers { null }

        viewModel.loadInProgressGame()

        assertTrue(viewModel.uiState.value.players.isEmpty())
        assertEquals("", viewModel.uiState.value.game.name)
    }

    @Test
    fun loadInProgressGame_ManualWinner() {
        assertTrue(viewModel.uiState.value.players.isEmpty())
        assertEquals("", viewModel.uiState.value.game.name)

        val player1 = TestData.createPlayer("Sheldon", listOf(0, 7))
        val player2 = TestData.createPlayer("Leonard", listOf(7, 21))

        every { gameProgressRepository.getById(any()) }.answers {
            GameProgress(
                game = sevens,
                players = listOf(player1, player2),
                winner = null
            )
        }

        viewModel.loadInProgressGame()

        assertTrue(viewModel.uiState.value.players.isNotEmpty())
        assertEquals(player1, viewModel.uiState.value.players[0])
        assertEquals(player2, viewModel.uiState.value.players[1])
        assertNull(viewModel.uiState.value.winner)
    }

    @Test
    fun loadInProgressGame_WithObjectiveGoaMet() {
        assertTrue(viewModel.uiState.value.players.isEmpty())
        assertEquals("", viewModel.uiState.value.game.name)

        val player1 = TestData.createPlayer("Sheldon", listOf(0, 7))
        val player2 = TestData.createPlayer("Leonard", listOf(7, 21))

        val sevensWithGoal = sevens.copy(objective = Game.Objective(goal = 28))

        every { gameProgressRepository.getById(any()) }.answers {
            GameProgress(
                game = sevensWithGoal,
                players = listOf(player1, player2),
                winner = null
            )
        }

        viewModel.loadInProgressGame()

        assertTrue(viewModel.uiState.value.players.isNotEmpty())
        assertEquals(player1, viewModel.uiState.value.players[0])
        assertEquals(player2, viewModel.uiState.value.players[1])
        assertEquals("Leonard", viewModel.uiState.value.winner)
    }

    @Test
    fun loadInProgressGame_WithObjectiveGoaUnMet() {
        assertTrue(viewModel.uiState.value.players.isEmpty())
        assertEquals("", viewModel.uiState.value.game.name)

        val player1 = TestData.createPlayer("Sheldon", listOf(0, 7))
        val player2 = TestData.createPlayer("Leonard", listOf(7, 21))

        val sevensWithGoal = sevens.copy(objective = Game.Objective(goal = 35))

        every { gameProgressRepository.getById(any()) }.answers {
            GameProgress(
                game = sevensWithGoal,
                players = listOf(player1, player2),
                winner = null
            )
        }

        viewModel.loadInProgressGame()

        assertTrue(viewModel.uiState.value.players.isNotEmpty())
        assertEquals(player1, viewModel.uiState.value.players[0])
        assertEquals(player2, viewModel.uiState.value.players[1])
        assertNull(viewModel.uiState.value.winner)
    }
}