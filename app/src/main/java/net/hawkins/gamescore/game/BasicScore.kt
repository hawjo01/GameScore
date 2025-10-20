package net.hawkins.gamescore.game

import net.hawkins.gamescore.data.Player

object BasicScore : GameType {
    const val NAME = "Basic Scoring"

    override fun getName(): String {
        return NAME
    }

    override fun hasWinningThreshold(): Boolean {
        return false
    }

    override fun isValidScore(score: String): Boolean {
        return (score.toIntOrNull() != null)
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

    override fun highlightNegativeScore(): Boolean {
        return false
    }
}