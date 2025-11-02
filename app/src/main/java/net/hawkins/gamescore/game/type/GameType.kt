package net.hawkins.gamescore.game.type

import net.hawkins.gamescore.game.Game

interface GameType {
    fun getName(): String
    fun isValidScore(score: String): Boolean
    fun findWinner(players: List<Game.Player>): Game.Player?
    fun hasWinningThreshold(): Boolean
    fun highlightNegativeScore(): Boolean
}