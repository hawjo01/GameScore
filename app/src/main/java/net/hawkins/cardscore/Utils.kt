package net.hawkins.cardscore

object Utils {
    fun isNegativeInt(string: String): Boolean {
        val number = string.toIntOrNull()
        if (number != null) {
            return number < 0
        }
        return false
    }
}