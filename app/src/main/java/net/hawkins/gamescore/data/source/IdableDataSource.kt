package net.hawkins.gamescore.data.source

import net.hawkins.gamescore.data.Idable

interface IdableDataSource <T : Idable> {
    fun getAll(): List<T>
    fun save(item: T)
    fun deleteById(id: Int)
}