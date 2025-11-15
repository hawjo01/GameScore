package net.hawkins.gamescore.game.gameplay

import net.hawkins.gamescore.data.model.Game
import net.hawkins.gamescore.game.GamePlay
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class BasicScoreHighTest {

    val game = Game(
        name = "Basic Scoring - High",
        constraints = Game.Constraints(
            equalHandSizes = false
        ),
        objective = Game.Objective(
            type = Game.Objective.Type.HIGH_SCORE,
        ),
        color = Game.Colors()
    )

    @Test
    fun isValidScore_true() {
        val gamePlay = GamePlay(game, listOf())
        assertTrue(gamePlay.isValidScore("0"))
        assertTrue(gamePlay.isValidScore("5"))
        assertTrue(gamePlay.isValidScore("-5"))
        assertTrue(gamePlay.isValidScore("1"))
        assertTrue(gamePlay.isValidScore("-1"))
    }

    @Test
    fun isValidScore_false() {
        val gamePlay = GamePlay(game, listOf())
        assertFalse(gamePlay.isValidScore(""))
        assertFalse(gamePlay.isValidScore(".5"))
        assertFalse(gamePlay.isValidScore("a"))
    }

    @Test
    fun hasWinningThreshold() {
        val gamePlay = GamePlay(game, listOf())
        assertFalse(gamePlay.hasWinningThreshold())
    }

    @Test
    fun findWinner_zeroPlayers() {
        val gamePlay = GamePlay(game, listOf())
        assertNull(gamePlay.determineWinner())
    }

    @Test
    fun findWinner_differentScores() {
        val gamePlay = GamePlay(game, listOf("Howard", "Rajesh"))
        val player1 = gamePlay.players[0]
        player1.addScore(20)

        val winner = gamePlay.determineWinner()
        assertEquals(player1, winner)
    }

    @Test
    fun findWinner_HighScore() {
        val gamePlay = GamePlay(game, listOf("Howard", "Rajesh"))
        val player1 = gamePlay.players[0]
        player1.addScore(20)

        val player2 = gamePlay.players[1]
        player2.addScore(50)

        val winner = gamePlay.determineWinner()
        assertEquals(player2, winner)
    }

    @Test
    fun findWinner_EqualScores() {
        val gamePlay = GamePlay(game, listOf("Howard", "Rajesh"))
        val player1 = gamePlay.players[0]
        player1.addScore(20)

        val player2 = gamePlay.players[1]
        player2.addScore(20)

        val winner = gamePlay.determineWinner()
        assertNull(winner)
    }

    @Test
    fun highlightNegativeScore() {
        val gamePlay = GamePlay(game, listOf())
        assertFalse(gamePlay.highlightNegativeScore())
    }
}