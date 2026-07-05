package net.hawkins.gamescore.data

import net.hawkins.gamescore.data.model.FavoriteGame
import net.hawkins.gamescore.data.model.Game
import net.hawkins.gamescore.data.source.IdableDataSource
import org.junit.Assert.assertEquals
import org.junit.Test

class FavoriteGameRepositoryTest {

    private class MockIdableDataSource<T : net.hawkins.gamescore.data.model.Idable> : IdableDataSource<T> {
        val items = mutableListOf<T>()
        var getAllCalled = false
        var getByIdCalled = false
        var saveCalled = false
        var deleteByIdCalled = false

        override fun getAll(): List<T> {
            getAllCalled = true
            return items
        }

        override fun getById(id: Long): T? {
            getByIdCalled = true
            return items.find { it.id == id }
        }

        override fun save(item: T): T {
            saveCalled = true
            items.add(item)
            return item
        }

        override fun deleteById(id: Long) {
            deleteByIdCalled = true
            items.removeIf { it.id == id }
        }
    }

    @Test
    fun getAll_CallsDataSource() {
        val mockDataSource = MockIdableDataSource<FavoriteGame>()
        val repository = FavoriteGameRepository(mockDataSource)
        
        repository.getAll()
        
        assertEquals(true, mockDataSource.getAllCalled)
    }

    @Test
    fun getById_CallsDataSource() {
        val mockDataSource = MockIdableDataSource<FavoriteGame>()
        val repository = FavoriteGameRepository(mockDataSource)
        
        repository.getById(1L)
        
        assertEquals(true, mockDataSource.getByIdCalled)
    }

    @Test
    fun save_CallsDataSource() {
        val mockDataSource = MockIdableDataSource<FavoriteGame>()
        val repository = FavoriteGameRepository(mockDataSource)
        val favoriteGame = FavoriteGame(name = "Test", game = Game())
        
        repository.save(favoriteGame)
        
        assertEquals(true, mockDataSource.saveCalled)
    }

    @Test
    fun deleteById_CallsDataSource() {
        val mockDataSource = MockIdableDataSource<FavoriteGame>()
        val repository = FavoriteGameRepository(mockDataSource)
        
        repository.deleteById(1L)
        
        assertEquals(true, mockDataSource.deleteByIdCalled)
    }
}
