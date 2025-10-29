package net.hawkins.gamescore.game

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class GameTest {

    object Seven : GameType {
        override fun getName(): String {
            return "Sevens"
        }

        override fun isValidScore(score: String): Boolean {
            return (score.toIntOrNull() != null && score.toInt() % 7 == 0)
        }

        override fun findWinner(players: List<Player>): Player? {
            return players.maxByOrNull { player -> player.totalScore() }
        }

        override fun hasWinningThreshold(): Boolean {
            return false
        }

        override fun highlightNegativeScore(): Boolean {
            return false
        }
    }

    object Eight : GameType {
        override fun getName(): String {
            return "Eights"
        }

        override fun isValidScore(score: String): Boolean {
            return (score.toIntOrNull() != null && score.toInt() % 8 == 0)
        }

        override fun findWinner(players: List<Player>): Player? {
            return players.maxByOrNull { player -> player.totalScore() }
        }

        override fun hasWinningThreshold(): Boolean {
            return true
        }

        override fun highlightNegativeScore(): Boolean {
            return true
        }
    }

    @Test
    fun getGameName() {
        val game = Game(Seven, listOf())
        assertEquals(Seven.getName(), game.getGameName())
    }

    @Test
    fun players() {
        val game = Game(Seven, listOf("Sheldon", "Leonard"))
        assertEquals(2, game.players.size)
        assertEquals("Sheldon", game.players[0].name)
        assertEquals("Leonard", game.players[1].name)
    }

    @Test
    fun validScore() {
        val game = Game(Seven, listOf())

        assertFalse(game.isValidScore("a"))
        assertFalse(game.isValidScore("-6"))
        assertFalse(game.isValidScore("-6"))

        assertTrue(game.isValidScore("-7"))
        assertTrue(game.isValidScore("0"))
        assertTrue(game.isValidScore("7"))
    }

    @Test
    fun resetGame() {
        val game = Game(Seven, listOf("Sheldon", "Leonard"))
        game.players[0].addScore(10)
        game.players[1].addScore(20)

        val winner = game.determineWinner()
        assertEquals("Leonard", winner?.name)
        assertEquals("Leonard", game.getWinner()?.name)

        game.resetGame()
        assertTrue(game.players[0].scores.isEmpty())
        assertTrue(game.players[1].scores.isEmpty())
        assertNull(game.getWinner())
    }

    @Test
    fun highlightNegativeScore() {
        val sevens = Game(Seven, listOf("Sheldon", "Leonard"))
        assertFalse(sevens.highlightNegativeScore())

        val eights = Game(Eight, listOf("Howard", "Rajesh"))
        assertTrue(eights.highlightNegativeScore())
    }

    @Test
    fun hasWinningThreshold() {
        val sevens = Game(Seven, listOf("Sheldon", "Leonard"))
        assertFalse(sevens.hasWinningThreshold())

        val eights = Game(Eight, listOf("Howard", "Rajesh"))
        assertTrue(eights.hasWinningThreshold())
    }

    @Test
    fun numberOfRounds() {
        val sevens = Game(Seven, listOf("Sheldon", "Leonard"))
        assertEquals(0, sevens.numberOfRounds())

        sevens.players[0].addScore(7)
        assertEquals(1, sevens.numberOfRounds())

        sevens.players[1].addScore(14)
        assertEquals(1, sevens.numberOfRounds())

        sevens.players[1].addScore(21)
        assertEquals(2, sevens.numberOfRounds())
    }
}