package net.hawkins.gamescore.game.type

abstract class AbstractBasicScore : GameType {
    override fun hasWinningThreshold(): Boolean {
        return false
    }

    override fun isValidScore(score: String): Boolean {
        return (score.toIntOrNull() != null)
    }

    override fun highlightNegativeScore(): Boolean {
        return false
    }
}