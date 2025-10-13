package net.hawkins.gamescore.data

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.File
import java.io.FileWriter
import java.io.IOException

class FavoriteGames(val file: File) {
    val games = mutableListOf<FavoriteGame>()

    init {
        println("Initializing FavoriteGames from " + file.absolutePath)
        if (file.exists()) {
            try {
                file.bufferedReader().use { bufferedReader: BufferedReader ->

                    val gson = Gson()
                    val listType = object : TypeToken<ArrayList<FavoriteGame>>() {}.type
                    val favoriteGameList =
                        gson.fromJson<ArrayList<FavoriteGame>>(bufferedReader, listType)
                    favoriteGameList.forEach { games.add(it) }
                }
            } catch (e: Exception) {
                println("An unexpected error occurred: ${e.message}")
            }
        } else {
            println("Skip loading favorite games.  File " + file.absolutePath + " does not exist")
        }
    }

    fun add(favoriteGame: FavoriteGame) {
        games.add(favoriteGame)
        save()
    }

    fun remove(favoriteGame: FavoriteGame) {
        games.remove(favoriteGame)
        save()
    }

    private fun save() {
        try {
            val gson = GsonBuilder().setPrettyPrinting().create()
            FileWriter(file).use { writer ->
                gson.toJson(games, writer)
            }
        } catch (e: IOException) {
            println("Failed to save favorite games. " + e.message)
            e.printStackTrace()
        }
    }
}

class FavoriteGame(
    val name: String = "",
    val players: List<String> = mutableListOf(),
    val game: String = ""
)