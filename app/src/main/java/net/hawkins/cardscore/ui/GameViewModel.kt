package net.hawkins.cardscore.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import net.hawkins.cardscore.data.Player

class GameViewModel :  ViewModel() {
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    fun resetGame() {
        for (player in _uiState.value.players) {
            player.resetScores()
        }
    }

    fun findWinner(): Player? {
        if (_uiState.value.players.size == 0) {
            return null
        }
        var playerWithMaxNumberOfHands: Player? = _uiState.value.players.maxBy{ it.scores.size }
        if (playerWithMaxNumberOfHands == null) {
            return null
        }
        var maxNumberOfHands = playerWithMaxNumberOfHands.scores.size
        var handComplete = _uiState.value.players.all { player -> player.scores.size == maxNumberOfHands}
        if (handComplete) {
            val playerWithHighestScore = _uiState.value.players.maxBy { it.totalScore() }
            if (playerWithHighestScore.totalScore() >= 1500) {
                return playerWithHighestScore
            }
        }
        return null
    }

    fun getPlayers(): List<Player> {
        return _uiState.value.players
    }

    fun setPlayers(playerNames: List<String>) {
        val players = ArrayList<Player>()
        for (playerName in playerNames) {
            players.add(Player(playerName))
        }
        _uiState.value.players.addAll(players)
    }

    fun playersReady(): Boolean {
        var ready = _uiState.value.players.size > 0
        return ready
    }
}