package net.hawkins.gamescore.game.type

import net.hawkins.gamescore.game.GamePlay
import net.hawkins.gamescore.game.GameModel

object Games {
    val TYPES = listOf<GameType>(
        GamePlay(
            GameModel(
                name = "Basic Scoring - High",
                constraints = GameModel.Constraints(
                ),
                objective = GameModel.Objective(
                    type = GameModel.Objective.Type.HIGH_SCORE,
                ),
                display = GameModel.Display()
            )
        ),
        GamePlay(
            GameModel(
                name = "2500",
                constraints = GameModel.Constraints(
                    modulus = 5
                ),
                objective = GameModel.Objective(
                    goal = 2500
                ),
                display = GameModel.Display(
                    negative = GameModel.Display.Type.NEGATIVE
                )
            )
        ),
        GamePlay(
            GameModel(
                name = "Basic Scoring - Low",
                objective = GameModel.Objective(
                    type = GameModel.Objective.Type.LOW_SCORE
                ),
            )
        )
    )

    fun getByName(name: String): GameType {
        return TYPES.first { gameType -> gameType.getName() == name }
    }

    fun isValidType(name: String): Boolean {
        return TYPES.firstOrNull { gameType -> gameType.getName() == name } != null
    }
}