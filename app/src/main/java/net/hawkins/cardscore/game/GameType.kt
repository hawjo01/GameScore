package net.hawkins.cardscore.game

import androidx.compose.ui.graphics.Color
import net.hawkins.cardscore.data.Player

interface GameType {

    fun getNameId(): Int
    fun isValidScore(score: String): Boolean
    fun findWinner(players: List<Player>): Player?
    fun hasWinningThreshold(): Boolean { return false }

    fun getScoreColor(score: Int): Color { return Color.Unspecified }
}