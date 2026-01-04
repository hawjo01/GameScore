package net.hawkins.gamescore.service.gameplay

import net.hawkins.gamescore.data.model.Game
import net.hawkins.gamescore.service.GamePlayService
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class TwentyFiveHundredTest : AbstractGamePlayTest() {

    val game = Game(
        name = "2500",
        constraints = Game.Constraints(
            multipleOf = 5,
            equalHandSizes = true
        ),
        objective = Game.Objective(
            goal = 2500
        ),
        color = Game.Colors(
            negativeScore = Game.Colors.Color.RED
        )
    )

    @Test
    fun isValidScore_true() {
        val gamePlayService = GamePlayService(game)
        assertTrue(gamePlayService.isValidScore("0"))
        assertTrue(gamePlayService.isValidScore("5"))
        assertTrue(gamePlayService.isValidScore("-5"))
    }

    @Test
    fun isValidScore_false() {
        val gamePlayService = GamePlayService(game)
        assertFalse(gamePlayService.isValidScore(""))
        assertFalse(gamePlayService.isValidScore("1"))
        assertFalse(gamePlayService.isValidScore("-1"))
        assertFalse(gamePlayService.isValidScore(".5"))
        assertFalse(gamePlayService.isValidScore("a"))
    }

    @Test
    fun isManualWinner() {
        val gamePlayService = GamePlayService(game)
        assertFalse(gamePlayService.isManualWinner())
    }

    @Test
    fun findWinner_zeroPlayers() {
        val gamePlayService = GamePlayService(game)
        assertNull(gamePlayService.determineWinner(emptyList()))
    }

    @Test
    fun findWinner_differentScores() {
        val gamePlayService = GamePlayService(game)
        val player1 = createPlayer("Sheldon", listOf(20))
        val player2 = createPlayer("Penny")
        val players = listOf(player1, player2)

        assertNull(gamePlayService.determineWinner(players))
    }

    @Test
    fun findWinner_ThresholdNotMet() {
        val gamePlayService = GamePlayService(game)
        val player1 = createPlayer("Sheldon", listOf(20))
        val player2 = createPlayer("Penny", listOf(50))
        val players = listOf(player1, player2)

        assertNull(gamePlayService.determineWinner(players))
    }

    @Test
    fun findWinner_ThresholdMet() {
        val gamePlayService = GamePlayService(game)
        val player1 = createPlayer("Sheldon", listOf(20))
        val player2 = createPlayer("Penny", listOf(2500))
        val players = listOf(player1, player2)

        val winner = gamePlayService.determineWinner(players)
        assertEquals(player2.name, winner)
    }

    @Test
    fun findWinner_ThresholdMet_UnequalHands() {
        val gamePlayService = GamePlayService(game)
        val player1 = createPlayer("Sheldon", listOf(20))
        val player2 = createPlayer("Penny", listOf(1000, 1500))
        val players = listOf(player1, player2)

        val winner = gamePlayService.determineWinner(players)
        assertNull(winner)
    }

    @Test
    fun findWinner_ThresholdExceeded() {
        val gamePlayService = GamePlayService(game)
        val player1 = createPlayer("Sheldon", listOf(2600))
        val player2 = createPlayer("Penny", listOf(2505))
        val players = listOf(player1, player2)

        val winner = gamePlayService.determineWinner(players)
        assertEquals(player1.name, winner)
    }

    @Test
    fun findWinner_ThresholdMet_EqualsScores() {
        val gamePlayService = GamePlayService(game)
        val player1 = createPlayer("Sheldon", listOf(2600))
        val player2 = createPlayer("Penny", listOf(2600))
        val players = listOf(player1, player2)

        val winner = gamePlayService.determineWinner(players)
        assertNull(winner)
    }
}