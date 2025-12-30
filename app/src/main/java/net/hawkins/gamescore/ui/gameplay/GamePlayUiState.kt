package net.hawkins.gamescore.ui.gameplay

import net.hawkins.gamescore.data.model.Game

data class GamePlayUiState(
    val game: Game,
    val players: List<Player>,
    val winner: String? = null
) {
    fun numberOfRounds(): Int {
        return players.maxBy { player -> player.scores.size }.scores.size
    }
}

data class Player(val name: String, val scores: List<Int> = emptyList()) {
    fun totalScore(): Int {
        return scores.sum()
    }
}