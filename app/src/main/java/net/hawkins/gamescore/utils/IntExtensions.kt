package net.hawkins.gamescore.utils

fun Int.isNegative(): Boolean {
    return (this < 0)
}

fun Int.isEven(): Boolean {
    return this % 2 == 0
}
