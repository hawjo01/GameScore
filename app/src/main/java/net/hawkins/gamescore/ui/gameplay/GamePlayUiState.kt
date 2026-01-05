package net.hawkins.gamescore.ui.gameplay

import androidx.compose.ui.graphics.Color
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

data class Score(val value: Int, @Transient val color: Color = Color.Unspecified, @Transient val displayValue: String? = null)

data class Player(val name: String, val scores: List<Score> = emptyList()) {
    fun totalScore(): Int {
        return scores.sumOf { it.value }
    }
}