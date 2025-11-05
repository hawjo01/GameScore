package net.hawkins.gamescore.game.type

import net.hawkins.gamescore.game.Player

object TwentyFiveHundred : GameType {

    const val NAME = "2500"

    override fun getName(): String {
        return NAME
    }

    override fun hasWinningThreshold(): Boolean {
        return true
    }

    override fun highlightNegativeScore(): Boolean {
        return true
    }

    override fun isValidScore(score: String): Boolean {
        return (score.toIntOrNull() != null && score.toInt() % 5 == 0)
    }

    override fun findWinner(players: List<Player>): Player? {
        val playerWithMaxNumberOfHands: Player? =
            players.maxByOrNull { player -> player.scores.size }
        if (playerWithMaxNumberOfHands == null) {
            return null
        }

        val maxNumberOfHands = playerWithMaxNumberOfHands.scores.size
        val handComplete = players.all { player -> player.scores.size == maxNumberOfHands }
        if (!handComplete) {
            return null
        }

        val highestScore = players.maxOf { player -> player.totalScore() }
        val playersWithHighScore = players.filter { player -> player.totalScore() == highestScore }
        if (playersWithHighScore.size > 1) {
            return null
        }

        return if (playersWithHighScore[0].totalScore() >= 2500) {
            playersWithHighScore[0]
        } else {
            null
        }
    }
}