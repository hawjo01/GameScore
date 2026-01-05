package net.hawkins.gamescore.ui.gameplay

import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase
import net.hawkins.gamescore.data.FavoriteGameRepository
import net.hawkins.gamescore.data.GameProgressRepository
import net.hawkins.gamescore.data.model.Game
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlin.test.assertNotNull

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
                type = Game.Objective.Type.LOW_SCORE
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
        TestCase.assertEquals("Five Crowns", uiState.value.game.name)
        TestCase.assertEquals(2, uiState.value.players.size)
        TestCase.assertEquals("Leonard", uiState.value.players[0].name)
        assertTrue(uiState.value.players[0].scores.isEmpty())
        TestCase.assertEquals("Penny", uiState.value.players[1].name)
        assertTrue(uiState.value.players[1].scores.isEmpty())

        viewModel.onEvent(GamePlayUiEvent.AddScore(0, 0))
        assertNull(uiState.value.winner)

        viewModel.onEvent(GamePlayUiEvent.AddScore(1, 0))
        assertNull(uiState.value.winner)

        viewModel.onEvent(GamePlayUiEvent.AddScore(0, 10))
        assertNull(uiState.value.winner)

        viewModel.onEvent(GamePlayUiEvent.AddScore(1, 5))
        assertNull(uiState.value.winner)

        viewModel.onEvent(GamePlayUiEvent.DetermineWinner)
        assertEquals("Penny", uiState.value.winner)
    }
}