package net.hawkins.gamescore.ui.gamesetup

import androidx.compose.runtime.Stable
import net.hawkins.gamescore.data.model.Game

@Stable
sealed interface GameSetupUiEvent {
    data class SetGameName(val name: String) : GameSetupUiEvent
    data class SetConstraintPositiveOnlyScores(val positiveOnly: Boolean) : GameSetupUiEvent
    data class SetConstraintEqualHandSizes(val requireEqualHandSizes: Boolean) : GameSetupUiEvent
    data class SetConstraintScoreModulus(val modulus: Int?) : GameSetupUiEvent
    data class SetObjectiveGoal(val goal: Int?) : GameSetupUiEvent
    data class SetObjectiveType(val type: Game.Objective.Type) : GameSetupUiEvent
    data class SetDisplayNegativeColor(val color: Game.Colors.Color) : GameSetupUiEvent
    data class SetDisplayPositiveColor(val color: Game.Colors.Color) : GameSetupUiEvent
    object NewGame : GameSetupUiEvent
    data class SetGame(val game: Game) : GameSetupUiEvent
    data class SetObjectiveRounds(val rounds: Int?) : GameSetupUiEvent
    data class SetRoundObjectiveGoal(val goal: Int?) : GameSetupUiEvent
    data class SetRoundObjectiveDisplayValue(val value: String?) : GameSetupUiEvent
    data class SetRoundObjectiveDisplayColor(val color: Game.Colors.Color) : GameSetupUiEvent
}