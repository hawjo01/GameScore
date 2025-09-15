package net.hawkins.gamescore.game

import net.hawkins.gamescore.data.Player
import org.junit.Assert.*
import org.junit.Test

class TwentyFiveHundredTest {

    @Test
    fun isValidScore_true() {
        val rummy = TwentyFiveHundred()
        assertTrue(rummy.isValidScore("0"))
        assertTrue(rummy.isValidScore("5"))
        assertTrue(rummy.isValidScore("-5"))
    }

    @Test
    fun isValidScore_false() {
        val rummy = TwentyFiveHundred()
        assertFalse(rummy.isValidScore(""))
        assertFalse(rummy.isValidScore("1"))
        assertFalse(rummy.isValidScore("-1"))
        assertFalse(rummy.isValidScore(".5"))
    }

    @Test
    fun hasWinningThreshold() {
        val rummy = TwentyFiveHundred()
        assertTrue(rummy.hasWinningThreshold())
    }

    @Test
    fun findWinner_zeroPlayers() {
        val rummy = TwentyFiveHundred()
        val players: List<Player> = mutableListOf()
        assertNull(rummy.findWinner(players))
    }

    @Test
    fun findWinner_differentScores() {
        val player1 = Player("foo")
        player1.addScore(20)

        val player2 = Player("bar")

        val players: List<Player> = listOf(player1, player2)
        val rummy = TwentyFiveHundred()

        assertNull(rummy.findWinner(players))
    }

    @Test
    fun findWinner_ThresholdNotMet() {
        val player1 = Player("foo")
        player1.addScore(20)

        val player2 = Player("bar")
        player2.addScore(50)

        val players: List<Player> = listOf(player1, player2)
        val rummy = TwentyFiveHundred()

        assertNull(rummy.findWinner(players))
    }

    @Test
    fun findWinner_ThresholdMet() {
        val player1 = Player("foo")
        player1.addScore(20)

        val player2 = Player("bar")
        player2.addScore(2500)

        val players: List<Player> = listOf(player1, player2)
        val rummy = TwentyFiveHundred()

        val winner = rummy.findWinner(players)
        assertEquals(player2, winner)
    }

    @Test
    fun findWinner_ThresholdExceeded() {
        val player1 = Player("foo")
        player1.addScore(2600)

        val player2 = Player("bar")
        player2.addScore(2505)

        val players: List<Player> = listOf(player1, player2)
        val rummy = TwentyFiveHundred()

        val winner = rummy.findWinner(players)
        assertEquals(player1, winner)
    }
}