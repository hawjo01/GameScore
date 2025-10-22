package net.hawkins.gamescore.data

import net.hawkins.gamescore.data.source.FileFavoriteGameDataSource
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File
import java.nio.file.Paths

class FavoriteGameRepositoryTest {

    @Test
    fun getGames_InvalidGameName() {
        val resourceDirectory = Paths.get("src", "test", "resources")
        val file = File(resourceDirectory.toFile(), "favorite-games-invalid-game-name.json")
        assertTrue(file.exists())
        val dataSource = FileFavoriteGameDataSource(file)
        val repository = FavoriteGameRepository(dataSource)
        val games = repository.getFavoriteGames()
        assertTrue(games.isEmpty())
    }
}