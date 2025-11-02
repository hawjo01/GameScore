package net.hawkins.gamescore.ui

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import net.hawkins.gamescore.data.GameRepository
import net.hawkins.gamescore.data.model.Game
import net.hawkins.gamescore.data.model.Game.Colors
import net.hawkins.gamescore.data.model.Game.Constraints
import net.hawkins.gamescore.data.model.Game.Objective
import javax.inject.Inject

@HiltViewModel
class GameSetupViewModel @Inject constructor(
    private val _gameRepository: GameRepository,
) : AbstractViewModel() {

    private val _uiState = MutableStateFlow(GameSetupUiState())
    val uiState: StateFlow<GameSetupUiState> = _uiState.asStateFlow()

    fun saveGame() {
        _gameRepository.save(_uiState.value.toGame())
    }

    fun setGameName(newName: String) {
        _uiState.update { currentState ->
            currentState.copy(name = newName)
        }
    }

    fun setConstraintAllowNegative(state: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(constraintPositiveOnly = state)
        }
    }

    fun setConstraintEqualHandSizes(state: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(constraintEqualHandSizes = state)
        }
    }

    fun setObjectiveType(newType: Objective.Type) {
        _uiState.update { currentState ->
            currentState.copy(objectiveType = newType)
        }
    }

    fun setObjectiveGoal(newGoal: Int?) {
        _uiState.update { currentState ->
            currentState.copy(objectiveGoal = newGoal)
        }
    }

    fun setConstraintModulus(newGoal: Int?) {
        _uiState.update { currentState ->
            currentState.copy(constraintMultipleOf = newGoal)
        }
    }

    fun setDisplayNegative(value: Colors.Color) {
        _uiState.update { currentState ->
            currentState.copy(negativeColor = value)
        }
    }

    fun setDisplayPositive(value: Colors.Color) {
        _uiState.update { currentState ->
            currentState.copy(positiveColor = value)
        }
    }

    fun setGame(game: Game) {
        _uiState.update { currentState ->
            currentState.copy(
                name = game.name,
                objectiveGoal = game.objective.goal,
                objectiveType = game.objective.type,
                constraintEqualHandSizes = game.constraints.equalHandSizes,
                constraintPositiveOnly = game.constraints.positiveOnly,
                constraintMultipleOf = game.constraints.multipleOf,
                negativeColor = game.color.negativeScore,
                positiveColor = game.color.positiveScore,
                mode = GameSetupUiState.Mode.VIEW
            )
        }
    }

    fun setMode(newMode:  GameSetupUiState.Mode) {
        _uiState.update { currentState ->
            currentState.copy(
                mode = newMode
            )
        }
    }


    fun getGame(): Game {
        return _uiState.value.toGame()
    }
}


fun GameSetupUiState.toGame(): Game {
    return Game(
        name = this.name,
        objective = Objective(
            type = objectiveType,
            goal = objectiveGoal
        ),
        constraints = Constraints(
            positiveOnly = constraintPositiveOnly,
            equalHandSizes = constraintEqualHandSizes,
            multipleOf = constraintMultipleOf
        ),
        color = Colors(
            negativeScore = negativeColor,
            positiveScore = positiveColor
        )
    )
}