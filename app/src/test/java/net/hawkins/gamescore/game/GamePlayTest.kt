package net.hawkins.gamescore.game

import net.hawkins.gamescore.data.model.Game
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class GamePlayTest {

    private val Seven = Game(
        name = "Sevens",
        constraints = Game.Constraints(multipleOf = 7)
    )

    private val Eight = Game(
        name = "Eights",
        objective = Game.Objective(goal = 80),
        constraints = Game.Constraints(multipleOf = 8),
        color = Game.Colors(negativeScore = Game.Colors.Color.RED)
    )

    @Test
    fun getGameName() {
        val gamePlay = GamePlay(Seven, listOf())
        assertEquals(Seven.name, gamePlay.getGameName())
    }

    @Test
    fun players() {
        val gamePlay = GamePlay(Seven, listOf("Sheldon", "Leonard"))
        assertEquals(2, gamePlay.players.size)
        assertEquals("Sheldon", gamePlay.players[0].name)
        assertEquals("Leonard", gamePlay.players[1].name)
    }

    @Test
    fun validScore() {
        val gamePlay = GamePlay(Seven, listOf())

        assertFalse(gamePlay.isValidScore("a"))
        assertFalse(gamePlay.isValidScore("-6"))
        assertFalse(gamePlay.isValidScore("-6"))

        assertTrue(gamePlay.isValidScore("-7"))
        assertTrue(gamePlay.isValidScore("0"))
        assertTrue(gamePlay.isValidScore("7"))
    }

    @Test
    fun resetGame() {
        val gamePlay = GamePlay(Seven, listOf("Sheldon", "Leonard"))
        gamePlay.players[0].addScore(10)
        gamePlay.players[1].addScore(20)

        val winner = gamePlay.determineWinner()
        assertEquals("Leonard", winner?.name)
        assertEquals("Leonard", gamePlay.getWinner()?.name)

        gamePlay.resetGame()
        assertTrue(gamePlay.players[0].scores.isEmpty())
        assertTrue(gamePlay.players[1].scores.isEmpty())
        assertNull(gamePlay.getWinner())
    }

    @Test
    fun highlightNegativeScore() {
        val sevens = GamePlay(Seven, listOf("Sheldon", "Leonard"))
        assertFalse(sevens.highlightNegativeScore())

        val eights = GamePlay(Eight, listOf("Howard", "Rajesh"))
        assertTrue(eights.highlightNegativeScore())
    }

    @Test
    fun hasWinningThreshold() {
        val sevens = GamePlay(Seven, listOf("Sheldon", "Leonard"))
        assertFalse(sevens.hasWinningThreshold())

        val eights = GamePlay(Eight, listOf("Howard", "Rajesh"))
        assertTrue(eights.hasWinningThreshold())
    }

    @Test
    fun numberOfRounds() {
        val sevens = GamePlay(Seven, listOf("Sheldon", "Leonard"))
        assertEquals(0, sevens.numberOfRounds())

        sevens.players[0].addScore(7)
        assertEquals(1, sevens.numberOfRounds())

        sevens.players[1].addScore(14)
        assertEquals(1, sevens.numberOfRounds())

        sevens.players[1].addScore(21)
        assertEquals(2, sevens.numberOfRounds())
    }
}