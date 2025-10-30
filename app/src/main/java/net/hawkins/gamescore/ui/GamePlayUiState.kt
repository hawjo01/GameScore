package net.hawkins.gamescore.ui

import net.hawkins.gamescore.game.type.BasicScoreHigh
import net.hawkins.gamescore.game.Game

data class GamePlayUiState(
    val game: Game = Game(BasicScoreHigh, emptyList())
)
