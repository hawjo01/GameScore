package net.hawkins.cardscore.game

import net.hawkins.cardscore.data.Player
import org.junit.Assert.*
import org.junit.Test

class BasicScoreTest {

    @Test
    fun isValidScore_true() {
        val gameType = BasicScore()
        assertTrue(gameType.isValidScore("0"))
        assertTrue(gameType.isValidScore("5"))
        assertTrue(gameType.isValidScore("-5"))
        assertTrue(gameType.isValidScore("1"))
        assertTrue(gameType.isValidScore("-1"))
    }

    @Test
    fun isValidScore_false() {
        val gameType = BasicScore()
        assertFalse(gameType.isValidScore(""))
        assertFalse(gameType.isValidScore(".5"))
    }

    @Test
    fun hasWinningThreshold() {
        val gameType = BasicScore()
        assertFalse(gameType.hasWinningThreshold())
    }

    @Test
    fun findWinner_zeroPlayers() {
        val gameType = BasicScore()
        val players: List<Player> = mutableListOf()
        assertNull(gameType.findWinner(players))
    }

    @Test
    fun findWinner_differentScores() {
        val player1 = Player("foo")
        player1.addScore(20)

        val player2 = Player("bar")

        val players: List<Player> = listOf(player1, player2)
        val gameType = BasicScore()

        val winner = gameType.findWinner(players)
        assertEquals(player1, winner)
    }

    @Test
    fun findWinner_HighScore() {
        val player1 = Player("foo")
        player1.addScore(20)

        val player2 = Player("bar")
        player2.addScore(50)

        val players: List<Player> = listOf(player1, player2)
        val gameType = BasicScore()

        val winner = gameType.findWinner(players)
        assertEquals(player2, winner)
    }

    @Test
    fun findWinner_EqualScores() {
        val player1 = Player("foo")
        player1.addScore(20)

        val player2 = Player("bar")
        player2.addScore(20)

        val players: List<Player> = listOf(player1, player2)
        val gameType = BasicScore()

        val winner = gameType.findWinner(players)
        assertNull(winner)
    }
}