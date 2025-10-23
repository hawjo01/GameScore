package net.hawkins.gamescore.data.source

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.File
import java.io.FileWriter
import java.io.IOException

class FileFavoritePlayerDataSource(private val file: File) : FavoritePlayerDataSource {

    override fun getPlayers(): List<String> {
        println("Initializing FavoritePlayerNames from " + file.absolutePath)
        return if (file.exists()) {
            try {
                file.bufferedReader().use { bufferedReader: BufferedReader ->
                    val json = bufferedReader.readText()
                    val type = object : TypeToken<ArrayList<String>>() {}.type
                    Gson().fromJson(json, type) ?: emptyList()
                }
            } catch (e: Exception) {
                println("An unexpected error occurred: ${e.message}")
                emptyList()
            }
        } else {
            println("Skip loading favorite players.  File " + file.absolutePath + " does not exist")
            emptyList()
        }
    }

    override fun savePlayer(player: String) {
        val players = getPlayers()
        savePlayers(players.plus(player))
    }

    override fun deletePlayer(player: String) {
        val players = getPlayers()
        savePlayers(players.minus(player))
    }

    private fun savePlayers(players: List<String>) {
        try {
            val gson = GsonBuilder().setPrettyPrinting().create()
            FileWriter(file).use { writer ->
                gson.toJson(players, writer)
            }
        } catch (e: IOException) {
            println("Failed to save players. " + e.message)
            e.printStackTrace()
        }
    }
}