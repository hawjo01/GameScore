package net.hawkins.gamescore

object Utils {
    fun isNegativeInt(string: String): Boolean {
        val number = string.toIntOrNull()
        if (number != null) {
            return number < 0
        }
        return false
    }

    fun isNegativeInt(int: Int): Boolean {
        return (int < 0)
    }
}