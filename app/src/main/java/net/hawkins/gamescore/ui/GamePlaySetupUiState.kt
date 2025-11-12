package net.hawkins.gamescore.ui

import net.hawkins.gamescore.data.model.FavoriteGame
import net.hawkins.gamescore.data.model.Game

data class GamePlaySetupUiState(
    val game: Game = Game(name = ""),
    val playerNames: List<String> = emptyList(),
    val favoritePlayerNames: List<String> = emptyList(),
    val favoriteGames: List<FavoriteGame> = emptyList(),
    val savedGames: List<Game> = emptyList()
)