package net.hawkins.gamescore.data

import net.hawkins.gamescore.data.model.Game

class GameRepository {

    fun getAll() : List<Game> {
        return listOf(
            Game(
                name = "Basic Scoring - High",
                constraints = Game.Constraints(
                ),
                objective = Game.Objective(
                    type = Game.Objective.Type.HIGH_SCORE,
                ),
                color = Game.Colors()
            ),
            Game(
                name = "2500",
                constraints = Game.Constraints(
                    multipleOf = 5,
                    equalHandSizes = true
                ),
                objective = Game.Objective(
                    goal = 2500
                ),
                color = Game.Colors(
                    negativeScore = Game.Colors.Color.RED
                )
            ),
            Game(
                name = "Basic Scoring - Low",
                objective = Game.Objective(
                    type = Game.Objective.Type.LOW_SCORE
                ),
            )
        )
    }

    fun getByName(name: String): Game {
        return getAll().first { game -> game.name == name }
    }
}