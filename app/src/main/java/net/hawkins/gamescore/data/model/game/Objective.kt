package net.hawkins.gamescore.data.model.game

data class Objective(
    val type: Type = Type.HIGH_SCORE,
    val goal: Int? = null
) {
    enum class Type {
        HIGH_SCORE,
        LOW_SCORE
    }
}