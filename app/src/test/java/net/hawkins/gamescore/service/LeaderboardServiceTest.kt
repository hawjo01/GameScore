package net.hawkins.gamescore.service

import net.hawkins.gamescore.TestData
import net.hawkins.gamescore.ui.gameplay.Player
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Test

class LeaderboardServiceTest {

    val leaderboardService = LeaderboardService()

    @Test
    fun buildLeaderboard_LowScoring_TieForFirstWith3rd() {
        val fiveCrowns = TestData.getFiveCrowns()
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

        sheldon = TestData.addScores(sheldon, 5)
        leonard = TestData.addScores(leonard,0)
        rajesh = TestData.addScores(rajesh,0)
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
        val fiveCrowns = TestData.getFiveCrowns()
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

        sheldon = TestData.addScores(sheldon,5)
        leonard = TestData.addScores(leonard,0)
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
        val twentyFiveHundred = TestData.getTwentyFiveHundred()
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

        sheldon = TestData.addScores(sheldon, 5)
        leonard = TestData.addScores(leonard, 0)
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
        val twentyFiveHundred = TestData.getTwentyFiveHundred()
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

        sheldon = TestData.addScores(sheldon, 5)
        leonard = TestData.addScores(leonard,0)
        howard = TestData.addScores(howard,5)
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