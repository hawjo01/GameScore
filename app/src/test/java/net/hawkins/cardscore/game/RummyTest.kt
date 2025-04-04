package net.hawkins.cardscore.game

import net.hawkins.cardscore.data.Player
import org.junit.Assert.*
import org.junit.Test

class RummyTest {

    @Test
    fun isValidScore_true() {
        val rummy = Rummy()
        assertTrue(rummy.isValidScore("0"))
        assertTrue(rummy.isValidScore("5"))
        assertTrue(rummy.isValidScore("-5"))
    }

    @Test
    fun isValidScore_false() {
        val rummy = Rummy()
        assertFalse(rummy.isValidScore(""))
        assertFalse(rummy.isValidScore("1"))
        assertFalse(rummy.isValidScore("-1"))
        assertFalse(rummy.isValidScore(".5"))
    }

    @Test
    fun hasWinningThreshold() {
        val rummy = Rummy()
        assertTrue(rummy.hasWinningThreshold())
    }

    @Test
    fun findWinner_zeroPlayers() {
        val rummy = Rummy()
        val players: List<Player> = mutableListOf<Player>()
        assertNull(rummy.findWinner(players))
    }

    @Test
    fun findWinner_differentScores() {
        val player1 = Player("foo")
        player1.addScore(20)

        val player2 = Player("bar")

        val players: List<Player> = listOf(player1, player2)
        val rummy = Rummy()

        assertNull(rummy.findWinner(players))
    }

    @Test
    fun findWinner_ThresholdNotMet() {
        val player1 = Player("foo")
        player1.addScore(20)

        val player2 = Player("bar")
        player2.addScore(50)

        val players: List<Player> = listOf(player1, player2)
        val rummy = Rummy()

        assertNull(rummy.findWinner(players))
    }

    @Test
    fun findWinner_ThresholdMet() {
        val player1 = Player("foo")
        player1.addScore(20)

        val player2 = Player("bar")
        player2.addScore(1500)

        val players: List<Player> = listOf(player1, player2)
        val rummy = Rummy()

        val winner = rummy.findWinner(players)
        assertEquals(player2, winner)
    }

    @Test
    fun findWinner_ThresholdExceeded() {
        val player1 = Player("foo")
        player1.addScore(1600)

        val player2 = Player("bar")
        player2.addScore(1505)

        val players: List<Player> = listOf(player1, player2)
        val rummy = Rummy()

        val winner = rummy.findWinner(players)
        assertEquals(player1, winner)
    }
}