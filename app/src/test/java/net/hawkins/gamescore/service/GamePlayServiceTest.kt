package net.hawkins.gamescore.service

import net.hawkins.gamescore.data.model.Game
import org.junit.Assert
import org.junit.Test

class GamePlayServiceTest {

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
        val gamePlayService = GamePlayService(seven)

        Assert.assertFalse(gamePlayService.isValidScore("a"))
        Assert.assertFalse(gamePlayService.isValidScore("-6"))
        Assert.assertFalse(gamePlayService.isValidScore("-6"))

        Assert.assertTrue(gamePlayService.isValidScore("-7"))
        Assert.assertTrue(gamePlayService.isValidScore("0"))
        Assert.assertTrue(gamePlayService.isValidScore("7"))
        Assert.assertTrue(gamePlayService.isValidScore("14"))
    }

    @Test
    fun validScore_NoScoreModulus() {
        val gamePlayService = GamePlayService(eight)

        Assert.assertFalse(gamePlayService.isValidScore("a"))
        Assert.assertFalse(gamePlayService.isValidScore("-"))
        Assert.assertFalse(gamePlayService.isValidScore(".5"))

        Assert.assertTrue(gamePlayService.isValidScore("-1"))
        Assert.assertTrue(gamePlayService.isValidScore("0"))
        Assert.assertTrue(gamePlayService.isValidScore("1"))
        Assert.assertTrue(gamePlayService.isValidScore("2"))
        Assert.assertTrue(gamePlayService.isValidScore("3"))
        Assert.assertTrue(gamePlayService.isValidScore("4"))
        Assert.assertTrue(gamePlayService.isValidScore("5"))
        Assert.assertTrue(gamePlayService.isValidScore("6"))
        Assert.assertTrue(gamePlayService.isValidScore("7"))
        Assert.assertTrue(gamePlayService.isValidScore("8"))
        Assert.assertTrue(gamePlayService.isValidScore("9"))
        Assert.assertTrue(gamePlayService.isValidScore("10"))
    }
}