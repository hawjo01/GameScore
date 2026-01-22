package net.hawkins.gamescore.ui.gameplaysetup

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import net.hawkins.gamescore.data.FavoriteGameRepository
import net.hawkins.gamescore.data.FavoritePlayerRepository
import net.hawkins.gamescore.data.GameRepository
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GamePlaySetupViewModelTest {
    @MockK
    lateinit var gameRepository: GameRepository

    @MockK
    lateinit var favoritePlayerRepository: FavoritePlayerRepository

    @MockK
    lateinit var favoriteGameRepository: FavoriteGameRepository
    lateinit var viewModel: GamePlaySetupViewModel

    @Before
    fun setUp() {
        gameRepository = mockk<GameRepository>()
        every { gameRepository.getAll() } returns emptyList()

        favoriteGameRepository = mockk<FavoriteGameRepository>()
        every { favoriteGameRepository.getAll() } returns emptyList()

        favoritePlayerRepository = mockk<FavoritePlayerRepository>()
        every { favoritePlayerRepository.getAll() } returns emptyList()

        viewModel = GamePlaySetupViewModel(
            _favoriteGameRepository = favoriteGameRepository,
            _playerRepository = favoritePlayerRepository,
            _gameRepository = gameRepository
        )
    }

    @Test
    fun addRemoveSetPlayers() {
        viewModel.onEvent(GamePlaySetupUiEvent.SetPlayers(listOf("Sheldon", "Leonard", "Penny")))
        var uiState = viewModel.uiState.value

        assertEquals(3, uiState.playerNames.size)
        assertEquals("Sheldon", uiState.playerNames[0])
        assertEquals("Leonard", uiState.playerNames[1])
        assertEquals("Penny", uiState.playerNames[2])

        viewModel.onEvent(GamePlaySetupUiEvent.RemovePlayer(1))
        uiState = viewModel.uiState.value
        assertEquals(2, uiState.playerNames.size)
        assertEquals("Sheldon", uiState.playerNames[0])
        assertEquals("Penny", uiState.playerNames[1])

        viewModel.onEvent(GamePlaySetupUiEvent.RemovePlayer(index = 1))
        uiState = viewModel.uiState.value
        assertEquals(1, uiState.playerNames.size)
        assertEquals("Sheldon", uiState.playerNames[0])

        viewModel.onEvent(GamePlaySetupUiEvent.AddPlayer("Howard"))
        uiState = viewModel.uiState.value
        assertEquals(2, uiState.playerNames.size)
        assertEquals("Sheldon", uiState.playerNames[0])
        assertEquals("Howard", uiState.playerNames[1])

        viewModel.onEvent(GamePlaySetupUiEvent.RemovePlayer(index = 1))
        uiState = viewModel.uiState.value
        assertEquals(1, uiState.playerNames.size)
        assertEquals("Sheldon", uiState.playerNames[0])

        viewModel.onEvent(GamePlaySetupUiEvent.RemovePlayer(index = 0))
        uiState = viewModel.uiState.value
        assertEquals(0, uiState.playerNames.size)
    }
}