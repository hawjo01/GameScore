package net.hawkins.gamescore.game.gameplay

import net.hawkins.gamescore.data.model.Game
import net.hawkins.gamescore.game.GamePlay
import net.hawkins.gamescore.ui.gameplay.Player
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class TwentyFiveHundredTest {

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
        val gamePlay = GamePlay(game)
        assertTrue(gamePlay.isValidScore("0"))
        assertTrue(gamePlay.isValidScore("5"))
        assertTrue(gamePlay.isValidScore("-5"))
    }

    @Test
    fun isValidScore_false() {
        val gamePlay = GamePlay(game)
        assertFalse(gamePlay.isValidScore(""))
        assertFalse(gamePlay.isValidScore("1"))
        assertFalse(gamePlay.isValidScore("-1"))
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
        val player1 = Player("Sheldon", listOf(20))
        val player2 = Player("Penny")
        val players = listOf(player1, player2)

        assertNull(gamePlay.determineWinner(players))
    }

    @Test
    fun findWinner_ThresholdNotMet() {
        val gamePlay = GamePlay(game)
        val player1 = Player("Sheldon", listOf(20))
        val player2 = Player("Penny", listOf(50))
        val players = listOf(player1, player2)

        assertNull(gamePlay.determineWinner(players))
    }

    @Test
    fun findWinner_ThresholdMet() {
        val gamePlay = GamePlay(game)
        val player1 = Player("Sheldon", listOf(20))
        val player2 = Player("Penny", listOf(2500))
        val players = listOf(player1, player2)

        val winner = gamePlay.determineWinner(players)
        assertEquals(player2.name, winner)
    }

    @Test
    fun findWinner_ThresholdMet_UnequalHands() {
        val gamePlay = GamePlay(game)
        val player1 = Player("Sheldon", listOf(20))
        val player2 = Player("Penny", listOf(1000, 1500))
        val players = listOf(player1, player2)

        val winner = gamePlay.determineWinner(players)
        assertNull(winner)
    }

    @Test
    fun findWinner_ThresholdExceeded() {
        val gamePlay = GamePlay(game)
        val player1 = Player("Sheldon", listOf(2600))
        val player2 = Player("Penny", listOf(2505))
        val players = listOf(player1, player2)

        val winner = gamePlay.determineWinner(players)
        assertEquals(player1.name, winner)
    }

    @Test
    fun findWinner_ThresholdMet_EqualsScores() {
        val gamePlay = GamePlay(game)
        val player1 = Player("Sheldon", listOf(2600))
        val player2 = Player("Penny", listOf(2600))
        val players = listOf(player1, player2)

        val winner = gamePlay.determineWinner(players)
        assertNull(winner)
    }
}