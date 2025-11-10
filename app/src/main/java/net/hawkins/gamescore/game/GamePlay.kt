package net.hawkins.gamescore.game

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import net.hawkins.gamescore.data.model.Game

class GamePlay(private val game: Game, playerNames: List<String>) {

    private val _winner = mutableStateOf<Player?>(null)
    val players: List<Player> = playerNames.map { playerName -> Player(playerName) }

    fun resetGame() {
        _winner.value = null
        players.forEach { player -> player.resetScores() }
    }

    fun getWinner(): Player? {
        return _winner.value
    }

    fun determineWinner(): Player? {
        _winner.value = findWinner()
        return _winner.value
    }

    private fun findWinner(): Player? {
        if (players.isEmpty()) {
            return null
        }

        if (game.constraints.equalHandSizes && !equalNumberOfHands(players)) {
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
            candidate
        } else {
            null
        }
    }

    private fun equalNumberOfHands(players: List<Player>): Boolean {
        return players.all { player -> player.scores.size == players[0].scores.size }
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

    fun highlightNegativeScore(): Boolean {
        return game.color.negativeScore != Game.Colors.Color.DEFAULT
    }

    fun hasWinningThreshold(): Boolean {
        return game.objective.goal != null
    }

    fun getGameName(): String {
        return game.name
    }

    fun numberOfRounds(): Int {
        return players.maxBy { player -> player.scores.size }.scores.size
    }

    data class Player(val name: String) {
        val scores = mutableStateListOf<Int>()

        fun totalScore(): Int {
            return scores.sum()
        }

        fun resetScores() {
            scores.clear()
        }

        fun addScore(score: Int) {
            scores.add(score)
        }

        fun changeScore(newScore: Int, scoreIndex: Int) {
            scores[scoreIndex] = newScore
        }

        fun deleteScore(scoreIndex: Int) {
            scores.removeAt(scoreIndex)
        }
    }
}