package net.hawkins.gamescore.game.type

import net.hawkins.gamescore.game.Player

object BasicScoreHigh : AbstractBasicScore() {
    const val NAME = "Basic Scoring - High"

    override fun getName(): String {
        return NAME
    }

    override fun findWinner(players: List<Player>): Player? {
        if (players.isEmpty()) {
            return null
        }

        val playerWithHighestScore = players.maxBy { player -> player.totalScore() }
        val highestScore = playerWithHighestScore.totalScore()
        val count = players.count { player -> player.totalScore() == highestScore }
        return if (count == 1) {
            playerWithHighestScore
        } else {
            null
        }
    }
}