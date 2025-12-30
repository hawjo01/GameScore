package net.hawkins.gamescore.service

import net.hawkins.gamescore.data.GameProgressRepository
import net.hawkins.gamescore.data.model.GameProgress
import net.hawkins.gamescore.ui.gameplay.GamePlayUiState

class GameProgressService(private val _gameProgressRepository: GameProgressRepository) {

    fun saveGameProgress(uiState: GamePlayUiState) {
        val gameProgress = GameProgress(
            id = 1,
            game = uiState.game,
            players = uiState.players,
            winner = uiState.winner
        )
        _gameProgressRepository.save(gameProgress)
    }

    fun getGameProgress(): GameProgress? {
        return _gameProgressRepository.getById(1)
    }

    fun isGameInProgress(): Boolean {
        val gameProgress = getGameProgress() ?: return false
        if (gameProgress.isComplete()) {
            return false
        }
        gameProgress.players.forEach { player ->
            if (player.scores.isNotEmpty()) {
                return true
            }
        }
        return false
    }
}