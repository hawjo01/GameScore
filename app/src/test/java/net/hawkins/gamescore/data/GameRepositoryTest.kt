package net.hawkins.gamescore.data

import org.junit.Assert
import org.junit.Test

class GameRepositoryTest {

    @Test
    fun numberOfGameTypes() {
        Assert.assertEquals(3, GameRepository.getAll().size)
    }

    @Test
    fun getByName() {
        val twentyFiveHundred = GameRepository.getByName("2500")
        Assert.assertEquals("2500", twentyFiveHundred.name)

        val basicScore = GameRepository.getByName("Basic Scoring - High")
        Assert.assertEquals("Basic Scoring - High", basicScore.name)
    }

    @Test
    fun isValidName() {
        Assert.assertTrue(GameRepository.isGame("2500"))
        Assert.assertTrue(GameRepository.isGame("Basic Scoring - High"))
        Assert.assertTrue(GameRepository.isGame("Basic Scoring - Low"))

        Assert.assertFalse(GameRepository.isGame("Not a valid game name"))
    }
}