package net.hawkins.gamescore.ui.gameplay

import androidx.compose.runtime.Stable
import net.hawkins.gamescore.data.model.Game

@Stable
sealed interface GamePlayUiEvent {
    data class AddScore(val seatIndex: Int, val score: Int) : GamePlayUiEvent
    data class DeleteScore(val seatIndex: Int, val roundIndex: Int) : GamePlayUiEvent
    data class ChangeScore(val seatIndex: Int, val roundIndex: Int, val newScore: Int) :
        GamePlayUiEvent

    data class SaveFavoriteGame(val name: String) : GamePlayUiEvent
    data class StartGame(val game: Game, val playerNames: List<String>) : GamePlayUiEvent
    object RefreshState : GamePlayUiEvent
    object ResetGame : GamePlayUiEvent
    object DetermineWinner : GamePlayUiEvent
}