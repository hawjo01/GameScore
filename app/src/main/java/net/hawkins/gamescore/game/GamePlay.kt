package net.hawkins.gamescore.game

import net.hawkins.gamescore.data.model.GameRules
import net.hawkins.gamescore.game.Game.Player
import net.hawkins.gamescore.game.type.GameType

open class GamePlay(val gameRules: GameRules) : GameType {
    override fun getName(): String {
        return gameRules.name
    }

    override fun isValidScore(score: String): Boolean {
        val int = score.toIntOrNull() ?: return false

        if (gameRules.constraints.positiveOnly && int < 0) {
            return false
        }

        if (gameRules.constraints.multipleOf != null && int % gameRules.constraints.multipleOf != 0) {
            return false
        }

        return true
    }

    override fun findWinner(players: List<Player>): Player? {
        if (players.isEmpty()) {
            return null
        }

        if (gameRules.constraints.equalHandSizes && !equalNumberOfHands(players)) {
            return null
        }

        val candidate = when (gameRules.objective.type) {
            GameRules.Objective.Type.HIGH_SCORE -> players.maxBy { player -> player.totalScore() }
            GameRules.Objective.Type.LOW_SCORE -> players.minBy { player -> player.totalScore() }
        }

        val candidateTotalScore = candidate.totalScore()
        if (!isGoalMet(candidateTotalScore)) {
            return null
        }

        val numberOfWinners = players.count { player -> player.totalScore() == candidateTotalScore }
        return if (numberOfWinners == 1) {
            candidate
        } else {
            null
        }
    }

    override fun hasWinningThreshold(): Boolean {
        return gameRules.objective.goal != null
    }

    override fun highlightNegativeScore(): Boolean {
        return gameRules.color.negativeScore == GameRules.Colors.Color.RED
    }

    private fun equalNumberOfHands(players: List<Player>): Boolean {
        return players.all { player -> player.scores.size == players[0].scores.size }
    }

    private fun isGoalMet(totalScore: Int): Boolean {
        val goal = gameRules.objective.goal ?: return true

        return when (gameRules.objective.type) {
            GameRules.Objective.Type.HIGH_SCORE -> totalScore >= goal
            GameRules.Objective.Type.LOW_SCORE -> totalScore <= goal
        }
    }
}