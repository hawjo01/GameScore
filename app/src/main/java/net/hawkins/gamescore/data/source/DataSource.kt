package net.hawkins.gamescore.data.source

interface DataSource<T> {
    fun getAll(): List<T>
    fun save(item: T)
    fun delete(item: T)
}