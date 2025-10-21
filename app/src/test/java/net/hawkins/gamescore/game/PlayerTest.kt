package net.hawkins.gamescore.game

import org.junit.Assert
import org.junit.Test

class PlayerTest {

    @Test
    fun totalScore() {
        val player = Player("Raj")
        Assert.assertEquals(0, player.totalScore())

        player.addScore(10)
        Assert.assertEquals(10, player.totalScore())

        player.addScore(20)
        Assert.assertEquals(30, player.totalScore())

        player.addScore(-5)
        Assert.assertEquals(25, player.totalScore())
    }

    @Test
    fun resetScores() {
        val player = Player("Sheldon")
        Assert.assertTrue(player.scores.isEmpty())
        player.addScore(1)
        player.addScore(2)
        player.addScore(3)
        Assert.assertFalse(player.scores.isEmpty())

        player.resetScores()
        Assert.assertTrue(player.scores.isEmpty())
    }

    @Test
    fun addScore() {
        val player = Player("Leonard")
        Assert.assertTrue(player.scores.isEmpty())
        player.addScore(1)
        Assert.assertEquals(1, player.scores.size)
        player.addScore(2)
        Assert.assertEquals(2, player.scores.size)
    }

    @Test
    fun changeScore() {
        val player = Player("Howard")
        player.addScore(1)
        Assert.assertEquals(1, player.scores[0])
        player.changeScore(scoreIndex = 0, newScore = 2)
        Assert.assertEquals(2, player.scores[0])
        Assert.assertEquals(2, player.totalScore())
    }
}