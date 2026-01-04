package net.hawkins.gamescore.service.gameplay

import net.hawkins.gamescore.data.model.Game
import net.hawkins.gamescore.service.GamePlayService
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class BasicScoreLowTest : AbstractGamePlayTest() {

    val game = Game(
        name = "Basic Scoring - Low",
        constraints = Game.Constraints(
            equalHandSizes = false
        ),
        objective = Game.Objective(
            type = Game.Objective.Type.LOW_SCORE
        ),
    )

    @Test
    fun isValidScore_true() {
        val gamePlayService = GamePlayService(game)
        assertTrue(gamePlayService.isValidScore("0"))
        assertTrue(gamePlayService.isValidScore("5"))
        assertTrue(gamePlayService.isValidScore("-5"))
        assertTrue(gamePlayService.isValidScore("1"))
        assertTrue(gamePlayService.isValidScore("-1"))
    }

    @Test
    fun isValidScore_false() {
        val gamePlayService = GamePlayService(game)
        assertFalse(gamePlayService.isValidScore(""))
        assertFalse(gamePlayService.isValidScore(".5"))
        assertFalse(gamePlayService.isValidScore("a"))
    }

    @Test
    fun isManualWinner() {
        val gamePlayService = GamePlayService(game)
        assertTrue(gamePlayService.isManualWinner())
    }

    @Test
    fun findWinner_zeroPlayers() {
        val gamePlayService = GamePlayService(game)
        assertNull(gamePlayService.determineWinner(emptyList()))
    }

    @Test
    fun findWinner_differentScores() {
        val gamePlayService = GamePlayService(game)
        val player1 = createPlayer("Howard", listOf(20))
        val player2 = createPlayer("Rajesh")
        val players = listOf(player1, player2)

        val winner = gamePlayService.determineWinner(players)
        assertEquals(player2.name, winner)
    }

    @Test
    fun findWinner_LowScore() {
        val gamePlayService = GamePlayService(game)
        val player1 = createPlayer("Howard", listOf(20))
        val player2 = createPlayer("Rajesh", listOf(50))
        val players = listOf(player1, player2)

        val winner = gamePlayService.determineWinner(players)
        assertEquals(player1.name, winner)
    }

    @Test
    fun findWinner_EqualScores() {
        val gamePlayService = GamePlayService(game)
        val player1 = createPlayer("Howard", listOf(20))
        val player2 = createPlayer("Rajesh", listOf(20))
        val players = listOf(player1, player2)

        val winner = gamePlayService.determineWinner(players)
        assertNull(winner)
    }
}