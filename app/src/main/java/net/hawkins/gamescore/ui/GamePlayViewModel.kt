package net.hawkins.gamescore.ui

import android.app.Application
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import net.hawkins.gamescore.data.FavoriteGameRepository
import net.hawkins.gamescore.data.Repository
import net.hawkins.gamescore.game.Game
import net.hawkins.gamescore.model.FavoriteGame


class GamePlayViewModel(application: Application) : GameScoreViewModel(application) {
    private val _uiState = MutableStateFlow(GamePlayUiState())
    val uiState: StateFlow<GamePlayUiState> = _uiState.asStateFlow()

    private val _favoriteGameRepository: FavoriteGameRepository =
        Repository.Factory(application).getFavoriteGameRepository()

    fun setGame(newGame: Game) {
        _uiState.update { currentState ->
            currentState.copy(game = newGame)
        }
    }

    fun saveFavoriteGame(favoriteGame: FavoriteGame) {
        _favoriteGameRepository.save(favoriteGame)
    }
}