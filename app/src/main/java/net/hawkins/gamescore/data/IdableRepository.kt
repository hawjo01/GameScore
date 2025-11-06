package net.hawkins.gamescore.data

interface IdableRepository<T : Idable> {
    fun getAll(): List<T>
    fun save(item: T)
    fun deleteById(id: Int)
}