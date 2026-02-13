package net.hawkins.gamescore

import net.hawkins.gamescore.data.model.Game
import net.hawkins.gamescore.ui.gameplay.Player
import net.hawkins.gamescore.ui.gameplay.Score
import kotlin.collections.plus

class TestData {
    companion object {

        fun getFiveCrowns(): Game {
            return Game(
                name = "Five Crowns",
                objective = Game.Objective(
                    type = Game.Objective.Type.LOW_SCORE,
                    rounds = 11

                ),
                roundObjective = Game.RoundObjective(
                    goal = 0,
                    displayValue = "Win",
                    displayColor = Game.Colors.Color.GREEN
                ),
                constraints = Game.Constraints(
                    positiveOnly = true
                )
            )
        }

        fun createPlayer(name: String, values: List<Int> = emptyList()): Player {
            val scores = values.map { value -> Score(value = value) }
            return Player(name, scores)
        }
    }
}