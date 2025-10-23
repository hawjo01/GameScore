package net.hawkins.gamescore.ui

import android.app.Application
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import net.hawkins.gamescore.Constants.FAVORITE_GAMES_FILENAME
import net.hawkins.gamescore.model.FavoriteGame
import net.hawkins.gamescore.data.FavoriteGameRepository
import net.hawkins.gamescore.data.source.FileFavoriteGameDataSource
import net.hawkins.gamescore.game.Game
import java.io.File


class GamePlayViewModel(application: Application): GameScoreViewModel(application) {
    private val _uiState = MutableStateFlow(GamePlayUiState())
    val uiState: StateFlow<GamePlayUiState> = _uiState.asStateFlow()

    private val _favoriteGameRepository: FavoriteGameRepository

    init {
        val favoriteGameRepositoryFile =
            File(application.applicationContext.filesDir, FAVORITE_GAMES_FILENAME)
        _favoriteGameRepository =
            FavoriteGameRepository(FileFavoriteGameDataSource(favoriteGameRepositoryFile))
    }

    fun setGame(newGame: Game) {
        _uiState.update { currentState ->
            currentState.copy(game = newGame)
        }
    }

    fun saveFavoriteGame(favoriteGame: FavoriteGame) {
        _favoriteGameRepository.addFavoriteGame(favoriteGame)
    }
}