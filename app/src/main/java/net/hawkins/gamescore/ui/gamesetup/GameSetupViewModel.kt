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
        if (isNameValid(_uiState.value.gameName)) {
            _uiState.update { currentState ->
                currentState.copy(
                    isValidName = false
                )
            }
            return false
        }
        val savedGame = _gameRepository.save(_uiState.value.toGame())
        _uiState.value = savedGame.toGameSetupUiState(isNameValid(savedGame.name))
        return true
    }

    fun onEvent(event: GameSetupUiEvent) {
        when (event) {
            is GameSetupUiEvent.SetGameName -> onChangeGameName(event.name)
            is GameSetupUiEvent.SetConstraintPositiveOnlyScores -> onChangeConstraintPositiveOnly(event.positiveOnly)
            is GameSetupUiEvent.SetConstraintEqualHandSizes -> onChangeConstraintEqualHandSizes(event.requireEqualHandSizes)
            is GameSetupUiEvent.SetConstraintScoreModulus -> onChangeConstraintScoreModulus(event.modulus)
            is GameSetupUiEvent.SetObjectiveGoal -> onChangeObjectiveGoal(event.goal)
            is GameSetupUiEvent.SetObjectiveType -> onChangeObjectiveType(event.type)
            is GameSetupUiEvent.SetObjectiveRounds -> onChangeObjectiveRounds(event.rounds)
            is GameSetupUiEvent.SetRoundObjectiveGoal -> onChangeRoundObjectiveGoal(event.goal)
            is GameSetupUiEvent.SetRoundObjectiveDisplayValue -> onChangeRoundObjectiveDisplayValue(event.value)
            is GameSetupUiEvent.SetRoundObjectiveDisplayColor -> onChangeRoundObjectiveDisplayColor(event.color)
            is GameSetupUiEvent.SetDisplayNegativeColor -> onChangeColorDisplayNegative(event.color)
            is GameSetupUiEvent.SetDisplayPositiveColor -> onChangeColorDisplayPositive(event.color)
            is GameSetupUiEvent.NewGame -> onClearGame()
            is GameSetupUiEvent.SetGame -> onChangeGame(event.game)
        }
    }

    private fun onChangeGameName(newName: String) {
        val isNameValid = isNameValid(newName)
        _uiState.update { currentState ->
            currentState.copy(
                gameName = newName,
                isValidName = isNameValid
            )
        }
    }

    private fun onChangeConstraintPositiveOnly(newState: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                gameConstraintPositiveOnly = newState
            )
        }
    }

    private fun onChangeConstraintEqualHandSizes(newState: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                gameConstraintEqualHandSizes = newState
            )
        }
    }

    private fun onChangeConstraintScoreModulus(newModulus: Int?) {
        _uiState.update { currentState ->
            currentState.copy(
                gameConstraintScoreModulus = newModulus
            )
        }
    }

    private fun onChangeObjectiveType(newType: Game.Objective.Type) {
        _uiState.update { currentState ->
            currentState.copy(
                gameObjectiveType = newType
            )
        }
    }

    private fun onChangeObjectiveRounds(newRounds: Int?) {
        _uiState.update { currentState ->
            currentState.copy(
                gameObjectiveRounds = newRounds
            )
        }
    }

    private fun onChangeObjectiveGoal(newGoal: Int?) {
        _uiState.update { currentState ->
            currentState.copy(
                gameObjectiveGoal = newGoal
            )
        }
    }

    private fun onChangeRoundObjectiveGoal(newGoal: Int?) {
        _uiState.update { currentState ->
            currentState.copy(
                gameRoundObjectiveGoal = newGoal
            )
        }
    }

    private fun onChangeRoundObjectiveDisplayValue(newDisplayValue: String?) {
        _uiState.update { currentState ->
            currentState.copy(
                gameRoundObjectiveDisplayValue = newDisplayValue?.trimToNull()
            )
        }
    }

    private fun onChangeRoundObjectiveDisplayColor(newColor: Game.Colors.Color) {
        _uiState.update { currentState ->
            currentState.copy(
                gameRoundObjectiveDisplayColor = newColor
            )
        }
    }

    private fun onChangeColorDisplayNegative(newColor: Game.Colors.Color) {
        _uiState.update { currentState ->
            currentState.copy(
                gameColorsNegativeScore = newColor
            )
        }
    }

    private fun onChangeColorDisplayPositive(newColor: Game.Colors.Color) {
        _uiState.update { currentState ->
            currentState.copy(
                gameColorsPositiveScore = newColor
            )
        }
    }

    private fun onChangeGame(newGame: Game) {
        val isValidName = isNameValid(newGame.name)
        _uiState.value = newGame.toGameSetupUiState(isNameValid = isValidName)
    }

    private fun onClearGame() {
        _uiState.value = GameSetupUiState()
    }

    private fun isNameValid(name: String): Boolean {
        return name.isNotBlank()
    }
}