package net.hawkins.gamescore.game

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PlayerTest {

    @Test
    fun totalScore() {
        val player = Player("Raj")
        assertEquals(0, player.totalScore())

        player.addScore(10)
        assertEquals(10, player.totalScore())

        player.addScore(20)
        assertEquals(30, player.totalScore())

        player.addScore(-5)
        assertEquals(25, player.totalScore())
    }

    @Test
    fun resetScores() {
        val player = Player("Sheldon")
        assertTrue(player.scores.isEmpty())
        player.addScore(1)
        player.addScore(2)
        player.addScore(3)
        assertFalse(player.scores.isEmpty())

        player.resetScores()
        assertTrue(player.scores.isEmpty())
    }

    @Test
    fun addScore() {
        val player = Player("Leonard")
        assertTrue(player.scores.isEmpty())
        player.addScore(1)
        assertEquals(1, player.scores.size)
        player.addScore(2)
        assertEquals(2, player.scores.size)
    }

    @Test
    fun changeScore() {
        val player = Player("Howard")
        player.addScore(1)
        assertEquals(1, player.scores[0])
        player.changeScore(scoreIndex = 0, newScore = 2)
        assertEquals(2, player.scores[0])
        assertEquals(2, player.totalScore())
    }

    @Test
    fun deleteScore() {
        val player = Player("Amy")
        assertEquals(0, player.scores.size)

        player.addScore(7)
        assertEquals(1, player.scores.size)
        player.deleteScore(0)
        assertEquals(0, player.scores.size)

        player.addScore(0)
        player.addScore(7)
        player.addScore(-7)
        assertEquals(3, player.scores.size)
        player.deleteScore(1)
        assertEquals(2, player.scores.size)
        assertEquals(listOf(0, -7), player.scores)

        player.deleteScore(0)
        assertEquals(1, player.scores.size)
        assertEquals(listOf(-7), player.scores)

    }
}