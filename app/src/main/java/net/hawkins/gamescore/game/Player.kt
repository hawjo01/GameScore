package net.hawkins.gamescore.game

import androidx.compose.runtime.mutableStateListOf

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
}