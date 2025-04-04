package net.hawkins.cardscore.game

import net.hawkins.cardscore.data.Player

interface GameType {

    fun getNameId(): Int
    fun isValidScore(score: String): Boolean
    fun findWinner(players: List<Player>): Player?
    fun hasWinningThreshold(): Boolean { return false }
}