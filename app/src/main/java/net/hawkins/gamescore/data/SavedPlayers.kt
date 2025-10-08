package net.hawkins.gamescore.data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.File
import java.io.FileWriter
import java.io.IOException

class SavedPlayers() {

    private var _players = mutableStateListOf<String>()
    private var _file : File? = null

    fun loadFromFile(file: File) {
        _file = file

        println("Initializing SavedPlayers from " + file.absolutePath)
        if (file.exists()) {
            try {
                file.bufferedReader().use { bufferedReader: BufferedReader ->

                    val gson = Gson()
                    val listType = object : TypeToken<ArrayList<String>>() {}.type
                    val playerList =
                        gson.fromJson<ArrayList<String>>(bufferedReader, listType)
                    playerList.forEach { _players.add(it) }
                }
            } catch (e: Exception) {
                println("An unexpected error occurred: ${e.message}")
            }
        } else {
            println("Skip loading saved players.  File " + file.absolutePath + " does not exist")
        }
    }

    fun getPlayers(): SnapshotStateList<String> {
        return _players
    }

    fun addPlayer(player: String) {
        if (_players.contains(player)) {
            println("Skipping save of player '$player', name already exists")
            return
        }

        _players.add(player)
        savePlayers()

    }

    fun removePlayer(player: String) {
        _players.remove(player)
        savePlayers()
    }

    private fun savePlayers() {
        try {
            val gson = GsonBuilder().setPrettyPrinting().create()
            FileWriter(_file).use { writer ->
                gson.toJson(_players, writer)
            }
        } catch (e: IOException) {
            println("Failed to save players. " + e.message)
            e.printStackTrace()
        }
    }

}