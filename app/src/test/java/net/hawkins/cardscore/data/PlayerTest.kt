package net.hawkins.cardscore.data

import org.junit.Assert.*
import org.junit.Test

class PlayerTest {

    @Test
    fun totalScore() {
        val player = Player("Foo")
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
        val player = Player("Foo")
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
        val player = Player("Foo")
        assertTrue(player.scores.isEmpty())
        player.addScore(1)
        assertEquals(1, player.scores.size)
        player.addScore(2)
        assertEquals(2, player.scores.size)
    }

    @Test
    fun changeScore() {
        val player = Player("foo")
        player.addScore(1)
        assertEquals(1, player.scores[0])
        player.changeScore(scoreIndex = 0, newScore = 2)
        assertEquals(2, player.scores[0])
        assertEquals(2, player.totalScore())
    }
}