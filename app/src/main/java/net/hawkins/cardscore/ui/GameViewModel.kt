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
        var handComplete = _uiState.value.players.all { player -> player.scores.size == _uiState.value.players[0].scores.size }
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
}