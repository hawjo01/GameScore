package net.hawkins.gamescore.favorites

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File
import java.nio.file.Paths

class FavoriteGamesTest : AbstractFavoriteTest() {

    @Test
    fun fileDoesNotExist() {
        val favoriteGames = FavoriteGames(randomTempFile())
        assertTrue(favoriteGames.getGames().isEmpty())
    }

    @Test
    fun getGames() {
        val resourceDirectory = Paths.get("src", "test", "resources")
        val file = File(resourceDirectory.toFile(), "favorite-games.json")
        val favoriteGames = FavoriteGames(file)
        val games = favoriteGames.getGames()
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
    fun getGames_InvalidGameName() {
        val resoureDirectory = Paths.get("src", "test", "resources")
        val file = File(resoureDirectory.toFile(), "favorite-games-invalid-game-name.json")
        assertTrue(file.exists())
        val favoriteGames = FavoriteGames(file)
        val games = favoriteGames.getGames()
        assertTrue(games.isEmpty())
    }

    @Test
    fun addAndRemove() {
        val tempFile = randomTempFile()
        val favoriteGames = FavoriteGames(tempFile)
        assertTrue(favoriteGames.getGames().isEmpty())

        val favoriteGame =
            FavoriteGame("Add Test Favorite", listOf("Penny", "Bernadette"), "Basic Scoring")
        favoriteGames.add(favoriteGame)

        val favoriteGames2 = FavoriteGames(tempFile)
        assertEquals(1, favoriteGames2.getGames().size)
        assertEquals(favoriteGame.name, favoriteGames2.getGames()[0].name)
        assertEquals(favoriteGame.game, favoriteGames2.getGames()[0].game)
        assertEquals(favoriteGame.players.size, favoriteGames2.getGames()[0].players.size)
        assertEquals(favoriteGame.players[0], favoriteGames2.getGames()[0].players[0])
        assertEquals(favoriteGame.players[1], favoriteGames2.getGames()[0].players[1])

        favoriteGames2.remove(favoriteGames2.getGames()[0])
        assertTrue(favoriteGames2.getGames().isEmpty())

        val favoriteGames3 = FavoriteGames(tempFile)
        assertTrue(favoriteGames3.getGames().isEmpty())
    }
}