package net.hawkins.gamescore.data

import net.hawkins.gamescore.data.model.Idable
import net.hawkins.gamescore.data.source.IdableDataSource

abstract class AbstractIdableRepository<T : Idable>(protected val dataSource : IdableDataSource<T>) : IdableRepository<T> {
    override fun getAll(): List<T> = dataSource.getAll()

    override fun save(item: T) = dataSource.save(item)

    override fun deleteById(id: Int) = dataSource.deleteById(id)
}