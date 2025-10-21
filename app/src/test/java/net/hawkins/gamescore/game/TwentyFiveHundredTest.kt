package net.hawkins.gamescore.game

import org.junit.Assert.*
import org.junit.Test

class TwentyFiveHundredTest {

    @Test
    fun isValidScore_true() {
        assertTrue(TwentyFiveHundred.isValidScore("0"))
        assertTrue(TwentyFiveHundred.isValidScore("5"))
        assertTrue(TwentyFiveHundred.isValidScore("-5"))
    }

    @Test
    fun isValidScore_false() {
        assertFalse(TwentyFiveHundred.isValidScore(""))
        assertFalse(TwentyFiveHundred.isValidScore("1"))
        assertFalse(TwentyFiveHundred.isValidScore("-1"))
        assertFalse(TwentyFiveHundred.isValidScore(".5"))
        assertFalse(TwentyFiveHundred.isValidScore("a"))
    }

    @Test
    fun hasWinningThreshold() {
        assertTrue(TwentyFiveHundred.hasWinningThreshold())
    }

    @Test
    fun findWinner_zeroPlayers() {
        val players: List<Player> = mutableListOf()
        assertNull(TwentyFiveHundred.findWinner(players))
    }

    @Test
    fun findWinner_differentScores() {
        val player1 = Player("foo")
        player1.addScore(20)

        val player2 = Player("bar")

        val players: List<Player> = listOf(player1, player2)

        assertNull(TwentyFiveHundred.findWinner(players))
    }

    @Test
    fun findWinner_ThresholdNotMet() {
        val player1 = Player("foo")
        player1.addScore(20)

        val player2 = Player("bar")
        player2.addScore(50)

        val players: List<Player> = listOf(player1, player2)

        assertNull(TwentyFiveHundred.findWinner(players))
    }

    @Test
    fun findWinner_ThresholdMet() {
        val player1 = Player("foo")
        player1.addScore(20)

        val player2 = Player("bar")
        player2.addScore(2500)

        val players: List<Player> = listOf(player1, player2)

        val winner = TwentyFiveHundred.findWinner(players)
        assertEquals(player2, winner)
    }

    @Test
    fun findWinner_ThresholdExceeded() {
        val player1 = Player("foo")
        player1.addScore(2600)

        val player2 = Player("bar")
        player2.addScore(2505)

        val players: List<Player> = listOf(player1, player2)

        val winner = TwentyFiveHundred.findWinner(players)
        assertEquals(player1, winner)
    }

    @Test
    fun findWinner_ThresholdMet_EqualsScores() {
        val player1 = Player("foo")
        player1.addScore(2600)

        val player2 = Player("bar")
        player2.addScore(2600)

        val players = listOf(player1, player2)

        val winner = TwentyFiveHundred.findWinner(players)
        assertNull(winner)
    }

    @Test
    fun getScoreColor() {
        assertTrue(TwentyFiveHundred.highlightNegativeScore())
    }
}