package net.hawkins.gamescore.ui

import android.app.Application
import android.content.ContextWrapper
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import net.hawkins.gamescore.AbstractBaseTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.File

class GameSetupViewModelTest : AbstractBaseTest() {

    @MockK
    lateinit var context: ContextWrapper

    @MockK
    lateinit var application: Application

    @MockK
    lateinit var viewModel: GameSetupViewModel

    @Before
    fun setUp() {
        context = mockk<ContextWrapper>()
        every { context.filesDir } returns randomTempDir()

        application = mockk<Application>()
        every { application.applicationContext } returns context

        viewModel = GameSetupViewModel(application)
    }


    @Test
    fun addRemoveSetPlayers() {
        viewModel.setPlayers(listOf("Sheldon", "Leonard", "Penny"))
        var uiState = viewModel.uiState.value

        assertEquals(3, uiState.playerNames.size)
        assertEquals("Sheldon", uiState.playerNames[0])
        assertEquals("Leonard", uiState.playerNames[1])
        assertEquals("Penny", uiState.playerNames[2])

        viewModel.removePlayer(1)
        uiState = viewModel.uiState.value
        assertEquals(2, uiState.playerNames.size)
        assertEquals("Sheldon", uiState.playerNames[0])
        assertEquals("Penny", uiState.playerNames[1])

        viewModel.removePlayer(1)
        uiState = viewModel.uiState.value
        assertEquals(1, uiState.playerNames.size)
        assertEquals("Sheldon", uiState.playerNames[0])

        viewModel.addPlayer("Howard")
        uiState = viewModel.uiState.value
        assertEquals(2, uiState.playerNames.size)
        assertEquals("Sheldon", uiState.playerNames[0])
        assertEquals("Howard", uiState.playerNames[1])

        viewModel.removePlayer(1)
        uiState = viewModel.uiState.value
        assertEquals(1, uiState.playerNames.size)
        assertEquals("Sheldon", uiState.playerNames[0])

        viewModel.removePlayer(0)
        uiState = viewModel.uiState.value
        assertEquals(0, uiState.playerNames.size)
    }
}