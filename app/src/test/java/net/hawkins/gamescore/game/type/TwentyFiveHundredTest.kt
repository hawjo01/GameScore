package net.hawkins.gamescore.game.type

import net.hawkins.gamescore.game.Game.Player
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class TwentyFiveHundredTest {

    val game = Games.getByName("Basic Scoring - Low")

    @Test
    fun isValidScore_true() {
        assertTrue(game.isValidScore("0"))
        assertTrue(game.isValidScore("5"))
        assertTrue(game.isValidScore("-5"))
    }

    @Test
    fun isValidScore_false() {
        assertFalse(game.isValidScore(""))
        assertFalse(game.isValidScore("1"))
        assertFalse(game.isValidScore("-1"))
        assertFalse(game.isValidScore(".5"))
        assertFalse(game.isValidScore("a"))
    }

    @Test
    fun hasWinningThreshold() {
        assertTrue(game.hasWinningThreshold())
    }

    @Test
    fun findWinner_zeroPlayers() {
        val players: List<Player> = mutableListOf()
        assertNull(game.findWinner(players))
    }

    @Test
    fun findWinner_differentScores() {
        val player1 = Player("foo")
        player1.addScore(20)

        val player2 = Player("bar")

        val players: List<Player> = listOf(player1, player2)

        assertNull(game.findWinner(players))
    }

    @Test
    fun findWinner_ThresholdNotMet() {
        val player1 = Player("foo")
        player1.addScore(20)

        val player2 = Player("bar")
        player2.addScore(50)

        val players: List<Player> = listOf(player1, player2)

        assertNull(game.findWinner(players))
    }

    @Test
    fun findWinner_ThresholdMet() {
        val player1 = Player("foo")
        player1.addScore(20)

        val player2 = Player("bar")
        player2.addScore(2500)

        val players: List<Player> = listOf(player1, player2)

        val winner = game.findWinner(players)
        assertEquals(player2, winner)
    }

    @Test
    fun findWinner_ThresholdExceeded() {
        val player1 = Player("foo")
        player1.addScore(2600)

        val player2 = Player("bar")
        player2.addScore(2505)

        val players: List<Player> = listOf(player1, player2)

        val winner = game.findWinner(players)
        assertEquals(player1, winner)
    }

    @Test
    fun findWinner_ThresholdMet_EqualsScores() {
        val player1 = Player("foo")
        player1.addScore(2600)

        val player2 = Player("bar")
        player2.addScore(2600)

        val players = listOf(player1, player2)

        val winner = game.findWinner(players)
        assertNull(winner)
    }

    @Test
    fun getScoreColor() {
        assertTrue(game.highlightNegativeScore())
    }
}