package net.hawkins.gamescore.game.gameplay

import net.hawkins.gamescore.data.model.Game
import net.hawkins.gamescore.game.GamePlay
import net.hawkins.gamescore.ui.gameplay.Player
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
        val gamePlay = GamePlay(game)
        assertTrue(gamePlay.isValidScore("0"))
        assertTrue(gamePlay.isValidScore("5"))
        assertTrue(gamePlay.isValidScore("-5"))
        assertTrue(gamePlay.isValidScore("1"))
        assertTrue(gamePlay.isValidScore("-1"))
    }

    @Test
    fun isValidScore_false() {
        val gamePlay = GamePlay(game)
        assertFalse(gamePlay.isValidScore(""))
        assertFalse(gamePlay.isValidScore(".5"))
        assertFalse(gamePlay.isValidScore("a"))
    }

    @Test
    fun findWinner_zeroPlayers() {
        val gamePlay = GamePlay(game)
        assertNull(gamePlay.determineWinner(emptyList()))
    }

    @Test
    fun findWinner_differentScores() {
        val gamePlay = GamePlay(game)
        val player = Player("Howard", listOf(20))

        val winner = gamePlay.determineWinner(listOf(player))
        assertEquals(player.name, winner)
    }

    @Test
    fun findWinner_HighScore() {
        val gamePlay = GamePlay(game)
        val player1 = Player("Howard", listOf(20))
        val player2 = Player("Rajesh", listOf(50))
        val players = listOf(player1, player2)

        val winner = gamePlay.determineWinner(players)
        assertEquals(player2.name, winner)
    }

    @Test
    fun findWinner_EqualScores() {
        val gamePlay = GamePlay(game)
        val player1 = Player("Howard", listOf(20))
        val player2 = Player("Rajesh", listOf(20))
        val players = listOf(player1, player2)

        val winner = gamePlay.determineWinner(players)
        assertNull(winner)
    }
}