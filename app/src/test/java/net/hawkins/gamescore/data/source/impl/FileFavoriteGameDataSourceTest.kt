package net.hawkins.gamescore.data.source.impl

import net.hawkins.gamescore.AbstractBaseTest
import net.hawkins.gamescore.model.FavoriteGame
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File
import java.nio.file.Paths

class FileFavoriteGameDataSourceTest : AbstractBaseTest() {

    @Test
    fun fileDoesNotExist() {
        val dataSource = FileFavoriteGameDataSource(randomTempFile())
        assertTrue(dataSource.getAll().isEmpty())
    }

    @Test
    fun getGames() {
        val resourceDirectory = Paths.get("src", "test", "resources")
        val file = File(resourceDirectory.toFile(), "favorite-games.json")
        val dataSource = FileFavoriteGameDataSource(file)
        val games = dataSource.getAll()
        assertEquals(2, games.size)

        val favoriteGame1 = games[0]
        assertEquals("2500 - Sheldon & Leonard", favoriteGame1.name)
        assertEquals("2500", favoriteGame1.game)
        assertEquals(2, favoriteGame1.players.size)
        assertEquals("Sheldon", favoriteGame1.players[0])
        assertEquals("Leonard", favoriteGame1.players[1])

        val favoriteGame2 = games[1]
        assertEquals("Scrabble - Howard & Rajesh", favoriteGame2.name)
        assertEquals("Basic Scoring", favoriteGame2.game)
        assertEquals(2, favoriteGame2.players.size)
        assertEquals("Howard", favoriteGame2.players[0])
        assertEquals("Rajesh", favoriteGame2.players[1])
    }

    @Test
    fun addAndRemove() {
        val tempFile = randomTempFile()
        val dataSource = FileFavoriteGameDataSource(tempFile)
        assertTrue(dataSource.getAll().isEmpty())

        val favoriteGame =
            FavoriteGame("Add Test Favorite", listOf("Penny", "Bernadette"), "Basic Scoring")
        dataSource.save(favoriteGame)

        val dataSource2 = FileFavoriteGameDataSource(tempFile)
        assertEquals(1, dataSource2.getAll().size)
        assertEquals(favoriteGame.name, dataSource2.getAll()[0].name)
        assertEquals(favoriteGame.game, dataSource2.getAll()[0].game)
        assertEquals(favoriteGame.players.size, dataSource2.getAll()[0].players.size)
        assertEquals(favoriteGame.players[0], dataSource2.getAll()[0].players[0])
        assertEquals(favoriteGame.players[1], dataSource2.getAll()[0].players[1])

        dataSource2.delete(dataSource2.getAll()[0])
        assertTrue(dataSource2.getAll().isEmpty())

        val dataSource3 = FileFavoriteGameDataSource(tempFile)
        assertTrue(dataSource3.getAll().isEmpty())
    }
}