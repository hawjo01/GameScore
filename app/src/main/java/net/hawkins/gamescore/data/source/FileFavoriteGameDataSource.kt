package net.hawkins.gamescore.data.source

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import net.hawkins.gamescore.model.FavoriteGame
import java.io.BufferedReader
import java.io.File
import java.io.FileWriter
import java.io.IOException

class FileFavoriteGameDataSource (private val file: File) : FavoriteGameDataSource {

    override fun getGames(): List<FavoriteGame> {
        return if (file.exists()) {
            try {
                file.bufferedReader().use { bufferedReader: BufferedReader ->
                    val json = bufferedReader.readText()
                    val type = object : TypeToken<ArrayList<FavoriteGame>>() {}.type
                    Gson().fromJson(json, type) ?: emptyList()
                }
            } catch (e: Exception) {
                println("An unexpected error occurred: ${e.message}")
                emptyList()
            }
        } else {
            println("Skip loading favorite games.  File " + file.absolutePath + " does not exist")
            emptyList()
        }
    }

    override fun saveGame(favoriteGame: FavoriteGame) {
        val games = getGames()
        saveGames(games.plus(favoriteGame))
    }

    override fun deleteGame(favoriteGame: FavoriteGame) {
        val games = getGames()
        saveGames(games.minus(favoriteGame))
    }

    private fun saveGames(favoriteGames: List<FavoriteGame>) {
        try {
            val gson = GsonBuilder().setPrettyPrinting().create()
            FileWriter(file).use { writer ->
                gson.toJson(favoriteGames, writer)
            }
        } catch (e: IOException) {
            println("Failed to save favorite games. " + e.message)
            e.printStackTrace()
        }
    }
}