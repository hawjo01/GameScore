package net.hawkins.gamescore.data.model

data class Game(
    override var id: Int = 0,
    val name: String = "",
    val objective: Objective = Objective(),
    val roundObjective: RoundObjective = RoundObjective(),
    val constraints: Constraints = Constraints(),
    val color: Colors = Colors()
) : Idable {

    data class Objective(
        val type: Type = Type.HIGH_SCORE,
        val goal: Int? = null,
        val rounds: Int? = null
    ) {
        enum class Type {
            HIGH_SCORE,
            LOW_SCORE
        }
    }

    data class RoundObjective(
        val goal: Int? = null,
        val displayValue: String? = null,
        val displayColor: Colors.Color = Colors.Color.DEFAULT
    )

    data class Constraints(
        val positiveOnly: Boolean = false,
        val multipleOf: Int? = null,
        val equalHandSizes: Boolean = true
    )

    data class Colors(
        val negativeScore: Color = Color.DEFAULT,
        val positiveScore: Color = Color.DEFAULT
    ) {
        enum class Color {
            DEFAULT,
            GREEN,
            RED
        }
    }
}