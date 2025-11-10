package net.hawkins.gamescore.data

import net.hawkins.gamescore.data.model.Game
import net.hawkins.gamescore.data.model.game.Colors
import net.hawkins.gamescore.data.model.game.Constraints
import net.hawkins.gamescore.data.model.game.Objective
import net.hawkins.gamescore.data.model.game.Rules

object GameRepository {

    fun getAll(): List<Game> {
        return listOf(
            Game(
                name = "Basic Scoring - High",
                id = -1,
                rules = Rules(
                    objective = Objective(
                        type = Objective.Type.HIGH_SCORE
                    )
                )
            ),
            Game(
                name = "2500",
                id = -2,
                rules = Rules(
                    constraints = Constraints(
                        multipleOf = 5,
                        equalHandSizes = true
                    ),
                    objective = Objective(
                        goal = 2500
                    )
                ),
                colors = Colors(
                    negativeScore = Colors.Color.RED
                )
            ),
            Game(
                name = "Basic Scoring - Low",
                id = -3,
                rules = Rules(
                    objective = Objective(
                        type = Objective.Type.LOW_SCORE
                    ),
                )
            )
        )
    }

    fun getDefaultGame(): Game {
        return getAll()[0]
    }

    fun getByName(name: String): Game {
        return getAll().first { game -> game.name == name }
    }

    fun isGame(name: String): Boolean {
        return getAll().firstOrNull { game -> game.name == name } != null
    }
}