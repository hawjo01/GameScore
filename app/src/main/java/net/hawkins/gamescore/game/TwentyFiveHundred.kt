package net.hawkins.gamescore.game

import net.hawkins.gamescore.R
import net.hawkins.gamescore.data.Player

class TwentyFiveHundred : GameType {

    override fun hasWinningThreshold(): Boolean {
        return true
    }

    override fun highlightNegativeScore(): Boolean {
        return true
    }

    override fun getNameId(): Int {
        return R.string.twenty_five_hundred
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

        val playerWithHighestScore = players.maxBy { it.totalScore() }
        return if (playerWithHighestScore.totalScore() >= 2500) {
            playerWithHighestScore
        } else {
            null
        }
    }
}