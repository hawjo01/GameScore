package net.hawkins.gamescore

object Utils {
    fun isNegativeInt(string: String): Boolean {
        val number = string.toIntOrNull()
        return if (number != null) {
            isNegativeInt(number)
        } else {
            false
        }
    }

    fun isNegativeInt(int: Int): Boolean {
        return (int < 0)
    }
}