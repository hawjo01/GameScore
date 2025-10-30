package net.hawkins.gamescore.game.type

object Games {
    val TYPES = listOf(
        TwentyFiveHundred,
        BasicScoreHigh,
        BasicScoreLow
    )

    fun getByName(name: String): GameType {
        return TYPES.first { gameType -> gameType.getName() == name }
    }

    fun isValidType(name: String): Boolean {
        return TYPES.firstOrNull { gameType -> gameType.getName() == name } != null
    }
}