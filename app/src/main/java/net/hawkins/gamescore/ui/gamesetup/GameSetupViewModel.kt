package net.hawkins.gamescore.ui.gamesetup

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import net.hawkins.gamescore.data.GameRepository
import net.hawkins.gamescore.data.model.Game
import net.hawkins.gamescore.ui.AbstractViewModel
import net.hawkins.gamescore.utils.trimToNull
import javax.inject.Inject

@HiltViewModel
class GameSetupViewModel @Inject constructor(
    private val _gameRepository: GameRepository,
) : AbstractViewModel() {

    private val _uiState = MutableStateFlow(GameSetupUiState())
    val uiState: StateFlow<GameSetupUiState> = _uiState.asStateFlow()

    fun saveGame(): Boolean {
        if (_uiState.value.game.name.isBlank()) {
            _uiState.update { currentState ->
                currentState.copy(
                    isValidName = false
                )
            }
            return false
        }
        _gameRepository.save(_uiState.value.game)
        return true
    }

    fun onEvent(event: GameSetupUiEvent) {
        when (event) {
            is GameSetupUiEvent.SetGameName -> setGameName(event.name)
            is GameSetupUiEvent.SetConstraintPositiveOnlyScores -> setConstraintAllowNegative(event.positiveOnly)
            is GameSetupUiEvent.SetConstraintEqualHandSizes -> setConstraintEqualHandSizes(event.requireEqualHandSizes)
            is GameSetupUiEvent.SetConstraintScoreModulus -> setConstraintModulus(event.modulus)
            is GameSetupUiEvent.SetObjectiveGoal -> setObjectiveGoal(event.goal)
            is GameSetupUiEvent.SetObjectiveType -> setObjectiveType(event.type)
            is GameSetupUiEvent.SetObjectiveRounds -> setObjectiveRounds(event.rounds)
            is GameSetupUiEvent.SetRoundObjectiveGoal -> setRoundObjectiveGoal(event.goal)
            is GameSetupUiEvent.SetRoundObjectiveDisplayValue -> setRoundObjectiveDisplayValue(event.value)
            is GameSetupUiEvent.SetRoundObjectiveDisplayColor -> setRoundObjectiveDisplayColor(event.color)
            is GameSetupUiEvent.SetDisplayNegativeColor -> setDisplayNegative(event.color)
            is GameSetupUiEvent.SetDisplayPositiveColor -> setDisplayPositive(event.color)
            is GameSetupUiEvent.NewGame -> newGame()
            is GameSetupUiEvent.SetGame -> updateUiState(event.game)
        }
    }

    private fun setGameName(newName: String) {
        val currentGame = _uiState.value.game
        val newGame = currentGame.copy(name = newName)
        updateUiState(newGame)
    }

    private fun setConstraintAllowNegative(state: Boolean) {
        val currentGame = _uiState.value.game
        val currentConstraints = currentGame.constraints
        val newConstraints = currentConstraints.copy(positiveOnly = state)
        val newGame = currentGame.copy(constraints = newConstraints)
        updateUiState(newGame)
    }

    private fun setConstraintEqualHandSizes(state: Boolean) {
        val currentGame = _uiState.value.game
        val currentConstraints = currentGame.constraints
        val newConstraints = currentConstraints.copy(equalHandSizes = state)
        val newGame = currentGame.copy(constraints = newConstraints)
        updateUiState(newGame)
    }

    private fun setConstraintModulus(newMultipleOf: Int?) {
        val currentGame = _uiState.value.game
        val currentConstraints = currentGame.constraints
        val newConstraints = currentConstraints.copy(multipleOf = newMultipleOf)
        val newGame = currentGame.copy(constraints = newConstraints)
        updateUiState(newGame)
    }

    private fun setObjectiveType(newType: Game.Objective.Type) {
        val currentGame = _uiState.value.game
        val currentObjective = currentGame.objective
        val newObjective = currentObjective.copy(type = newType)
        val newGame = currentGame.copy(objective = newObjective)
        updateUiState(newGame)
    }

    private fun setObjectiveRounds(rounds: Int?) {
        val currentGame = _uiState.value.game
        val currentObjective = currentGame.objective
        val newObjective = currentObjective.copy(rounds = rounds)
        val newGame = currentGame.copy(objective = newObjective)
        updateUiState(newGame)
    }

    private fun setObjectiveGoal(newGoal: Int?) {
        val currentGame = _uiState.value.game
        val currentObjective = currentGame.objective
        val newObjective = currentObjective.copy(goal = newGoal)
        val newGame = currentGame.copy(objective = newObjective)
        updateUiState(newGame)
    }

    private fun setRoundObjectiveGoal(goal: Int?) {
        val currentGame = _uiState.value.game
        val currentRoundObjective = currentGame.roundObjective
        val newRoundObjective = currentRoundObjective.copy(goal = goal)
        val newGame = currentGame.copy(roundObjective = newRoundObjective)
        updateUiState(newGame)
    }

    private fun setRoundObjectiveDisplayValue(displayValue: String?) {
        val currentGame = _uiState.value.game
        val currentRoundObjective = currentGame.roundObjective
        val newRoundObjective =
            currentRoundObjective.copy(displayValue = displayValue?.trimToNull())
        val newGame = currentGame.copy(roundObjective = newRoundObjective)
        updateUiState(newGame)
    }

    private fun setRoundObjectiveDisplayColor(newColor: Game.Colors.Color) {
        val currentGame = _uiState.value.game
        val currentRoundObjective = currentGame.roundObjective
        val newRoundObjective = currentRoundObjective.copy(displayColor = newColor)
        val newGame = currentGame.copy(roundObjective = newRoundObjective)
        updateUiState(newGame)
    }

    private fun setDisplayNegative(newColor: Game.Colors.Color) {
        val currentGame = _uiState.value.game
        val currentColors = currentGame.color
        val newColors = currentColors.copy(negativeScore = newColor)
        val newGame = currentGame.copy(color = newColors)
        updateUiState(newGame)
    }

    private fun setDisplayPositive(newColor: Game.Colors.Color) {
        val currentGame = _uiState.value.game
        val currentColors = currentGame.color
        val newColors = currentColors.copy(positiveScore = newColor)
        val newGame = currentGame.copy(color = newColors)
        updateUiState(newGame)
    }

    private fun updateUiState(newGame: Game) {
        val isValidName = newGame.name.isNotBlank()
        _uiState.update { currentState ->
            currentState.copy(
                game = newGame,
                isValidName = isValidName
            )
        }
    }

    private fun newGame() {
        _uiState.value = GameSetupUiState()
    }
}