package net.hawkins.gamescore.ui

import android.app.Application
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import net.hawkins.gamescore.data.FavoriteGameRepository
import net.hawkins.gamescore.data.FavoritePlayerRepository
import net.hawkins.gamescore.data.Repository
import net.hawkins.gamescore.data.model.FavoriteGame

class GameSetupViewModel(application: Application) : GameScoreViewModel(application) {
    private val _uiState = MutableStateFlow(GameSetupUiState())
    val uiState: StateFlow<GameSetupUiState> = _uiState.asStateFlow()

    private val _playerRepository: FavoritePlayerRepository
    private val _favoriteGameRepository: FavoriteGameRepository

    init {
        val repositoryFactory = Repository.Factory(application)
        _playerRepository = repositoryFactory.getFavoritePlayerRepository()
        _favoriteGameRepository = repositoryFactory.getFavoriteGameRepository()

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
        _favoriteGameRepository.delete(favoriteGame)
        _uiState.update { currentState ->
            currentState.copy(favoriteGames = _favoriteGameRepository.getAll())
        }
    }
}