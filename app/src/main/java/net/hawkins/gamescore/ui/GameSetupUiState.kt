package net.hawkins.gamescore.ui

import net.hawkins.gamescore.model.FavoriteGame

data class GameSetupUiState(
    val gameName: String = "",
    val playerNames: List<String> = emptyList(),
    val favoritePlayerNames: List<String> = emptyList(),
    val favoriteGames: List<FavoriteGame> = emptyList()
)