package net.hawkins.gamescore.ui.managegames

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import net.hawkins.gamescore.data.GameRepository
import net.hawkins.gamescore.ui.AbstractViewModel
import javax.inject.Inject

@HiltViewModel
class GameManagementViewModel @Inject constructor(
    private val _gameRepository: GameRepository,
) : AbstractViewModel() {

    private val _uiState = MutableStateFlow(GameManagementUiState())
    val uiState: StateFlow<GameManagementUiState> = _uiState.asStateFlow()

    init {
        refreshState()
    }

    fun onEvent(event: GameManagementUiEvent) {
        when (event) {
            is GameManagementUiEvent.DeleteGame -> deleteGame(event.id)
            is GameManagementUiEvent.RefreshState -> refreshState()
        }
    }

    private fun deleteGame(id: Int) {
        _gameRepository.deleteById(id)
        refreshState()
    }

    private fun refreshState() {
        val games = _gameRepository.getAll()
        _uiState.update { currentState ->
            currentState.copy(
                games = games
            )
        }
    }
}