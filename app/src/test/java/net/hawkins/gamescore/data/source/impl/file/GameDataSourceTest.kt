package net.hawkins.gamescore.data.source.impl.file

import net.hawkins.gamescore.AbstractBaseTest
import net.hawkins.gamescore.data.model.Game
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class GameDataSourceTest : AbstractBaseTest() {

    @Test
    fun addAndRemove() {
        val tempFile = randomTempFile()
        val dataSource = GameDataSource(tempFile)
        assertTrue(dataSource.getAll().isEmpty())

        val game = Game(name = "New Game")
        dataSource.save(game)

        val dataSource2 = GameDataSource(tempFile)
        assertEquals(1, dataSource2.getAll().size)
        assertEquals(game.name, dataSource2.getAll()[0].name)

        val id = dataSource2.getAll()[0].id
        assertNotNull(id)
        dataSource2.deleteById(id)
        assertTrue(dataSource2.getAll().isEmpty())
    }

    @Test
    fun saveAndUpdate() {
        val tempFile = randomTempFile()
        val dataSource = GameDataSource(tempFile)
        
        val game = Game(name = "Initial Name")
        val savedGame = dataSource.save(game)
        
        val modifiedGame = savedGame.copy(name = "Updated Name")
        dataSource.save(modifiedGame)
        
        val dataSource2 = GameDataSource(tempFile)
        assertEquals(1, dataSource2.getAll().size)
        assertEquals("Updated Name", dataSource2.getAll()[0].name)
        assertEquals(savedGame.id, dataSource2.getAll()[0].id)
    }
}
