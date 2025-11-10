package net.hawkins.gamescore.data.model.game

data class Constraints(
    val positiveOnly: Boolean = false,
    val multipleOf: Int? = null,
    val equalHandSizes: Boolean = false
)