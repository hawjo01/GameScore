package net.hawkins.gamescore.data.source

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.File
import java.io.FileWriter
import java.io.IOException

abstract class JsonFileDataSource<T>(private val file: File, private val clazz: Class<T>) {

    protected fun getAll(): List<T> {
        return if (file.exists()) {
            try {
                file.bufferedReader().use { bufferedReader: BufferedReader ->
                    val json = bufferedReader.readText()
                    val type = TypeToken.getParameterized(List::class.java, clazz).getType()
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

    protected fun save(item: T) {
        val items = getAll()
        saveAll(items.plus(item))
    }

    protected fun delete(item: T) {
        val items = getAll()
        saveAll(items.minus(item))
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