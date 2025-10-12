package net.hawkins.gamescore.game

import net.hawkins.gamescore.R
import net.hawkins.gamescore.data.Player

class TwentyFiveHundred : GameType {

    companion object {
        const val GAME_ID = 1
    }

    override fun getTypeId(): Int {
        return GAME_ID
    }

    override fun hasWinningThreshold(): Boolean {
        return true
    }

    override fun highlightNegativeScore(): Boolean {
        return true
    }

    override fun getNameResourceId(): Int {
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