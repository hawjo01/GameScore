package net.hawkins.gamescore.game.type

import net.hawkins.gamescore.game.Game.Player
import org.junit.Assert.*
import org.junit.Test

class BasicScoreHighTest {

    val game = Games.getByName("Basic Scoring - High")

    @Test
    fun isValidScore_true() {
        assertTrue(game.isValidScore("0"))
        assertTrue(game.isValidScore("5"))
        assertTrue(game.isValidScore("-5"))
        assertTrue(game.isValidScore("1"))
        assertTrue(game.isValidScore("-1"))
    }

    @Test
    fun isValidScore_false() {
        assertFalse(game.isValidScore(""))
        assertFalse(game.isValidScore(".5"))
        assertFalse(game.isValidScore("a"))
    }

    @Test
    fun hasWinningThreshold() {
        assertFalse(game.hasWinningThreshold())
    }

    @Test
    fun findWinner_zeroPlayers() {
        val players: List<Player> = listOf()
        assertNull(game.findWinner(players))
    }

    @Test
    fun findWinner_differentScores() {
        val player1 = Player("foo")
        player1.addScore(20)

        val player2 = Player("bar")

        val players: List<Player> = listOf(player1, player2)

        val winner = game.findWinner(players)
        assertEquals(player1, winner)
    }

    @Test
    fun findWinner_HighScore() {
        val player1 = Player("foo")
        player1.addScore(20)

        val player2 = Player("bar")
        player2.addScore(50)

        val players: List<Player> = listOf(player1, player2)

        val winner = game.findWinner(players)
        assertEquals(player2, winner)
    }

    @Test
    fun findWinner_EqualScores() {
        val player1 = Player("foo")
        player1.addScore(20)

        val player2 = Player("bar")
        player2.addScore(20)

        val players: List<Player> = listOf(player1, player2)

        val winner = game.findWinner(players)
        assertNull(winner)
    }

    @Test
    fun highlightNegativeScore() {
        assertFalse(game.highlightNegativeScore())
    }
}