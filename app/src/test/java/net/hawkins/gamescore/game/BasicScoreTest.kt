package net.hawkins.gamescore.game

import org.junit.Assert.*
import org.junit.Test

class BasicScoreTest {

    @Test
    fun isValidScore_true() {
        assertTrue(BasicScore.isValidScore("0"))
        assertTrue(BasicScore.isValidScore("5"))
        assertTrue(BasicScore.isValidScore("-5"))
        assertTrue(BasicScore.isValidScore("1"))
        assertTrue(BasicScore.isValidScore("-1"))
    }

    @Test
    fun isValidScore_false() {
        assertFalse(BasicScore.isValidScore(""))
        assertFalse(BasicScore.isValidScore(".5"))
        assertFalse(BasicScore.isValidScore("a"))
    }

    @Test
    fun hasWinningThreshold() {
        assertFalse(BasicScore.hasWinningThreshold())
    }

    @Test
    fun findWinner_zeroPlayers() {
        val players: List<Player> = listOf()
        assertNull(BasicScore.findWinner(players))
    }

    @Test
    fun findWinner_differentScores() {
        val player1 = Player("foo")
        player1.addScore(20)

        val player2 = Player("bar")

        val players: List<Player> = listOf(player1, player2)

        val winner = BasicScore.findWinner(players)
        assertEquals(player1, winner)
    }

    @Test
    fun findWinner_HighScore() {
        val player1 = Player("foo")
        player1.addScore(20)

        val player2 = Player("bar")
        player2.addScore(50)

        val players: List<Player> = listOf(player1, player2)

        val winner = BasicScore.findWinner(players)
        assertEquals(player2, winner)
    }

    @Test
    fun findWinner_EqualScores() {
        val player1 = Player("foo")
        player1.addScore(20)

        val player2 = Player("bar")
        player2.addScore(20)

        val players: List<Player> = listOf(player1, player2)

        val winner = BasicScore.findWinner(players)
        assertNull(winner)
    }

    @Test
    fun highlightNegativeScore() {
        assertFalse(BasicScore.highlightNegativeScore())
    }
}