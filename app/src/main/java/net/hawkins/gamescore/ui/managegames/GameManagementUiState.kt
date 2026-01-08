package net.hawkins.gamescore.ui.managegames

import net.hawkins.gamescore.data.model.Game

data class GameManagementUiState(val games: List<Game> = emptyList())