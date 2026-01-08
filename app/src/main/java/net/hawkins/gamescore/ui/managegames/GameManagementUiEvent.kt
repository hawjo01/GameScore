package net.hawkins.gamescore.ui.managegames

import androidx.compose.runtime.Stable

@Stable
sealed interface  GameManagementUiEvent {
    data class DeleteGame(val id: Int) : GameManagementUiEvent
    object RefreshState : GameManagementUiEvent
}