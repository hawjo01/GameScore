package net.hawkins.gamescore.utils

fun String.isNegativeInt(): Boolean {
    val number = this.toIntOrNull()
    return number?.isNegative() ?: false
}