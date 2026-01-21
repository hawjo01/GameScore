package net.hawkins.gamescore.ui.leaderboard

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import net.hawkins.gamescore.data.model.Game
import net.hawkins.gamescore.service.LeaderboardService
import net.hawkins.gamescore.ui.AbstractViewModel
import net.hawkins.gamescore.ui.gameplay.Player

class LeaderboardViewModel : AbstractViewModel() {
    private val _uiState = MutableStateFlow(LeaderboardUiState())
    val uiState: StateFlow<LeaderboardUiState> = _uiState.asStateFlow()

    private val leaderboardService = LeaderboardService()

    fun onEvent(event: LeaderboardUiEvent) {
        when (event) {
            is LeaderboardUiEvent.RefreshLeaderboard -> updateLeaderboard(event.game, event.players)
        }
    }

    private fun updateLeaderboard(game: Game, players: List<Player>) {
        val leaderboard = leaderboardService.buildLeaderboard(game, players)
        _uiState.update { currentState ->
            currentState.copy(leaderboard = leaderboard)
        }
    }
}