package net.hawkins.gamescore.data.source

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File
import java.nio.file.Paths

class FileFavoritePlayerDataSourceTest : AbstractFileDataSourceTest() {

    @Test
    fun fileDoesNotExist() {
        val dataSource = FileFavoritePlayerDataSource(randomTempFile())
        assertTrue(dataSource.getPlayers().isEmpty())
    }

    @Test
    fun getPlayers() {
        val resourceDirectory = Paths.get("src", "test", "resources")
        val file = File(resourceDirectory.toFile(), "favorite-players.json")
        val dataSource = FileFavoritePlayerDataSource(file)
        val names = dataSource.getPlayers()
        assertEquals(2, names.size)
        assertEquals("Sheldon", names[0])
        assertEquals("Leonard", names[1])
    }

    @Test
    fun addAndDelete() {
        val tempFile = tempDir.newFile()
        val dataSource = FileFavoritePlayerDataSource(tempFile)
        assertTrue(dataSource.getPlayers().isEmpty())
        dataSource.savePlayer("Penny")

        // Load a new file
        val dataSource1 = FileFavoritePlayerDataSource(tempFile)
        assertEquals(1, dataSource1.getPlayers().size)
        assertEquals("Penny", dataSource1.getPlayers()[0])

        dataSource1.deletePlayer("Penny")

        val dataSource2 = FileFavoritePlayerDataSource(tempFile)
        assertTrue(dataSource2.getPlayers().isEmpty())
    }
}