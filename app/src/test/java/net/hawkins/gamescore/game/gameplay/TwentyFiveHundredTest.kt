package net.hawkins.gamescore.game.gameplay

import net.hawkins.gamescore.data.GameRepository
import net.hawkins.gamescore.game.GamePlay
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class TwentyFiveHundredTest {

    val game = GameRepository.getByName("2500")

    @Test
    fun isValidScore_true() {
        val gamePlay = GamePlay(game, listOf())
        assertTrue(gamePlay.isValidScore("0"))
        assertTrue(gamePlay.isValidScore("5"))
        assertTrue(gamePlay.isValidScore("-5"))
    }

    @Test
    fun isValidScore_false() {
        val gamePlay = GamePlay(game, listOf())
        assertFalse(gamePlay.isValidScore(""))
        assertFalse(gamePlay.isValidScore("1"))
        assertFalse(gamePlay.isValidScore("-1"))
        assertFalse(gamePlay.isValidScore(".5"))
        assertFalse(gamePlay.isValidScore("a"))
    }

    @Test
    fun hasWinningThreshold() {
        val gamePlay = GamePlay(game, listOf())
        assertTrue(gamePlay.hasWinningThreshold())
    }

    @Test
    fun findWinner_zeroPlayers() {
        val gamePlay = GamePlay(game, listOf())
        assertNull(gamePlay.determineWinner())
    }

    @Test
    fun findWinner_differentScores() {
        val gamePlay = GamePlay(game, listOf("Sheldon", "Penny"))
        val player1 = gamePlay.players[0]
        player1.addScore(20)

        assertNull(gamePlay.determineWinner())
    }

    @Test
    fun findWinner_ThresholdNotMet() {
        val gamePlay = GamePlay(game, listOf("Sheldon", "Penny"))
        val player1 = gamePlay.players[0]
        player1.addScore(20)

        val player2 = gamePlay.players[1]
        player2.addScore(50)

        assertNull(gamePlay.determineWinner())
    }

    @Test
    fun findWinner_ThresholdMet() {
        val gamePlay = GamePlay(game, listOf("Sheldon", "Penny"))
        val player1 = gamePlay.players[0]
        player1.addScore(20)

        val player2 = gamePlay.players[1]
        player2.addScore(2500)

        val winner = gamePlay.determineWinner()
        assertEquals(player2, winner)
    }

    @Test
    fun findWinner_ThresholdMet_UnequalHands() {
        val gamePlay = GamePlay(game, listOf("Sheldon", "Penny"))
        val player1 = gamePlay.players[0]
        player1.addScore(20)

        val player2 = gamePlay.players[1]
        player2.addScore(1000)
        player2.addScore(1500)

        val winner = gamePlay.determineWinner()
        assertNull(winner)
    }

    @Test
    fun findWinner_ThresholdExceeded() {
        val gamePlay = GamePlay(game, listOf("Sheldon", "Penny"))
        val player1 = gamePlay.players[0]
        player1.addScore(2600)

        val player2 = gamePlay.players[1]
        player2.addScore(2505)

        val winner = gamePlay.determineWinner()
        assertEquals(player1, winner)
    }

    @Test
    fun findWinner_ThresholdMet_EqualsScores() {
        val gamePlay = GamePlay(game, listOf("Sheldon", "Penny"))
        val player1 = gamePlay.players[0]
        player1.addScore(2600)

        val player2 = gamePlay.players[1]
        player2.addScore(2600)

        val players = listOf(player1, player2)

        val winner = gamePlay.determineWinner()
        assertNull(winner)
    }

    @Test
    fun getScoreColor() {
        val gamePlay = GamePlay(game, listOf())
        assertTrue(gamePlay.highlightNegativeScore())
    }
}