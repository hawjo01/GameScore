package net.hawkins.gamescore.game.type

import net.hawkins.gamescore.data.model.GameRules
import net.hawkins.gamescore.game.GamePlay

object Games {
    val TYPES = listOf<GameType>(
        GamePlay(
            GameRules(
                name = "Basic Scoring - High",
                constraints = GameRules.Constraints(
                ),
                objective = GameRules.Objective(
                    type = GameRules.Objective.Type.HIGH_SCORE,
                ),
                color = GameRules.Colors()
            )
        ),
        GamePlay(
            GameRules(
                name = "2500",
                constraints = GameRules.Constraints(
                    multipleOf = 5,
                    equalHandSizes = true
                ),
                objective = GameRules.Objective(
                    goal = 2500
                ),
                color = GameRules.Colors(
                    negativeScore = GameRules.Colors.Color.RED
                )
            )
        ),
        GamePlay(
            GameRules(
                name = "Basic Scoring - Low",
                objective = GameRules.Objective(
                    type = GameRules.Objective.Type.LOW_SCORE
                ),
            )
        )
    )

    fun getDefaultGame(): GameType {
        return TYPES[0]
    }

    fun getByName(name: String): GameType {
        return TYPES.first { gameType -> gameType.getName() == name }
    }

    fun isValidType(name: String): Boolean {
        return TYPES.firstOrNull { gameType -> gameType.getName() == name } != null
    }
}