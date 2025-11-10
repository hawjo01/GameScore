package net.hawkins.gamescore.ui

import net.hawkins.gamescore.game.GamePlay
import net.hawkins.gamescore.data.GameRepository

data class GamePlayUiState(
    val gamePlay: GamePlay = GamePlay(GameRepository.getDefaultGame(), emptyList())
)
