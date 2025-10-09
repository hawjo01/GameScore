package net.hawkins.gamescore.data

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import net.hawkins.gamescore.R
import java.io.BufferedReader
import java.io.File
import java.io.FileWriter
import java.io.IOException

class Favorites {

    private val _favorites = mutableListOf<Favorite>()
    private var _file : File? = null

    init {
        _favorites.add(Favorite("2500 - Jen, Josh", listOf("Jen", "Josh"), R.string.twenty_five_hundred))
        _favorites.add(Favorite("Scrabble - Jen, Josh", listOf("Jen", "Josh"), R.string.basic_scoring))
    }

    fun getFavorites() : List<Favorite> {
        return _favorites
    }

    fun load(file: File) {
        println("Initializing Favorites from " + file.absolutePath)
        if (file.exists()) {
            try {
                file.bufferedReader().use { bufferedReader: BufferedReader ->

                    val gson = Gson()
                    val listType = object : TypeToken<ArrayList<String>>() {}.type
                    val favoritesList =
                        gson.fromJson<ArrayList<Favorite>>(bufferedReader, listType)
                    favoritesList.forEach { _favorites.add(it) }
                }
            } catch (e: Exception) {
                println("An unexpected error occurred: ${e.message}")
            }
        } else {
            println("Skip loading favorite game.  File " + file.absolutePath + " does not exist")
        }
    }

    fun add(favorite: Favorite) {
        _favorites.add(favorite)
        save()
    }

    private fun save() {
        try {
            val gson = GsonBuilder().setPrettyPrinting().create()
            FileWriter(_file).use { writer ->
                gson.toJson(_favorites, writer)
            }
        } catch (e: IOException) {
            println("Failed to save players. " + e.message)
            e.printStackTrace()
        }
    }
}