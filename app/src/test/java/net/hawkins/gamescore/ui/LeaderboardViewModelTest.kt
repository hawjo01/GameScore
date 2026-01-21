package net.hawkins.gamescore.ui

import net.hawkins.gamescore.data.model.Game
import net.hawkins.gamescore.ui.gameplay.Player
import net.hawkins.gamescore.ui.gameplay.Score
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
    lateinit var fiveCrowns: Game

    @Before
    fun setUp() {
        viewModel = LeaderboardViewModel()

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

        player1 = player1.copy(scores = listOf(Score(5)))
        player2 = player2.copy(scores = listOf(Score(0)))
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

        var ranking2 = rankings[1]
        assertEquals(2, ranking2.rank)
        assertEquals(5, ranking2.score)
        assertEquals(listOf("Sheldon"), ranking2.playerNames)
    }
}