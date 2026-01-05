package net.hawkins.gamescore.service

import androidx.compose.ui.graphics.Color
import net.hawkins.gamescore.data.model.Game
import net.hawkins.gamescore.ui.gameplay.Player
import net.hawkins.gamescore.ui.gameplay.Score

class GamePlayService(val game: Game) {
    fun determineWinner(players: List<Player>): String? {

        if (players.isEmpty()) {
            return null
        }

        if (game.constraints.equalHandSizes && !equalNumberOfHands(players)) {
            return null
        }

        if (!isRoundsObjectiveMet(players)) {
            return null
        }

        val candidate = when (game.objective.type) {
            Game.Objective.Type.HIGH_SCORE -> players.maxBy { player -> player.totalScore() }
            Game.Objective.Type.LOW_SCORE -> players.minBy { player -> player.totalScore() }
        }

        val candidateTotalScore = candidate.totalScore()
        if (!isGoalMet(candidateTotalScore)) {
            return null
        }

        val numberOfWinners = players.count { player -> player.totalScore() == candidateTotalScore }
        return if (numberOfWinners == 1) {
            candidate.name
        } else {
            null
        }
    }

    private fun equalNumberOfHands(players: List<Player>): Boolean {
        return players.all { player -> player.scores.size == players[0].scores.size }
    }

    private fun isRoundsObjectiveMet(players: List<Player>): Boolean {
        if (game.objective.rounds == null) {
            return true
        }

        val playersWithAllRounds = players.filter { player -> player.scores.size == game.objective.rounds }
        return playersWithAllRounds.size == players.size
    }

    private fun isGoalMet(totalScore: Int): Boolean {
        val goal = game.objective.goal ?: return true

        return when (game.objective.type) {
            Game.Objective.Type.HIGH_SCORE -> totalScore >= goal
            Game.Objective.Type.LOW_SCORE -> totalScore <= goal
        }
    }

    fun isValidScore(score: String): Boolean {
        val int = score.toIntOrNull() ?: return false

        if (game.constraints.positiveOnly && int < 0) {
            return false
        }

        if (game.constraints.multipleOf != null && int % game.constraints.multipleOf != 0) {
            return false
        }

        return true
    }

    fun getScoreColor(score: Int): Color {
        return if (score < 0) {
            when (game.color.negativeScore) {
                Game.Colors.Color.DEFAULT -> Color.Unspecified
                Game.Colors.Color.RED -> Color.Red
                Game.Colors.Color.GREEN -> Color.Green

            }
        } else {
            when (game.color.positiveScore) {
                Game.Colors.Color.DEFAULT -> Color.Unspecified
                Game.Colors.Color.RED -> Color.Red
                Game.Colors.Color.GREEN -> Color.Green
            }
        }
    }

    fun isManualWinner(): Boolean {
        return game.objective.goal == null && game.objective.rounds == null
    }

    fun buildScore(score: Int): Score {
        val color = getScoreColor(score)
        return Score(value = score, color = color)
    }
}