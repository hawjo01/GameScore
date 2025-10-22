package net.hawkins.gamescore.ui

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameSetupViewModel : GameScoreViewModel() {
    private val _uiState = MutableStateFlow(GameSetupUiState())
    val uiState: StateFlow<GameSetupUiState> = _uiState.asStateFlow()

    fun setGameName(newGameName: String) {
        _uiState.update { currentState ->
            currentState.copy(gameName = newGameName)
        }
    }

    fun addPlayer(newPlayerName: String) {
        _uiState.update { currentState ->
            currentState.copy(playerNames = currentState.playerNames.plus(newPlayerName))
        }
    }

    fun removePlayer(index: Int) {
        _uiState.update { currentState ->
            currentState.copy(playerNames = currentState.playerNames.drop(index))
        }
    }

    fun setPlayers(newPlayerNames: List<String>) {
        _uiState.update { currentState ->
            currentState.copy(playerNames = newPlayerNames)
        }
    }
}