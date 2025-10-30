package net.hawkins.gamescore.game.type

import net.hawkins.gamescore.game.type.GameType
import net.hawkins.gamescore.game.Player

object BasicScoreLow : AbstractBasicScore() {
    const val NAME = "Basic Scoring - Low"

    override fun getName(): String {
        return NAME
    }

    override fun findWinner(players: List<Player>): Player? {
        if (players.isEmpty()) {
            return null
        }

        val playerWithLowestScore = players.minBy { player -> player.totalScore() }
        val lowestScore = playerWithLowestScore.totalScore()
        val count = players.count { player -> player.totalScore() == lowestScore }
        return if (count == 1) {
            playerWithLowestScore
        } else {
            null
        }
    }
}