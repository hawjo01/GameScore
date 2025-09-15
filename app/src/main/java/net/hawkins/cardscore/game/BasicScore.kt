package net.hawkins.cardscore.game

import net.hawkins.cardscore.R
import net.hawkins.cardscore.data.Player

class BasicScore: GameType {

    override fun getNameId(): Int { return R.string.basic_scoring }

    override fun isValidScore(score: String): Boolean {
        return (score.toIntOrNull() != null)
    }

    override fun findWinner(players: List<Player>): Player? {
        if (players.isEmpty()) {
            return null
        }

        val playerWithHighestScore = players.maxBy { player -> player.totalScore() }
        val highestScore = playerWithHighestScore.totalScore()
        val count = players.count{ player -> player.totalScore() == highestScore }
        return if (count == 1) {
            playerWithHighestScore
        } else {
            null
        }
    }
}