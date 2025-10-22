package net.hawkins.gamescore.ui

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import net.hawkins.gamescore.game.Game


class GamePlayViewModel : GameScoreViewModel() {
    private val _uiState = MutableStateFlow(GamePlayUiState())
    val uiState: StateFlow<GamePlayUiState> = _uiState.asStateFlow()

    fun setGame(newGame: Game) {
        _uiState.update { currentState ->
            currentState.copy(game = newGame)
        }
    }
}