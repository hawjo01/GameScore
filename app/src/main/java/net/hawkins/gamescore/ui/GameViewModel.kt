package net.hawkins.gamescore.ui

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import net.hawkins.gamescore.game.Game

data class TopAppBar(
    val title: String = "",
    val actions: @Composable (RowScope.() -> Unit)? = null
)

class GameViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private val _topAppBar = mutableStateOf(TopAppBar())
    val topAppBar: State<TopAppBar> = _topAppBar

    fun updateTopAppBar(newTitle: String = "", newActions: @Composable (RowScope.() -> Unit)?) {
        _topAppBar.value = TopAppBar(title = newTitle, actions = newActions)
    }

    fun setGame(newGame: Game) {
        _uiState.update { currentState ->
            currentState.copy(game = newGame)
        }
    }
}