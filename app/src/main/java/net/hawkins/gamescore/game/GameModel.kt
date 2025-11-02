package net.hawkins.gamescore.game

data class GameModel(
    val name: String,
    val objective: Objective = Objective(),
    val constraints: Constraints = Constraints(),
    val display: Display = Display()
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
        val allowNegative: Boolean = true,
        val modulus: Int? = null
    )

    data class Display(
        val negative: Type = Type.DEFAULT,
        val positive: Type = Type.DEFAULT
    ) {
        enum class Type {
            DEFAULT,
            POSITIVE,
            NEGATIVE
        }
    }
}
