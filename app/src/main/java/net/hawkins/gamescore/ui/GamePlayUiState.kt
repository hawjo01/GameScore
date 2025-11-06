package net.hawkins.gamescore.ui

import net.hawkins.gamescore.game.Game
import net.hawkins.gamescore.game.type.Games

data class GamePlayUiState(
    val game: Game = Game(Games.getDefaultGame(), emptyList())
)
