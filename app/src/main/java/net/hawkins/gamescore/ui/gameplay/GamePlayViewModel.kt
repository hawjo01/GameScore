package net.hawkins.gamescore.ui.gameplay

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import net.hawkins.gamescore.data.FavoriteGameRepository
import net.hawkins.gamescore.data.model.FavoriteGame
import net.hawkins.gamescore.data.model.Game
import net.hawkins.gamescore.game.GamePlay
import net.hawkins.gamescore.ui.AbstractViewModel
import javax.inject.Inject


@HiltViewModel
class GamePlayViewModel @Inject constructor(
    private val _favoriteGameRepository: FavoriteGameRepository,
) : AbstractViewModel() {
    private val _uiState = MutableStateFlow(GamePlayUiState())
    val uiState: StateFlow<GamePlayUiState> = _uiState.asStateFlow()

    fun setGame(newGamePlay: GamePlay) {
        _uiState.update { currentState ->
            currentState.copy(gamePlay = newGamePlay)
        }
    }

    fun saveFavoriteGame(favoriteGame: FavoriteGame) {
        _favoriteGameRepository.save(favoriteGame)
    }

    fun updateGame(newGame: Game) {
        val players = _uiState.value.gamePlay.players
        _uiState.update { currentState ->
            currentState.copy(
                gamePlay = GamePlay(newGame, players.map { player -> player.name })

            )
        }
        players.forEachIndexed { playerIndex, player ->
            player.scores.forEach { score ->
                _uiState.value.gamePlay.players[playerIndex].addScore(score)
            }
        }
    }
}