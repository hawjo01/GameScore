package net.hawkins.gamescore.game

import net.hawkins.gamescore.data.Player

interface GameType {

    fun getNameId(): Int
    fun isValidScore(score: String): Boolean
    fun findWinner(players: List<Player>): Player?
    fun hasWinningThreshold(): Boolean { return false }
    fun highlightNegativeScore(): Boolean
}