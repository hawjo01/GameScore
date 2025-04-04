package net.hawkins.cardscore

class Rummy {

    companion object {
        fun isValidScore(score: String): Boolean {
            return (score.toIntOrNull() != null && score.toInt() % 5 == 0)
        }
    }
}