package net.hawkins.gamescore.game

import net.hawkins.gamescore.data.model.Game
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class GamePlayTest {

    private val seven = Game(
        name = "Sevens",
        constraints = Game.Constraints(multipleOf = 7)
    )

    private val eight = Game(
        name = "Eights",
        objective = Game.Objective(goal = 80),
        color = Game.Colors(negativeScore = Game.Colors.Color.RED)
    )

    @Test
    fun validScore_MultipleOfSeven() {
        val gamePlay = GamePlay(seven)

        assertFalse(gamePlay.isValidScore("a"))
        assertFalse(gamePlay.isValidScore("-6"))
        assertFalse(gamePlay.isValidScore("-6"))

        assertTrue(gamePlay.isValidScore("-7"))
        assertTrue(gamePlay.isValidScore("0"))
        assertTrue(gamePlay.isValidScore("7"))
        assertTrue(gamePlay.isValidScore("14"))
    }

    @Test
    fun validScore_NoScoreModulus() {
        val gamePlay = GamePlay(eight)

        assertFalse(gamePlay.isValidScore("a"))
        assertFalse(gamePlay.isValidScore("-"))
        assertFalse(gamePlay.isValidScore(".5"))

        assertTrue(gamePlay.isValidScore("-1"))
        assertTrue(gamePlay.isValidScore("0"))
        assertTrue(gamePlay.isValidScore("1"))
        assertTrue(gamePlay.isValidScore("2"))
        assertTrue(gamePlay.isValidScore("3"))
        assertTrue(gamePlay.isValidScore("4"))
        assertTrue(gamePlay.isValidScore("5"))
        assertTrue(gamePlay.isValidScore("6"))
        assertTrue(gamePlay.isValidScore("7"))
        assertTrue(gamePlay.isValidScore("8"))
        assertTrue(gamePlay.isValidScore("9"))
        assertTrue(gamePlay.isValidScore("10"))
    }
}