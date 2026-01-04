package net.hawkins.gamescore.service

import androidx.compose.ui.graphics.Color
import net.hawkins.gamescore.data.model.Game
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GamePlayServiceTest {

    lateinit var sevens: Game
    lateinit var eights: Game

    @Before
    fun setUp() {
        sevens = Game(
            name = "Sevens",
            constraints = Game.Constraints(multipleOf = 7)
        )

        eights = Game(
            name = "Eights",
            objective = Game.Objective(goal = 80),
            color = Game.Colors(negativeScore = Game.Colors.Color.RED)
        )
    }

    @Test
    fun validScore_MultipleOfSeven() {
        val gamePlayService = GamePlayService(sevens)

        assertFalse(gamePlayService.isValidScore("a"))
        assertFalse(gamePlayService.isValidScore("-6"))
        assertFalse(gamePlayService.isValidScore("-6"))

        assertTrue(gamePlayService.isValidScore("-7"))
        assertTrue(gamePlayService.isValidScore("0"))
        assertTrue(gamePlayService.isValidScore("7"))
        assertTrue(gamePlayService.isValidScore("14"))
    }

    @Test
    fun validScore_NoScoreModulus() {
        val gamePlayService = GamePlayService(eights)

        assertFalse(gamePlayService.isValidScore("a"))
        assertFalse(gamePlayService.isValidScore("-"))
        assertFalse(gamePlayService.isValidScore(".5"))

        assertTrue(gamePlayService.isValidScore("-1"))
        assertTrue(gamePlayService.isValidScore("0"))
        assertTrue(gamePlayService.isValidScore("1"))
        assertTrue(gamePlayService.isValidScore("2"))
        assertTrue(gamePlayService.isValidScore("3"))
        assertTrue(gamePlayService.isValidScore("4"))
        assertTrue(gamePlayService.isValidScore("5"))
        assertTrue(gamePlayService.isValidScore("6"))
        assertTrue(gamePlayService.isValidScore("7"))
        assertTrue(gamePlayService.isValidScore("8"))
        assertTrue(gamePlayService.isValidScore("9"))
        assertTrue(gamePlayService.isValidScore("10"))
    }

    @Test
    fun isValidScore_PositiveOnly() {
        val game = Game(name = "Positive Only", constraints = Game.Constraints(positiveOnly = true))
        val gamePlayService = GamePlayService(game)

        assertFalse(gamePlayService.isValidScore("-1"))
        assertTrue(gamePlayService.isValidScore("0"))
        assertTrue(gamePlayService.isValidScore("1"))
    }

    @Test
    fun buildScore_GreenRed() {
        val game = Game(
            name = "Green Red",
            color = Game.Colors(
                positiveScore = Game.Colors.Color.GREEN,
                negativeScore = Game.Colors.Color.RED
            )
        )
        val gamePlayService = GamePlayService(game)

        val negativeScore = gamePlayService.buildScore(-5)
        assertEquals(-5, negativeScore.value)
        assertEquals(Color.Red, negativeScore.color)

        val positiveScore = gamePlayService.buildScore(5)
        assertEquals(5, positiveScore.value)
        assertEquals(Color.Green, positiveScore.color)
    }

    @Test
    fun buildScore_DefaultColors() {
        val game = Game(name = "Default Colors")
        val gamePlayService = GamePlayService(game)

        val negativeScore = gamePlayService.buildScore(-5)
        assertEquals(-5, negativeScore.value)
        assertEquals(Color.Unspecified, negativeScore.color)

        val positiveScore = gamePlayService.buildScore(5)
        assertEquals(5, positiveScore.value)
        assertEquals(Color.Unspecified, positiveScore.color)
    }

    @Test
    fun buildScore_RedGreen() {
        val game = Game(
            name = "Red Green",
            color = Game.Colors(
                positiveScore = Game.Colors.Color.RED,
                negativeScore = Game.Colors.Color.GREEN
            )
        )
        val gamePlayService = GamePlayService(game)

        val negativeScore = gamePlayService.buildScore(-5)
        assertEquals(-5, negativeScore.value)
        assertEquals(Color.Green, negativeScore.color)

        val positiveScore = gamePlayService.buildScore(5)
        assertEquals(5, positiveScore.value)
        assertEquals(Color.Red, positiveScore.color)
    }
}