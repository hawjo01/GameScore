package net.hawkins.gamescore.ui.gameplaysetup

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import net.hawkins.gamescore.data.FavoriteGameRepository
import net.hawkins.gamescore.data.FavoritePlayerRepository
import net.hawkins.gamescore.data.GameRepository
import net.hawkins.gamescore.data.model.Game
import net.hawkins.gamescore.ui.AbstractViewModel
import javax.inject.Inject

@HiltViewModel
class GamePlaySetupViewModel @Inject constructor(
    private val _gameRepository: GameRepository,
    private val _playerRepository: FavoritePlayerRepository,
    private val _favoriteGameRepository: FavoriteGameRepository
) : AbstractViewModel() {
    private val _uiState = MutableStateFlow(GamePlaySetupUiState())
    val uiState: StateFlow<GamePlaySetupUiState> = _uiState.asStateFlow()

    init {
        _uiState.update { currentState ->
            currentState.copy(
                favoritePlayerNames = _playerRepository.getAll(),
                favoriteGames = _favoriteGameRepository.getAll(),
                savedGames = _gameRepository.getAll()
            )
        }
    }

    fun onEvent(event: GamePlaySetupUiEvent) {
        when (event) {
            is GamePlaySetupUiEvent.AddPlayer -> addPlayer(event.name)
            is GamePlaySetupUiEvent.RemovePlayer -> removePlayer(event.index)
            is GamePlaySetupUiEvent.AddFavoritePlayer -> addFavoritePlayer(event.name)
            is GamePlaySetupUiEvent.DeleteFavoritePlayer -> deleteFavoritePlayer(event.name)
            is GamePlaySetupUiEvent.DeleteFavoriteGame -> deleteFavoriteGame(event.favoriteGameId)
            is GamePlaySetupUiEvent.SetGame -> setGame(event.game)
            is GamePlaySetupUiEvent.SetPlayers -> setPlayers(event.players)
            is GamePlaySetupUiEvent.DeleteGame -> deleteSavedGame(event.id)
        }
    }

    private fun setGame(newSelectedGame: Game) {
        _uiState.update { currentState ->
            currentState.copy(selectedGame = newSelectedGame)
        }
    }

    private fun addPlayer(newPlayerName: String) {
        _uiState.update { currentState ->
            currentState.copy(playerNames = currentState.playerNames.plus(newPlayerName))
        }
    }

    private fun removePlayer(position: Int) {
        _uiState.update { currentState ->
            currentState.copy(playerNames = currentState.playerNames.filterIndexed { index, _ -> index != position })
        }
    }

    private fun setPlayers(newPlayerNames: List<String>) {
        _uiState.update { currentState ->
            currentState.copy(playerNames = newPlayerNames)
        }
    }

    private fun deleteFavoritePlayer(player: String) {
        _playerRepository.delete(player)
        _uiState.update { currentState ->
            currentState.copy(favoritePlayerNames = _playerRepository.getAll())
        }
    }

    private fun addFavoritePlayer(player: String) {
        if (!_playerRepository.getAll().contains(player)) {
            _playerRepository.save(player)
            _uiState.update { currentState ->
                currentState.copy(favoritePlayerNames = _playerRepository.getAll())
            }
        }
    }

    private fun deleteFavoriteGame(favoriteGameId: Int) {
        _favoriteGameRepository.deleteById(favoriteGameId)
        _uiState.update { currentState ->
            currentState.copy(favoriteGames = _favoriteGameRepository.getAll())
        }
    }

    fun reloadGames() {
        val newGames = _gameRepository.getAll()
        _uiState.update { currentState ->
            currentState.copy(savedGames = newGames)
        }
    }

    private fun deleteSavedGame(id: Int) {
        _gameRepository.deleteById(id)
        val selectedGame = if (_uiState.value.selectedGame.id == id) {
            Game(name = "")
        } else {
            _uiState.value.selectedGame
        }

        val newGames = _gameRepository.getAll()
        _uiState.update { currentState ->
            currentState.copy(
                selectedGame = selectedGame,
                savedGames = newGames
            )
        }
    }
}