package net.hawkins.gamescore.ui

import net.hawkins.gamescore.data.model.Game
import net.hawkins.gamescore.data.model.Game.Colors
import net.hawkins.gamescore.data.model.Game.Objective
import kotlin.String

data class GameSetupUiState (
    val game: Game = Game(),
    val mode: Mode = Mode.NEW
) {
    enum class Mode {
        NEW,
        VIEW,
        EDIT
    }
}
//    val name : String = "",
//    val objectiveType : Objective.Type = Objective.Type.HIGH_SCORE,
//    val objectiveGoal : Int? = null,
//    val constraintPositiveOnly : Boolean = false,
//    val constraintMultipleOf: Int? = null,
//    val constraintEqualHandSizes: Boolean = false,
//    val negativeColor: Colors.Color = Colors.Color.DEFAULT,
//    val positiveColor: Colors.Color = Colors.Color.DEFAULT,
