package net.hawkins.gamescore.game.type

import net.hawkins.gamescore.game.Player
import org.junit.Assert
import org.junit.Test

class TwentyFiveHundredTest {

    @Test
    fun isValidScore_true() {
        Assert.assertTrue(TwentyFiveHundred.isValidScore("0"))
        Assert.assertTrue(TwentyFiveHundred.isValidScore("5"))
        Assert.assertTrue(TwentyFiveHundred.isValidScore("-5"))
    }

    @Test
    fun isValidScore_false() {
        Assert.assertFalse(TwentyFiveHundred.isValidScore(""))
        Assert.assertFalse(TwentyFiveHundred.isValidScore("1"))
        Assert.assertFalse(TwentyFiveHundred.isValidScore("-1"))
        Assert.assertFalse(TwentyFiveHundred.isValidScore(".5"))
        Assert.assertFalse(TwentyFiveHundred.isValidScore("a"))
    }

    @Test
    fun hasWinningThreshold() {
        Assert.assertTrue(TwentyFiveHundred.hasWinningThreshold())
    }

    @Test
    fun findWinner_zeroPlayers() {
        val players: List<Player> = mutableListOf()
        Assert.assertNull(TwentyFiveHundred.findWinner(players))
    }

    @Test
    fun findWinner_differentScores() {
        val player1 = Player("foo")
        player1.addScore(20)

        val player2 = Player("bar")

        val players: List<Player> = listOf(player1, player2)

        Assert.assertNull(TwentyFiveHundred.findWinner(players))
    }

    @Test
    fun findWinner_ThresholdNotMet() {
        val player1 = Player("foo")
        player1.addScore(20)

        val player2 = Player("bar")
        player2.addScore(50)

        val players: List<Player> = listOf(player1, player2)

        Assert.assertNull(TwentyFiveHundred.findWinner(players))
    }

    @Test
    fun findWinner_ThresholdMet() {
        val player1 = Player("foo")
        player1.addScore(20)

        val player2 = Player("bar")
        player2.addScore(2500)

        val players: List<Player> = listOf(player1, player2)

        val winner = TwentyFiveHundred.findWinner(players)
        Assert.assertEquals(player2, winner)
    }

    @Test
    fun findWinner_ThresholdExceeded() {
        val player1 = Player("foo")
        player1.addScore(2600)

        val player2 = Player("bar")
        player2.addScore(2505)

        val players: List<Player> = listOf(player1, player2)

        val winner = TwentyFiveHundred.findWinner(players)
        Assert.assertEquals(player1, winner)
    }

    @Test
    fun findWinner_ThresholdMet_EqualsScores() {
        val player1 = Player("foo")
        player1.addScore(2600)

        val player2 = Player("bar")
        player2.addScore(2600)

        val players = listOf(player1, player2)

        val winner = TwentyFiveHundred.findWinner(players)
        Assert.assertNull(winner)
    }

    @Test
    fun getScoreColor() {
        Assert.assertTrue(TwentyFiveHundred.highlightNegativeScore())
    }
}