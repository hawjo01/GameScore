package net.hawkins.gamescore.data

import net.hawkins.gamescore.data.model.Idable

interface IdableRepository<T : Idable> {
    fun getAll(): List<T>
    fun getById(id: Int): T?
    fun save(item: T): T
    fun deleteById(id: Int)
}