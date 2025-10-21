package net.hawkins.gamescore.favorites

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.File
import java.io.FileWriter
import java.io.IOException

class FavoritePlayers(private val file: File) {

    private val _names = mutableListOf<String>()

    init {
        println("Initializing FavoritePlayerNames from " + file.absolutePath)
        if (file.exists()) {
            try {
                file.bufferedReader().use { bufferedReader: BufferedReader ->
                    val gson = Gson()
                    val listType = object : TypeToken<ArrayList<String>>() {}.type
                    val playerList =
                        gson.fromJson<ArrayList<String>>(bufferedReader, listType)
                    playerList.forEach { _names.add(it) }
                }
            } catch (e: Exception) {
                println("An unexpected error occurred: ${e.message}")
            }
        } else {
            println("Skip loading favorite players.  File " + file.absolutePath + " does not exist")
        }
    }

    fun getNames(): List<String> {
        return _names
    }

    fun addName(name: String) {
        if (_names.contains(name)) {
            println("Skipping save of player '$name', name already exists")
            return
        }

        _names.add(name)
        save()
    }

    fun removeName(player: String) {
        _names.remove(player)
        save()
    }

    private fun save() {
        try {
            val gson = GsonBuilder().setPrettyPrinting().create()
            FileWriter(file).use { writer ->
                gson.toJson(_names, writer)
            }
        } catch (e: IOException) {
            println("Failed to save players. " + e.message)
            e.printStackTrace()
        }
    }
}