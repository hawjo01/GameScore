package net.hawkins.gamescore.game

import net.hawkins.gamescore.data.Player

interface GameType {

    fun getName(): String
    fun isValidScore(score: String): Boolean
    fun findWinner(players: List<Player>): Player?
    fun hasWinningThreshold(): Boolean
    fun highlightNegativeScore(): Boolean
}