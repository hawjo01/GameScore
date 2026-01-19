package net.hawkins.gamescore.data.source.impl.file

import net.hawkins.gamescore.AbstractBaseTest
import org.junit.Assert
import org.junit.Test
import java.io.File
import java.nio.file.Paths

class FavoritePlayerDataSourceTest : AbstractBaseTest() {

    @Test
    fun fileDoesNotExist() {
        val dataSource = FavoritePlayerDataSource(randomTempFile())
        Assert.assertTrue(dataSource.getAll().isEmpty())
    }

    @Test
    fun getPlayers() {
        val resourceDirectory = Paths.get("src", "test", "resources")
        val file = File(resourceDirectory.toFile(), "favorite-players.json")
        val dataSource = FavoritePlayerDataSource(file)
        val names = dataSource.getAll()
        Assert.assertEquals(2, names.size)
        Assert.assertEquals("Sheldon", names[0])
        Assert.assertEquals("Leonard", names[1])
    }

    @Test
    fun addAndDelete() {
        val tempFile = tempDir.newFile()
        val dataSource = FavoritePlayerDataSource(tempFile)
        Assert.assertTrue(dataSource.getAll().isEmpty())
        dataSource.save("Penny")

        // Load a new file
        val dataSource1 = FavoritePlayerDataSource(tempFile)
        Assert.assertEquals(1, dataSource1.getAll().size)
        Assert.assertEquals("Penny", dataSource1.getAll()[0])

        dataSource1.delete("Penny")

        val dataSource2 = FavoritePlayerDataSource(tempFile)
        Assert.assertTrue(dataSource2.getAll().isEmpty())
    }
}