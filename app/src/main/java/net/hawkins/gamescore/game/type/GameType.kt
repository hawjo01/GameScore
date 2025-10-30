package net.hawkins.gamescore.game.type

import net.hawkins.gamescore.game.Player

interface GameType {
    fun getName(): String
    fun isValidScore(score: String): Boolean
    fun findWinner(players: List<Player>): Player?
    fun hasWinningThreshold(): Boolean
    fun highlightNegativeScore(): Boolean
}