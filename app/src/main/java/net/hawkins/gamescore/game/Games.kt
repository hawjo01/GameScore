package net.hawkins.gamescore.game

object Games {
    val TYPES = listOf(
        TwentyFiveHundred,
        BasicScore
    )

    fun getByName(name: String): GameType {
        return TYPES.first { gameType -> gameType.getName() == name }
    }

    fun isValidGame(name: String): Boolean {
        return TYPES.firstOrNull { gameType -> gameType.getName() == name } != null
    }
}