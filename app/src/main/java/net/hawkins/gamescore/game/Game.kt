package net.hawkins.gamescore.game

import androidx.compose.runtime.mutableStateOf

class Game(private val gameType: GameType, playerNames: List<String>) {

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
        _winner.value = gameType.findWinner(players)
        return _winner.value
    }

    fun isValidScore(score: String): Boolean {
        return gameType.isValidScore(score)
    }

    fun highlightNegativeScore(): Boolean {
        return gameType.highlightNegativeScore()
    }

    fun hasWinningThreshold(): Boolean {
        return gameType.hasWinningThreshold()
    }

    fun getGameName(): String {
        return gameType.getName()
    }
}