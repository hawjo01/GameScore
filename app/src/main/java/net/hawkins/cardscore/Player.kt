package net.hawkins.cardscore


import androidx.compose.runtime.mutableStateListOf

data class Player(val name: String) {
    val scores = mutableStateListOf<Int>()

    fun totalScore(): Int {
        var total = 0
        for (score in scores) {
            total += score
        }
        return total
    }

    fun resetScores() {
        scores.clear()
    }

    fun addScore(score: Int) {
        scores.add(score)
    }
}
