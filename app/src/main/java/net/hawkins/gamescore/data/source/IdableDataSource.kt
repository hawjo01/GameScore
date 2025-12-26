package net.hawkins.gamescore.data.source

import net.hawkins.gamescore.data.model.Idable

interface IdableDataSource <T : Idable> {
    fun getAll(): List<T>
    fun getById(id: Int): T?
    fun save(item: T): T
    fun deleteById(id: Int)
}