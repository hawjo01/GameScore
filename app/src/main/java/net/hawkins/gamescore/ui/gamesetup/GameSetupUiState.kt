package net.hawkins.gamescore.ui.gamesetup

import net.hawkins.gamescore.data.model.Game

data class GameSetupUiState(
    val gameId: Int = 0,
    val gameName: String = "",
    val gameObjectiveGoal: Int? = null,
    val gameObjectiveRounds: Int? = null,
    val gameObjectiveType: Game.Objective.Type = Game.Objective.Type.HIGH_SCORE,
    val gameRoundObjectiveGoal: Int? = null,
    val gameRoundObjectiveDisplayValue: String? = null,
    val gameRoundObjectiveDisplayColor: Game.Colors.Color = Game.Colors.Color.DEFAULT,
    val gameConstraintEqualHandSizes: Boolean = true,
    val gameConstraintPositiveOnly: Boolean = false,
    val gameConstraintScoreModulus: Int? = null,
    val gameColorsNegativeScore: Game.Colors.Color = Game.Colors.Color.DEFAULT,
    val gameColorsPositiveScore: Game.Colors.Color = Game.Colors.Color.DEFAULT,
    val isValidName: Boolean = false
)

fun GameSetupUiState.toGame(): Game {
    val game = Game(
        id = this.gameId,
        name = this.gameName.trim(),
        objective = Game.Objective(
            type = this.gameObjectiveType,
            goal = this.gameObjectiveGoal,
            rounds = this.gameObjectiveRounds
        ),
        roundObjective = Game.RoundObjective(
            goal = gameRoundObjectiveGoal,
            displayValue = gameRoundObjectiveDisplayValue,
            displayColor = gameRoundObjectiveDisplayColor
        ),
        constraints = Game.Constraints(
            equalHandSizes = gameConstraintEqualHandSizes,
            positiveOnly = gameConstraintPositiveOnly,
            multipleOf = gameConstraintScoreModulus
        ),
        color = Game.Colors(
            negativeScore = gameColorsNegativeScore,
            positiveScore = gameColorsPositiveScore
        )
    )
    return game
}

fun Game.toGameSetupUiState(isNameValid: Boolean): GameSetupUiState {
    val uiState = GameSetupUiState(
        gameId = this.id,
        gameName = this.name,
        isValidName = isNameValid,
        gameObjectiveRounds = this.objective.rounds,
        gameObjectiveGoal = this.objective.goal,
        gameObjectiveType = this.objective.type,
        gameRoundObjectiveGoal = this.roundObjective.goal,
        gameRoundObjectiveDisplayValue = this.roundObjective.displayValue,
        gameRoundObjectiveDisplayColor = this.roundObjective.displayColor,
        gameConstraintEqualHandSizes = this.constraints.equalHandSizes,
        gameConstraintPositiveOnly = this.constraints.positiveOnly,
        gameConstraintScoreModulus = this.constraints.multipleOf,
        gameColorsNegativeScore = this.color.negativeScore,
        gameColorsPositiveScore = this.color.positiveScore,
    )

    return uiState
}

