package net.hawkins.gamescore.game

import androidx.compose.ui.graphics.Color
import net.hawkins.gamescore.data.Player
import org.junit.Assert
import org.junit.Test

class BasicScoreTest {

    @Test
    fun isValidScore_true() {
        val gameType = BasicScore()
        Assert.assertTrue(gameType.isValidScore("0"))
        Assert.assertTrue(gameType.isValidScore("5"))
        Assert.assertTrue(gameType.isValidScore("-5"))
        Assert.assertTrue(gameType.isValidScore("1"))
        Assert.assertTrue(gameType.isValidScore("-1"))
    }

    @Test
    fun isValidScore_false() {
        val gameType = BasicScore()
        Assert.assertFalse(gameType.isValidScore(""))
        Assert.assertFalse(gameType.isValidScore(".5"))
    }

    @Test
    fun hasWinningThreshold() {
        val gameType = BasicScore()
        Assert.assertFalse(gameType.hasWinningThreshold())
    }

    @Test
    fun findWinner_zeroPlayers() {
        val gameType = BasicScore()
        val players: List<Player> = mutableListOf()
        Assert.assertNull(gameType.findWinner(players))
    }

    @Test
    fun findWinner_differentScores() {
        val player1 = Player("foo")
        player1.addScore(20)

        val player2 = Player("bar")

        val players: List<Player> = listOf(player1, player2)
        val gameType = BasicScore()

        val winner = gameType.findWinner(players)
        Assert.assertEquals(player1, winner)
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
        Assert.assertEquals(player2, winner)
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
        Assert.assertNull(winner)
    }

    @Test
    fun getScoreColor() {
        val game = BasicScore()
        Assert.assertEquals(Color.Unspecified, game.getScoreColor(-10))
        Assert.assertEquals(Color.Unspecified, game.getScoreColor(0))
        Assert.assertEquals(Color.Unspecified, game.getScoreColor(10))
    }
}