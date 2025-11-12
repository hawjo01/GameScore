package net.hawkins.gamescore.ui

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import net.hawkins.gamescore.data.FavoriteGameRepository
import net.hawkins.gamescore.data.FavoritePlayerRepository
import net.hawkins.gamescore.data.model.FavoriteGame
import javax.inject.Inject

@HiltViewModel
class GamePlaySetupViewModel @Inject constructor(
    private val _playerRepository: FavoritePlayerRepository,
    private val _favoriteGameRepository: FavoriteGameRepository
) : AbstractViewModel() {
    private val _uiState = MutableStateFlow(GamePlaySetupUiState())
    val uiState: StateFlow<GamePlaySetupUiState> = _uiState.asStateFlow()

    init {
        _uiState.update { currentState ->
            currentState.copy(
                favoritePlayerNames = _playerRepository.getAll(),
                favoriteGames = _favoriteGameRepository.getAll()
            )
        }
    }

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

    fun removePlayer(position: Int) {
        _uiState.update { currentState ->
            currentState.copy(playerNames = currentState.playerNames.filterIndexed { index, _ -> index != position })
        }
    }

    fun setPlayers(newPlayerNames: List<String>) {
        _uiState.update { currentState ->
            currentState.copy(playerNames = newPlayerNames)
        }
    }

    fun deleteFavoritePlayer(player: String) {
        _playerRepository.delete(player)
        _uiState.update { currentState ->
            currentState.copy(favoritePlayerNames = _playerRepository.getAll())
        }
    }

    fun addFavoritePlayer(player: String) {
        if (!_playerRepository.getAll().contains(player)) {
            _playerRepository.save(player)
            _uiState.update { currentState ->
                currentState.copy(favoritePlayerNames = _playerRepository.getAll())
            }
        }
    }

    fun deleteFavoriteGame(favoriteGame: FavoriteGame) {
        val favoriteGameId = favoriteGame.id
        if (favoriteGameId != null) {
            _favoriteGameRepository.deleteById(favoriteGameId)
            _uiState.update { currentState ->
                currentState.copy(favoriteGames = _favoriteGameRepository.getAll())
            }
        }
    }
}