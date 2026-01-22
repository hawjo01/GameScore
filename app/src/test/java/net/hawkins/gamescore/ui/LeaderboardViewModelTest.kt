package net.hawkins.gamescore.ui

import net.hawkins.gamescore.TestData
import net.hawkins.gamescore.ui.gameplay.Player
import net.hawkins.gamescore.ui.leaderboard.LeaderboardUiEvent
import net.hawkins.gamescore.ui.leaderboard.LeaderboardViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class LeaderboardViewModelTest {

    lateinit var viewModel: LeaderboardViewModel

    @Before
    fun setUp() {
        viewModel = LeaderboardViewModel()
    }

    @Test
    fun refreshLeaderboard() {
        val uiState = viewModel.uiState
        assertNull(uiState.value.leaderboard.winner)
        assertEquals("", uiState.value.leaderboard.gameName)
        assertTrue(uiState.value.leaderboard.rankings.isEmpty())

        var player1 = Player("Sheldon")
        var player2 = Player("Leonard")
        var players = listOf(player1, player2)
        val fiveCrowns = TestData.getFiveCrowns()
        viewModel.onEvent(LeaderboardUiEvent.RefreshLeaderboard(fiveCrowns, players))

        assertNull(uiState.value.leaderboard.winner)
        assertEquals(fiveCrowns.name, uiState.value.leaderboard.gameName)
        var rankings = uiState.value.leaderboard.rankings
        assertFalse(rankings.isEmpty())

        assertEquals(1, rankings.size)
        var ranking1 = rankings[0]
        assertEquals(1, ranking1.rank)
        assertEquals(0, ranking1.score)
        assertEquals(listOf("Sheldon", "Leonard"), ranking1.playerNames)

        player1 = TestData.addScores(player1, 5)
        player2 = TestData.addScores(player2, 0)
        players = listOf(player1, player2)
        viewModel.onEvent(LeaderboardUiEvent.RefreshLeaderboard(fiveCrowns, players))
        assertEquals(fiveCrowns.name, uiState.value.leaderboard.gameName)
        rankings = uiState.value.leaderboard.rankings
        assertFalse(rankings.isEmpty())

        assertEquals(2, rankings.size)
        ranking1 = rankings[0]
        assertEquals(1, ranking1.rank)
        assertEquals(0, ranking1.score)
        assertEquals(listOf("Leonard"), ranking1.playerNames)

        val ranking2 = rankings[1]
        assertEquals(2, ranking2.rank)
        assertEquals(5, ranking2.score)
        assertEquals(listOf("Sheldon"), ranking2.playerNames)
    }
}