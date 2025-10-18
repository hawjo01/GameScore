package net.hawkins.gamescore.ui

import net.hawkins.gamescore.game.BasicScore
import net.hawkins.gamescore.game.Game

data class GameUiState(
    val game: Game = Game(BasicScore, emptyList())
)
