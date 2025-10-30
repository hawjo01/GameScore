package net.hawkins.gamescore.game.type

import net.hawkins.gamescore.game.Player
import org.junit.Assert.*
import org.junit.Test

class BasicScoreLowTest {

    @Test
    fun isValidScore_true() {
        assertTrue(BasicScoreLow.isValidScore("0"))
        assertTrue(BasicScoreLow.isValidScore("5"))
        assertTrue(BasicScoreLow.isValidScore("-5"))
        assertTrue(BasicScoreLow.isValidScore("1"))
        assertTrue(BasicScoreLow.isValidScore("-1"))
    }

    @Test
    fun isValidScore_false() {
        assertFalse(BasicScoreLow.isValidScore(""))
        assertFalse(BasicScoreLow.isValidScore(".5"))
        assertFalse(BasicScoreLow.isValidScore("a"))
    }

    @Test
    fun hasWinningThreshold() {
        assertFalse(BasicScoreLow.hasWinningThreshold())
    }

    @Test
    fun findWinner_zeroPlayers() {
        val players: List<Player> = listOf()
        assertNull(BasicScoreLow.findWinner(players))
    }

    @Test
    fun findWinner_differentScores() {
        val player1 = Player("foo")
        player1.addScore(20)

        val player2 = Player("bar")

        val players: List<Player> = listOf(player1, player2)

        val winner = BasicScoreLow.findWinner(players)
        assertEquals(player2, winner)
    }

    @Test
    fun findWinner_LowScore() {
        val player1 = Player("foo")
        player1.addScore(20)

        val player2 = Player("bar")
        player2.addScore(50)

        val players: List<Player> = listOf(player1, player2)

        val winner = BasicScoreLow.findWinner(players)
        assertEquals(player1, winner)
    }

    @Test
    fun findWinner_EqualScores() {
        val player1 = Player("foo")
        player1.addScore(20)

        val player2 = Player("bar")
        player2.addScore(20)

        val players: List<Player> = listOf(player1, player2)

        val winner = BasicScoreLow.findWinner(players)
        assertNull(winner)
    }

    @Test
    fun highlightNegativeScore() {
        assertFalse(BasicScoreLow.highlightNegativeScore())
    }
}