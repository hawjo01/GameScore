package net.hawkins.gamescore.game

import net.hawkins.gamescore.data.Player
import org.junit.Assert
import org.junit.Test

class TwentyFiveHundredTest {

    @Test
    fun isValidScore_true() {
        val game = TwentyFiveHundred()
        Assert.assertTrue(game.isValidScore("0"))
        Assert.assertTrue(game.isValidScore("5"))
        Assert.assertTrue(game.isValidScore("-5"))
    }

    @Test
    fun isValidScore_false() {
        val game = TwentyFiveHundred()
        Assert.assertFalse(game.isValidScore(""))
        Assert.assertFalse(game.isValidScore("1"))
        Assert.assertFalse(game.isValidScore("-1"))
        Assert.assertFalse(game.isValidScore(".5"))
    }

    @Test
    fun hasWinningThreshold() {
        val game = TwentyFiveHundred()
        Assert.assertTrue(game.hasWinningThreshold())
    }

    @Test
    fun findWinner_zeroPlayers() {
        val game = TwentyFiveHundred()
        val players: List<Player> = mutableListOf()
        Assert.assertNull(game.findWinner(players))
    }

    @Test
    fun findWinner_differentScores() {
        val player1 = Player("foo")
        player1.addScore(20)

        val player2 = Player("bar")

        val players: List<Player> = listOf(player1, player2)
        val game = TwentyFiveHundred()

        Assert.assertNull(game.findWinner(players))
    }

    @Test
    fun findWinner_ThresholdNotMet() {
        val player1 = Player("foo")
        player1.addScore(20)

        val player2 = Player("bar")
        player2.addScore(50)

        val players: List<Player> = listOf(player1, player2)
        val game = TwentyFiveHundred()

        Assert.assertNull(game.findWinner(players))
    }

    @Test
    fun findWinner_ThresholdMet() {
        val player1 = Player("foo")
        player1.addScore(20)

        val player2 = Player("bar")
        player2.addScore(2500)

        val players: List<Player> = listOf(player1, player2)
        val game = TwentyFiveHundred()

        val winner = game.findWinner(players)
        Assert.assertEquals(player2, winner)
    }

    @Test
    fun findWinner_ThresholdExceeded() {
        val player1 = Player("foo")
        player1.addScore(2600)

        val player2 = Player("bar")
        player2.addScore(2505)

        val players: List<Player> = listOf(player1, player2)
        val game = TwentyFiveHundred()

        val winner = game.findWinner(players)
        Assert.assertEquals(player1, winner)
    }

    @Test
    fun getScoreColor() {
        val game = TwentyFiveHundred()
        Assert.assertTrue(game.highlightNegativeScore())
    }
}