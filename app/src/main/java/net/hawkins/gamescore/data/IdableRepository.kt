package net.hawkins.gamescore.data

import net.hawkins.gamescore.data.model.Idable

interface IdableRepository<T : Idable> {
    fun getAll(): List<T>
    fun save(item: T)
    fun deleteById(id: Int)
}