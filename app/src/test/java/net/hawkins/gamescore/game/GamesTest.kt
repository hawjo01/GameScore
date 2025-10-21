package net.hawkins.gamescore.game

import org.junit.Assert.*
import org.junit.Test

class GamesTest {

    @Test
    fun numberOfGameTypes() {
        assertEquals(2, Games.TYPES.size)
    }

    @Test
    fun getByName() {
        val twentyFiveHunderd = Games.getByName("2500")
        assertEquals(TwentyFiveHundred, twentyFiveHunderd)

        val basicScore = Games.getByName("Basic Scoring")
        assertEquals(BasicScore, basicScore)
    }

    @Test
    fun isValidName() {
        assertTrue(Games.isValidGame("2500"))
        assertTrue(Games.isValidGame("Basic Scoring"))

        assertFalse(Games.isValidGame("Not a valid game name"))
    }
}