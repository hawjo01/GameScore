package net.hawkins.gamescore.ui

import net.hawkins.gamescore.data.GameRepository
import net.hawkins.gamescore.game.GamePlay

data class GamePlayUiState(
    val gamePlay: GamePlay = GamePlay(GameRepository.getDefaultGame(), emptyList())
)