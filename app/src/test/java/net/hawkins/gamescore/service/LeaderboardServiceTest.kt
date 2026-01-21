package net.hawkins.gamescore.service

import net.hawkins.gamescore.data.model.Game
import net.hawkins.gamescore.ui.gameplay.Player
import net.hawkins.gamescore.ui.gameplay.Score
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class LeaderboardServiceTest {

    val leaderboardService = LeaderboardService()
    lateinit var fiveCrowns: Game
    lateinit var twentyFiveHundred: Game

    @Before
    fun setUp() {
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

        twentyFiveHundred = Game(
            name = "2500",
            objective = Game.Objective(
                type = Game.Objective.Type.HIGH_SCORE,
                goal = 2500

            ),
            constraints = Game.Constraints(
                multipleOf = 5
            )
        )
    }

    @Test
    fun buildLeaderboard_LowScoring_TieForFirstWith3rd() {
        var sheldon = Player("Sheldon")
        var leonard = Player("Leonard")
        var rajesh = Player("Rajesh")
        var players = listOf(sheldon, leonard, rajesh)
        var leaderboard = leaderboardService.buildLeaderboard(fiveCrowns, players)

        assertNull(leaderboard.winner)
        assertEquals(fiveCrowns.name, leaderboard.gameName)
        var rankings = leaderboard.rankings
        assertFalse(rankings.isEmpty())

        assertEquals(1, rankings.size)
        var ranking1 = rankings[0]
        assertEquals(1, ranking1.rank)
        assertEquals(0, ranking1.score)
        assertEquals(listOf("Sheldon", "Leonard", "Rajesh"), ranking1.playerNames)

        sheldon = sheldon.copy(scores = listOf(Score(5)))
        leonard = leonard.copy(scores = listOf(Score(0)))
        rajesh = rajesh.copy(scores = listOf(Score(0)))
        players = listOf(sheldon, leonard, rajesh)
        leaderboard = leaderboardService.buildLeaderboard(fiveCrowns, players)
        assertEquals(fiveCrowns.name, leaderboard.gameName)
        rankings = leaderboard.rankings
        assertFalse(rankings.isEmpty())

        assertEquals(2, rankings.size)
        ranking1 = rankings[0]
        assertEquals(1, ranking1.rank)
        assertEquals(0, ranking1.score)
        assertEquals(listOf("Leonard", "Rajesh"), ranking1.playerNames)

        val ranking2 = rankings[1]
        assertEquals(3, ranking2.rank)
        assertEquals(5, ranking2.score)
        assertEquals(listOf("Sheldon"), ranking2.playerNames)
    }

    @Test
    fun buildLeaderboard_LowScoring() {
        var sheldon = Player("Sheldon")
        var leonard = Player("Leonard")
        var players = listOf(sheldon, leonard)
        var leaderboard = leaderboardService.buildLeaderboard(fiveCrowns, players)

        assertNull(leaderboard.winner)
        assertEquals(fiveCrowns.name, leaderboard.gameName)
        var rankings = leaderboard.rankings
        assertFalse(rankings.isEmpty())

        assertEquals(1, rankings.size)
        var ranking1 = rankings[0]
        assertEquals(1, ranking1.rank)
        assertEquals(0, ranking1.score)
        assertEquals(listOf("Sheldon", "Leonard"), ranking1.playerNames)

        sheldon = sheldon.copy(scores = listOf(Score(5)))
        leonard = leonard.copy(scores = listOf(Score(0)))
        players = listOf(sheldon, leonard)
        leaderboard = leaderboardService.buildLeaderboard(fiveCrowns, players)
        assertEquals(fiveCrowns.name, leaderboard.gameName)
        rankings = leaderboard.rankings
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

    @Test
    fun buildLeaderboard_HighScoring() {
        var sheldon = Player("Sheldon")
        var leonard = Player("Leonard")
        var players = listOf(sheldon, leonard)
        var leaderboard = leaderboardService.buildLeaderboard(twentyFiveHundred, players)

        assertNull(leaderboard.winner)
        assertEquals(twentyFiveHundred.name, leaderboard.gameName)
        var rankings = leaderboard.rankings
        assertFalse(rankings.isEmpty())

        assertEquals(1, rankings.size)
        var ranking1 = rankings[0]
        assertEquals(1, ranking1.rank)
        assertEquals(0, ranking1.score)
        assertEquals(listOf("Sheldon", "Leonard"), ranking1.playerNames)

        sheldon = sheldon.copy(scores = listOf(Score(5)))
        leonard = leonard.copy(scores = listOf(Score(0)))
        players = listOf(sheldon, leonard)
        leaderboard = leaderboardService.buildLeaderboard(twentyFiveHundred, players)
        assertEquals(twentyFiveHundred.name, leaderboard.gameName)
        rankings = leaderboard.rankings
        assertFalse(rankings.isEmpty())

        assertEquals(2, rankings.size)
        ranking1 = rankings[0]
        assertEquals(1, ranking1.rank)
        assertEquals(5, ranking1.score)
        assertEquals(listOf("Sheldon"), ranking1.playerNames)

        val ranking2 = rankings[1]
        assertEquals(2, ranking2.rank)
        assertEquals(0, ranking2.score)
        assertEquals(listOf("Leonard"), ranking2.playerNames)
    }

    @Test
    fun buildLeaderboard_HighScoring_TieForFirstWith3rd() {
        var sheldon = Player("Sheldon")
        var leonard = Player("Leonard")
        var howard = Player("Howard")
        var players = listOf(sheldon, leonard, howard)
        var leaderboard = leaderboardService.buildLeaderboard(twentyFiveHundred, players)

        assertNull(leaderboard.winner)
        assertEquals(twentyFiveHundred.name, leaderboard.gameName)
        var rankings = leaderboard.rankings
        assertFalse(rankings.isEmpty())

        assertEquals(1, rankings.size)
        var ranking1 = rankings[0]
        assertEquals(1, ranking1.rank)
        assertEquals(0, ranking1.score)
        assertEquals(listOf("Sheldon", "Leonard", "Howard"), ranking1.playerNames)

        sheldon = sheldon.copy(scores = listOf(Score(5)))
        leonard = leonard.copy(scores = listOf(Score(0)))
        howard = howard.copy(scores = listOf(Score(5)))
        players = listOf(sheldon, leonard, howard)
        leaderboard = leaderboardService.buildLeaderboard(twentyFiveHundred, players)
        assertEquals(twentyFiveHundred.name, leaderboard.gameName)
        rankings = leaderboard.rankings
        assertFalse(rankings.isEmpty())

        assertEquals(2, rankings.size)
        ranking1 = rankings[0]
        assertEquals(1, ranking1.rank)
        assertEquals(5, ranking1.score)
        assertEquals(listOf("Sheldon", "Howard"), ranking1.playerNames)

        val ranking2 = rankings[1]
        assertEquals(3, ranking2.rank)
        assertEquals(0, ranking2.score)
        assertEquals(listOf("Leonard"), ranking2.playerNames)
    }
}