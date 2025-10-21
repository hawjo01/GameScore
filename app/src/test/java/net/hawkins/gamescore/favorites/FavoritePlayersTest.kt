package net.hawkins.gamescore.favorites

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File
import java.nio.file.Paths

class FavoritePlayersTest : AbstractFavoriteTest() {

    @Test
    fun fileDoesNotExist() {
        val favoritePlayers = FavoritePlayers(randomTempFile())
        assertTrue(favoritePlayers.getNames().isEmpty())
    }

    @Test
    fun getNames() {
        val resoureDirectory = Paths.get("src", "test", "resources")
        val file = File(resoureDirectory.toFile(), "favorite-players.json")
        val favoritePlayers = FavoritePlayers(file)
        val names = favoritePlayers.getNames()
        assertEquals(2, names.size)
        assertEquals("Sheldon", names[0])
        assertEquals("Leonard", names[1])
    }

    @Test
    fun saveAndRemoveName() {
        val tempFile = tempDir.newFile()
        val favoritePlayers = FavoritePlayers(tempFile)
        assertTrue(favoritePlayers.getNames().isEmpty())
        favoritePlayers.addName("Penny")

        // Load a new file
        val favoritePlayers1 = FavoritePlayers(tempFile)
        assertEquals(1, favoritePlayers1.getNames().size)
        assertEquals("Penny", favoritePlayers1.getNames()[0])

        favoritePlayers1.removeName("Penny")

        val favoritePlayers2 = FavoritePlayers(tempFile)
        assertTrue(favoritePlayers2.getNames().isEmpty())
    }

    @Test
    fun addName() {
        val favoritePlayers = FavoritePlayers(randomTempFile())
        assertTrue(favoritePlayers.getNames().isEmpty())

        favoritePlayers.addName("Howard")
        assertTrue(favoritePlayers.getNames().isNotEmpty())
        assertEquals("Howard", favoritePlayers.getNames()[0])

        // Duplicate names should be ignored silently
        favoritePlayers.addName("Howard")
        assertTrue(favoritePlayers.getNames().isNotEmpty())
        assertEquals("Howard", favoritePlayers.getNames()[0])
    }

    @Test
    fun removeName() {
        val favoritePlayers = FavoritePlayers(randomTempFile())
        assertTrue(favoritePlayers.getNames().isEmpty())

        // Non-existent name should be ignored silently
        favoritePlayers.removeName("Rajesh")
        assertTrue(favoritePlayers.getNames().isEmpty())
    }
}