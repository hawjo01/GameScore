package net.hawkins.gamescore.ui

import net.hawkins.gamescore.data.model.FavoriteGame

data class GamePlaySetupUiState(
    val gameName: String = "",
    val playerNames: List<String> = emptyList(),
    val favoritePlayerNames: List<String> = emptyList(),
    val favoriteGames: List<FavoriteGame> = emptyList()
)