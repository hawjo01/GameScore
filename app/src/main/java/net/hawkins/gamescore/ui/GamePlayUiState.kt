package net.hawkins.gamescore.ui

import androidx.compose.ui.graphics.Color
import net.hawkins.gamescore.data.model.Game
import net.hawkins.gamescore.game.GamePlay

data class GamePlayUiState(
    val gamePlay: GamePlay = GamePlay(Game(name = ""), emptyList())
)