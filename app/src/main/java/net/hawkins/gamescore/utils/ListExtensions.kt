package net.hawkins.gamescore.utils

fun <T> List<T>.replaceElementAtIndex(index: Int, newItem: T): List<T> {
    return this.mapIndexed { i, item ->
        if (i == index) newItem else item
    }
}

fun <T> List<T>.removeElementAtIndex(index: Int): List<T> {
    return this.filterIndexed { i, _ -> i != index }
}