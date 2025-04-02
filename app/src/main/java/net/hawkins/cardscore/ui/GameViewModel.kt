package net.hawkins.cardscore.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import net.hawkins.cardscore.data.Player
import java.io.BufferedReader
import java.io.InputStreamReader

class GameViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()
    val savedPlayerLists = mutableListOf<List<String>>()

    fun loadPlayerNamesList(context: Context) {
        try {
            val inputStream = context.assets.open("players.json")
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val gson = Gson()
            val listType = object : TypeToken<ArrayList<ArrayList<String>>>() {}.type
            val playerNameLists =
                gson.fromJson<ArrayList<ArrayList<String>>>(bufferedReader, listType)
            playerNameLists.forEach { savedPlayerLists.add(it) }
        } catch (e: Exception) {
            println("An unexpected error occurred: ${e.message}")
        }
    }

    fun resetGame() {
        _uiState.value.players.forEach { player -> player.resetScores() }
    }

    fun findWinner(): Player? {
        if (_uiState.value.players.size == 0) {
            return null
        }

        var playerWithMaxNumberOfHands: Player? = _uiState.value.players.maxBy { it.scores.size }
        if (playerWithMaxNumberOfHands == null) {
            return null
        }

        var maxNumberOfHands = playerWithMaxNumberOfHands.scores.size
        var handComplete = _uiState.value.players.all { player -> player.scores.size == maxNumberOfHands }
        if (!handComplete) {
            return null
        }

        val playerWithHighestScore = _uiState.value.players.maxBy { it.totalScore() }
        if (playerWithHighestScore.totalScore() >= 1500) {
            return playerWithHighestScore
        } else {
            return null
        }
    }

    fun getPlayers(): List<Player> {
        return _uiState.value.players
    }

    fun playersReady(): Boolean {
        return _uiState.value.players.isNotEmpty()
    }

    fun addPlayerName(playerName: String) {
        _uiState.value.playerNames.add(playerName)
    }

    fun removePlayerName(playerName: String) {
        _uiState.value.playerNames.remove(playerName)
    }

    fun addPlayerNames(playerNames: List<String>) {
        _uiState.value.playerNames.addAll(playerNames)
    }

    fun getPlayerNames(): List<String> {
        return _uiState.value.playerNames
    }

    fun startGame() {
        val players = _uiState.value.playerNames.map { playerName -> Player(playerName) }
        _uiState.value.playerNames.clear()
        _uiState.value.players.addAll(players)
    }

    fun getSavedPlayerNameLists(): List<List<String>> {
        return savedPlayerLists
    }
}