package net.hawkins.gamescore.data.source.impl

import net.hawkins.gamescore.AbstractBaseTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File
import java.nio.file.Paths

class FileFavoritePlayerDataSourceTest : AbstractBaseTest() {

    @Test
    fun fileDoesNotExist() {
        val dataSource = FileFavoritePlayerDataSource(randomTempFile())
        assertTrue(dataSource.getAll().isEmpty())
    }

    @Test
    fun getPlayers() {
        val resourceDirectory = Paths.get("src", "test", "resources")
        val file = File(resourceDirectory.toFile(), "favorite-players.json")
        val dataSource = FileFavoritePlayerDataSource(file)
        val names = dataSource.getAll()
        assertEquals(2, names.size)
        assertEquals("Sheldon", names[0])
        assertEquals("Leonard", names[1])
    }

    @Test
    fun addAndDelete() {
        val tempFile = tempDir.newFile()
        val dataSource = FileFavoritePlayerDataSource(tempFile)
        assertTrue(dataSource.getAll().isEmpty())
        dataSource.save("Penny")

        // Load a new file
        val dataSource1 = FileFavoritePlayerDataSource(tempFile)
        assertEquals(1, dataSource1.getAll().size)
        assertEquals("Penny", dataSource1.getAll()[0])

        dataSource1.delete("Penny")

        val dataSource2 = FileFavoritePlayerDataSource(tempFile)
        assertTrue(dataSource2.getAll().isEmpty())
    }
}