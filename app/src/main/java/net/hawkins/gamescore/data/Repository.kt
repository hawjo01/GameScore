package net.hawkins.gamescore.data

interface Repository<T> {
    fun getAll(): List<T>
    fun save(item: T)
    fun delete(item: T)
}