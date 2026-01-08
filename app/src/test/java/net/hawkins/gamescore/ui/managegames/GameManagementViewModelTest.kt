package net.hawkins.gamescore.ui.managegames

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import net.hawkins.gamescore.data.GameRepository
import net.hawkins.gamescore.data.model.Game
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GameManagementViewModelTest {

    @MockK
    lateinit var gameRepository: GameRepository
    lateinit var viewModel: GameManagementViewModel
    lateinit var games: List<Game>

    @Before
    fun setUp() {
        games = listOf(
            Game(id = 1, name = "Game 1"),
            Game(id = 2, name = "Game 2")
        )
        gameRepository = mockk<GameRepository>()
        every { gameRepository.getAll() } returns games

        viewModel = GameManagementViewModel(_gameRepository = gameRepository)
    }

    @Test
    fun testInit() {
        assertEquals(2, viewModel.uiState.value.games.size)
    }

    @Test
    fun deleteGame() {
        val updatedGames = games.filterNot { game -> game.id != 1 }

        every { gameRepository.deleteById(any()) } returns Unit
        every { gameRepository.getAll() } returns updatedGames

        viewModel.onEvent(GameManagementUiEvent.DeleteGame(1))

        verify { gameRepository.deleteById(1) }
        assertEquals(1, viewModel.uiState.value.games.size)
    }

    @Test
    fun refreshState() {
        assertEquals(2, viewModel.uiState.value.games.size)

        val updatedGames = games.plus(Game(id = 3, name = "Game 3"))
        every { gameRepository.getAll() } returns updatedGames

        viewModel.onEvent(GameManagementUiEvent.RefreshState)

        assertEquals(3, viewModel.uiState.value.games.size)
    }
}