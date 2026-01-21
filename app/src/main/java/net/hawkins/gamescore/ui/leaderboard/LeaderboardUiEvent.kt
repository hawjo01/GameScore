package net.hawkins.gamescore.ui.leaderboard

import net.hawkins.gamescore.data.model.Game
import net.hawkins.gamescore.ui.gameplay.Player

sealed interface LeaderboardUiEvent {
    data class RefreshLeaderboard(val game: Game, val players: List<Player>) : LeaderboardUiEvent
}