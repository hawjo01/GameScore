package net.hawkins.gamescore.data

import net.hawkins.gamescore.data.source.DataSource

abstract class AbstractRepository<T>(protected val dataSource : DataSource<T>) : Repository<T> {
    override fun getAll(): List<T> = dataSource.getAll()

    override fun save(item: T) = dataSource.save(item)

    override fun delete(item: T) = dataSource.delete(item)
}