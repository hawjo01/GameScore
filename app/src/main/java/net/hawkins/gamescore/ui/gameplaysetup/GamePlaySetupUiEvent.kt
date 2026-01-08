package net.hawkins.gamescore.ui.gameplaysetup

import androidx.compose.runtime.Stable
import net.hawkins.gamescore.data.model.Game

@Stable
sealed interface GamePlaySetupUiEvent {
    data class AddPlayer(val name: String) : GamePlaySetupUiEvent
    data class RemovePlayer(val index: Int) : GamePlaySetupUiEvent
    data class AddFavoritePlayer(val name: String) : GamePlaySetupUiEvent
    data class DeleteFavoritePlayer(val name: String) : GamePlaySetupUiEvent
    data class DeleteFavoriteGame(val favoriteGameId: Int) : GamePlaySetupUiEvent
    object RefreshState : GamePlaySetupUiEvent
    data class SetGame(val game: Game) : GamePlaySetupUiEvent
    data class SetPlayers(val players: List<String>) : GamePlaySetupUiEvent
}