package net.hawkins.cardscore

data class Player(val name: String, var scores: List<Int> = listOf()) {

    fun totalScore(): Int {
        var total = 0
        for (score in scores) {
            total += score
        }
        return total
    }

    fun resetScores() {
        scores = listOf()
    }

    fun addScore(score: Int) {
        scores = scores + score
    }
}
