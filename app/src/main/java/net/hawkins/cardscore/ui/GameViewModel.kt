package net.hawkins.cardscore.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import net.hawkins.cardscore.data.Player

class GameViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

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
        return listOf(
            listOf("Jen", "Josh"),
            listOf("Phil", "Paula")
        )
    }
}