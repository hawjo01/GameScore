package net.hawkins.gamescore.game

import net.hawkins.gamescore.game.type.GameType

open class GamePlay( val gameModel: GameModel) : GameType {
    override fun getName(): String {
        return gameModel.name
    }

    override fun isValidScore(score: String): Boolean {
        val int = score.toIntOrNull()
        if (int == null) {
            return false
        }

        if (!gameModel.constraints.allowNegative && int < 0) {
            return false
        }

        if (gameModel.constraints.modulus != null &&  int % gameModel.constraints.modulus != 0){
            return false
        }

        return true
    }

    override fun findWinner(players: List<Game.Player>): Game.Player? {
        return if (gameModel.objective.type == GameModel.Objective.Type.HIGH_SCORE) {
            findHighScoreWinner(players)
        } else {
            findLowScoreWinner(players)
        }
    }

    override fun hasWinningThreshold(): Boolean {
        return gameModel.objective.goal == null
    }

    override fun highlightNegativeScore(): Boolean {
        return gameModel.display.negative == GameModel.Display.Type.NEGATIVE
    }

    private fun findHighScoreWinner(players: List<Game.Player>): Game.Player? {
        if (players.isEmpty()) {
            return null
        }

        val playerWithHighestScore = players.maxBy { player -> player.totalScore() }
        val highestScore = playerWithHighestScore.totalScore()
        val goal = gameModel.objective.goal
        if (goal != null && highestScore < goal) {
            return null
        }

        val count = players.count { player -> player.totalScore() == highestScore }
        return if (count == 1) {
            playerWithHighestScore
        } else {
            null
        }
    }

    private fun findLowScoreWinner(players: List<Game.Player>) : Game.Player? {
        if (players.isEmpty()) {
            return null
        }

        val playerWithLowestScore = players.minBy { player -> player.totalScore() }
        val lowestScore = playerWithLowestScore.totalScore()
        val count = players.count { player -> player.totalScore() == lowestScore }
        return if (count == 1) {
            playerWithLowestScore
        } else {
            null
        }
    }
}