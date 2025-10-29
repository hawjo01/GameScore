package net.hawkins.gamescore.ui

import android.app.Application
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import net.hawkins.gamescore.Constants.FAVORITE_GAMES_FILENAME
import net.hawkins.gamescore.Constants.FAVORITE_PLAYERS_FILENAME
import net.hawkins.gamescore.data.FavoriteGameRepository
import net.hawkins.gamescore.data.FavoritePlayerRepository
import net.hawkins.gamescore.data.source.FileFavoriteGameDataSource
import net.hawkins.gamescore.data.source.FileFavoritePlayerDataSource
import net.hawkins.gamescore.model.FavoriteGame
import java.io.File

class GameSetupViewModel(application: Application) : GameScoreViewModel(application) {
    private val _uiState = MutableStateFlow(GameSetupUiState())
    val uiState: StateFlow<GameSetupUiState> = _uiState.asStateFlow()

    private val _playerRepository: FavoritePlayerRepository
    private val _favoriteGameRepository: FavoriteGameRepository

    init {
        val playerRepositoryFile =
            File(application.applicationContext.filesDir, FAVORITE_PLAYERS_FILENAME)
        _playerRepository =
            FavoritePlayerRepository(FileFavoritePlayerDataSource(playerRepositoryFile))

        val favoriteGameRepositoryFile =
            File(application.applicationContext.filesDir, FAVORITE_GAMES_FILENAME)
        _favoriteGameRepository =
            FavoriteGameRepository(FileFavoriteGameDataSource(favoriteGameRepositoryFile))

        _uiState.update { currentState ->
            currentState.copy(
                favoritePlayerNames = _playerRepository.getPlayers(),
                favoriteGames = _favoriteGameRepository.getFavoriteGames()
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
            currentState.copy(playerNames = currentState.playerNames.filterIndexed { index, name -> index != position })
        }
    }

    fun setPlayers(newPlayerNames: List<String>) {
        _uiState.update { currentState ->
            currentState.copy(playerNames = newPlayerNames)
        }
    }

    fun deleteFavoritePlayer(player: String) {
        _playerRepository.removePlayer(player)
        _uiState.update { currentState ->
            currentState.copy(favoritePlayerNames = _playerRepository.getPlayers())
        }
    }

    fun addFavoritePlayer(player: String) {
        if (!_playerRepository.getPlayers().contains(player)) {
            _playerRepository.addPlayer(player)
            _uiState.update { currentState ->
                currentState.copy(favoritePlayerNames = _playerRepository.getPlayers())
            }
        }
    }

    fun deleteFavoriteGame(favoriteGame: FavoriteGame) {
        _favoriteGameRepository.removeFavoriteGame(favoriteGame)
        _uiState.update { currentState ->
            currentState.copy(favoriteGames = _favoriteGameRepository.getFavoriteGames())
        }
    }
}