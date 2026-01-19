package net.hawkins.gamescore.data.source.impl.file

import net.hawkins.gamescore.AbstractBaseTest
import net.hawkins.gamescore.data.model.FavoriteGame
import net.hawkins.gamescore.data.model.Game
import org.junit.Test
import java.io.File
import java.nio.file.Paths
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class FavoriteGameDataSourceTest : AbstractBaseTest() {

    @Test
    fun fileDoesNotExist() {
        val dataSource = FavoriteGameDataSource(randomTempFile())
        assertTrue(dataSource.getAll().isEmpty())
    }

    @Test
    fun getGames() {
        val resourceDirectory = Paths.get("src", "test", "resources")
        val file = File(resourceDirectory.toFile(), "favorite-games.json")
        val dataSource = FavoriteGameDataSource(file)
        val games = dataSource.getAll()
        assertEquals(2, games.size)

        val favoriteGame1 = games[0]
        assertEquals("2500 - Sheldon & Leonard", favoriteGame1.name)
        assertEquals("2500", favoriteGame1.game.name)
        assertEquals(2, favoriteGame1.players.size)
        assertEquals("Sheldon", favoriteGame1.players[0])
        assertEquals("Leonard", favoriteGame1.players[1])

        val favoriteGame2 = games[1]
        assertEquals("Scrabble - Howard & Rajesh", favoriteGame2.name)
        assertEquals("Basic Scoring", favoriteGame2.game.name)
        assertEquals(2, favoriteGame2.players.size)
        assertEquals("Howard", favoriteGame2.players[0])
        assertEquals("Rajesh", favoriteGame2.players[1])
    }

    @Test
    fun addAndRemove() {
        val tempFile = randomTempFile()
        val dataSource = FavoriteGameDataSource(tempFile)
        assertTrue(dataSource.getAll().isEmpty())

        val favoriteGame =
            FavoriteGame(
                "Add Test Favorite",
                listOf("Penny", "Bernadette"),
                Game(name = "Basic Scoring")
            )
        dataSource.save(favoriteGame)

        val dataSource2 = FavoriteGameDataSource(tempFile)
        assertEquals(1, dataSource2.getAll().size)
        assertEquals(favoriteGame.name, dataSource2.getAll()[0].name)
        assertEquals(favoriteGame.game, dataSource2.getAll()[0].game)
        assertEquals(favoriteGame.players.size, dataSource2.getAll()[0].players.size)
        assertEquals(favoriteGame.players[0], dataSource2.getAll()[0].players[0])
        assertEquals(favoriteGame.players[1], dataSource2.getAll()[0].players[1])

        val id = dataSource2.getAll()[0].id
        assertNotNull(id)
        dataSource2.deleteById(id)
        assertTrue(dataSource2.getAll().isEmpty())

        val dataSource3 = FavoriteGameDataSource(tempFile)
        assertTrue(dataSource3.getAll().isEmpty())
    }

    @Test
    fun saveAndUpdate() {
        val tempFile = randomTempFile()
        val dataSource = FavoriteGameDataSource(tempFile)
        assertTrue(dataSource.getAll().isEmpty())

        val favoriteGame =
            FavoriteGame(
                "Add Test Favorite",
                listOf("Penny", "Bernadette"),
                Game(name = "Basic Scoring")
            )
        dataSource.save(favoriteGame)

        val dataSource2 = FavoriteGameDataSource(tempFile)
        assertEquals(1, dataSource2.getAll().size)
        val savedGame = dataSource2.getAll()[0]
        assertEquals(favoriteGame.name, savedGame.name)
        assertEquals(favoriteGame.game, savedGame.game)
        assertEquals(favoriteGame.players.size, savedGame.players.size)
        assertEquals(favoriteGame.players[0], savedGame.players[0])
        assertEquals(favoriteGame.players[1], savedGame.players[1])
        assertNotNull(savedGame.id)

        val modifiedGame = savedGame.copy(name = "Updated Name")
        dataSource2.save(modifiedGame)

        val dataSource3 = FavoriteGameDataSource(tempFile)
        assertEquals(1, dataSource3.getAll().size)
        val updatedGame = dataSource3.getAll()[0]
        assertEquals("Updated Name", updatedGame.name)
        assertEquals(favoriteGame.game, updatedGame.game)
        assertEquals(favoriteGame.players.size, updatedGame.players.size)
        assertEquals(favoriteGame.players[0], updatedGame.players[0])
        assertEquals(favoriteGame.players[1], updatedGame.players[1])
        assertEquals(updatedGame.id, savedGame.id)
    }

    @Test
    fun getById_NoSavedItems() {
        val tempFile = randomTempFile()
        val dataSource = FavoriteGameDataSource(tempFile)
        assertTrue(dataSource.getAll().isEmpty())

        assertNull(dataSource.getById(1))
    }

    @Test
    fun getById() {
        val tempFile = randomTempFile()
        val dataSource = FavoriteGameDataSource(tempFile)
        assertTrue(dataSource.getAll().isEmpty())

        val favoriteGame =
            FavoriteGame(
                "Add Test Favorite",
                listOf("Penny", "Bernadette"),
                Game(name = "Basic Scoring")
            )
        val savedGame = dataSource.save(favoriteGame)
        assertNotNull(savedGame.id)

        val retrievedGame = dataSource.getById(savedGame.id)
        assertNotNull(retrievedGame)
    }

    @Test
    fun getById_NoItemWithId() {
        val tempFile = randomTempFile()
        val dataSource = FavoriteGameDataSource(tempFile)
        assertTrue(dataSource.getAll().isEmpty())

        val favoriteGame =
            FavoriteGame(
                "Add Test Favorite",
                listOf("Penny", "Bernadette"),
                Game(name = "Basic Scoring")
            )
        val savedGame = dataSource.save(favoriteGame)
        assertNotNull(savedGame.id)

        val retrievedGame = dataSource.getById(savedGame.id)
        assertNotNull(retrievedGame)

        val newId = retrievedGame.id + 1
        val nullGame = dataSource.getById(newId)
        assertNull(nullGame)


    }
}