package net.hawkins.gamescore.data.source.impl

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import net.hawkins.gamescore.data.Idable
import net.hawkins.gamescore.data.source.IdableDataSource
import java.io.BufferedReader
import java.io.File
import java.io.FileWriter
import java.io.IOException

abstract class IdableJsonFileDataSource<T : Idable>(
    protected val file: File,
    private val clazz: Class<T>
) :
    IdableDataSource<T> {

    override fun getAll(): List<T> {
        return if (file.exists()) {
            try {
                file.bufferedReader().use { bufferedReader: BufferedReader ->
                    val json = bufferedReader.readText()
                    val type = TypeToken.getParameterized(List::class.java, clazz).type
                    Gson().fromJson(json, type) ?: emptyList()
                }
            } catch (e: Exception) {
                println("An unexpected error occurred: ${e.message}")
                emptyList()
            }
        } else {
            println("File " + file.absolutePath + " does not exist")
            emptyList()
        }
    }

    override fun save(item: T) {
        val existingItems = getAll()
        val itemsToSave = if (item.id == null) {
            item.id = generateId(existingItems)
            existingItems.plus(item)
        } else {
            existingItems.filterNot { existingItem -> existingItem.id != item.id }.plus(item)
        }

        saveAll(itemsToSave)
    }

    override fun deleteById(id: Int) {
        val items = getAll()
        val itemsToSave = items.filterNot { existingItem -> existingItem.id == id }
        saveAll(itemsToSave)
    }

    private fun generateId(existingItems: List<T>): Int {
        val existingIds = existingItems.map { existingItem -> existingItem.id }
        var randomId: Int?
        do {
            randomId = (10000000..99999999).random()
        } while (existingIds.contains(randomId))
        return randomId
    }

    private fun saveAll(items: List<T>) {
        try {
            val gson = GsonBuilder().setPrettyPrinting().create()
            FileWriter(file).use { writer ->
                gson.toJson(items, writer)
            }
        } catch (e: IOException) {
            println("Error saving to file. " + e.message)
            e.printStackTrace()
        }
    }
}