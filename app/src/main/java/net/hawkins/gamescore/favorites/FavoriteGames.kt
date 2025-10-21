package net.hawkins.gamescore.favorites

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.File
import java.io.FileWriter
import java.io.IOException

class FavoriteGames(private val file: File) {
    private val _games = mutableListOf<FavoriteGame>()

    init {
        println("Initializing FavoriteGames from " + file.absolutePath)
        if (file.exists()) {
            try {
                file.bufferedReader().use { bufferedReader: BufferedReader ->
                    val gson = Gson()
                    val listType = object : TypeToken<ArrayList<FavoriteGame>>() {}.type
                    val favoriteGameList =
                        gson.fromJson<ArrayList<FavoriteGame>>(bufferedReader, listType)
                    favoriteGameList.forEach { _games.add(it) }
                }
            } catch (e: Exception) {
                println("An unexpected error occurred: ${e.message}")
            }
        } else {
            println("Skip loading favorite games.  File " + file.absolutePath + " does not exist")
        }
    }

    fun getGames(): List<FavoriteGame> {
        return _games
    }

    fun add(favoriteGame: FavoriteGame) {
        _games.add(favoriteGame)
        save()
    }

    fun remove(favoriteGame: FavoriteGame) {
        _games.remove(favoriteGame)
        save()
    }

    private fun save() {
        try {
            val gson = GsonBuilder().setPrettyPrinting().create()
            FileWriter(file).use { writer ->
                gson.toJson(_games, writer)
            }
        } catch (e: IOException) {
            println("Failed to save favorite games. " + e.message)
            e.printStackTrace()
        }
    }
}

data class FavoriteGame(
    val name: String = "",
    val players: List<String> = listOf(),
    val game: String = ""
)