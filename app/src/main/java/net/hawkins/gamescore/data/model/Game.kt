package net.hawkins.gamescore.data.model

data class Game(
    val name: String,
    val objective: Objective = Objective(),
    val constraints: Constraints = Constraints(),
    val color: Colors = Colors()
) {
    data class Objective(
        val type: Type = Type.HIGH_SCORE,
        val goal: Int? = null
    ) {
        enum class Type {
            HIGH_SCORE,
            LOW_SCORE
        }
    }

    data class Constraints(
        val positiveOnly: Boolean = false,
        val multipleOf: Int? = null,
        val equalHandSizes: Boolean = false
    )

    data class Colors(
        val negativeScore: Color = Color.DEFAULT,
        val positiveScore: Color = Color.DEFAULT
    ) {
        enum class Color {
            DEFAULT,
            RED
        }
    }
}