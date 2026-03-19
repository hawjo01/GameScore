package net.hawkins.gamescore.utils

fun String.isNegativeInt(): Boolean {
    val number = this.toIntOrNull()
    return number?.isNegative() ?: false
}

fun CharSequence.isNegativeInt(): Boolean {
    return this.toString().isNegativeInt()
}

fun CharSequence.toInt(): Int {
    return this.toString().toIntOrNull() ?: 0
}

fun String.trimToNull(): String? {
    val trimmed = this.trim()
    return trimmed.ifEmpty { null }
}