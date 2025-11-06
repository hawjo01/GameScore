package net.hawkins.gamescore.game.type

import net.hawkins.gamescore.game.Game.Player
import org.junit.Assert.*
import org.junit.Test

class BasicScoreHighTest {

    @Test
    fun isValidScore_true() {
        assertTrue(BasicScoreHigh.isValidScore("0"))
        assertTrue(BasicScoreHigh.isValidScore("5"))
        assertTrue(BasicScoreHigh.isValidScore("-5"))
        assertTrue(BasicScoreHigh.isValidScore("1"))
        assertTrue(BasicScoreHigh.isValidScore("-1"))
    }

    @Test
    fun isValidScore_false() {
        assertFalse(BasicScoreHigh.isValidScore(""))
        assertFalse(BasicScoreHigh.isValidScore(".5"))
        assertFalse(BasicScoreHigh.isValidScore("a"))
    }

    @Test
    fun hasWinningThreshold() {
        assertFalse(BasicScoreHigh.hasWinningThreshold())
    }

    @Test
    fun findWinner_zeroPlayers() {
        val players: List<Player> = listOf()
        assertNull(BasicScoreHigh.findWinner(players))
    }

    @Test
    fun findWinner_differentScores() {
        val player1 = Player("foo")
        player1.addScore(20)

        val player2 = Player("bar")

        val players: List<Player> = listOf(player1, player2)

        val winner = BasicScoreHigh.findWinner(players)
        assertEquals(player1, winner)
    }

    @Test
    fun findWinner_HighScore() {
        val player1 = Player("foo")
        player1.addScore(20)

        val player2 = Player("bar")
        player2.addScore(50)

        val players: List<Player> = listOf(player1, player2)

        val winner = BasicScoreHigh.findWinner(players)
        assertEquals(player2, winner)
    }

    @Test
    fun findWinner_EqualScores() {
        val player1 = Player("foo")
        player1.addScore(20)

        val player2 = Player("bar")
        player2.addScore(20)

        val players: List<Player> = listOf(player1, player2)

        val winner = BasicScoreHigh.findWinner(players)
        assertNull(winner)
    }

    @Test
    fun highlightNegativeScore() {
        assertFalse(BasicScoreHigh.highlightNegativeScore())
    }
}