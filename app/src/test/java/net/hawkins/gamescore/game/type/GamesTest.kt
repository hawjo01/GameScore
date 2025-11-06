package net.hawkins.gamescore.game.type

import org.junit.Assert.*
import org.junit.Test

class GamesTest {

    @Test
    fun numberOfGameTypes() {
        assertEquals(3, Games.TYPES.size)
    }

    @Test
    fun getByName() {
        val twentyFiveHundred = Games.getByName("2500")
        assertEquals("2500", twentyFiveHundred.getName())

        val basicScore = Games.getByName("Basic Scoring - High")
        assertEquals("Basic Scoring - High", basicScore.getName())
    }

    @Test
    fun isValidName() {
        assertTrue(Games.isValidType("2500"))
        assertTrue(Games.isValidType("Basic Scoring - High"))
        assertTrue(Games.isValidType("Basic Scoring - Low"))

        assertFalse(Games.isValidType("Not a valid game name"))
    }
}