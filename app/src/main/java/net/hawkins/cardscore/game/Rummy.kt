package net.hawkins.cardscore.game

import net.hawkins.cardscore.R
import net.hawkins.cardscore.data.Player

class Rummy: GameType {

    override fun hasWinningThreshold(): Boolean { return true }

    override fun getNameId(): Int { return R.string.rummy }

    override fun isValidScore(score: String): Boolean {
            return (score.toIntOrNull() != null && score.toInt() % 5 == 0)
    }

    override fun findWinner(players: List<Player>): Player? {
        var playerWithMaxNumberOfHands: Player? = players.maxByOrNull { player -> player.scores.size }
        if (playerWithMaxNumberOfHands == null) {
            return null
        }

        var maxNumberOfHands = playerWithMaxNumberOfHands.scores.size
        var handComplete = players.all { player -> player.scores.size == maxNumberOfHands }
        if (!handComplete) {
            return null
        }

        val playerWithHighestScore = players.maxBy { it.totalScore() }
        if (playerWithHighestScore.totalScore() >= 1500) {
            return playerWithHighestScore
        } else {
            return null
        }
    }
}