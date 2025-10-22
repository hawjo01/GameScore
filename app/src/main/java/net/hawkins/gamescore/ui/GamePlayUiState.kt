package net.hawkins.gamescore.ui

import net.hawkins.gamescore.game.BasicScore
import net.hawkins.gamescore.game.Game

data class GamePlayUiState(
    val game: Game = Game(BasicScore, emptyList())
)
