package net.hawkins.gamescore.ui

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import net.hawkins.gamescore.data.Player
import net.hawkins.gamescore.game.BasicScore
import net.hawkins.gamescore.game.GameType
import net.hawkins.gamescore.game.TwentyFiveHundred

data class TopAppBar(
    val title: String = "",
    val actions: @Composable (RowScope.() -> Unit)? = null
)

class GameViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(GameUiState())

    val gameTypes = listOf(
        TwentyFiveHundred(),
        BasicScore()
    )
    private var _gameType = mutableStateOf(gameTypes[0])

    private val _topAppBar = mutableStateOf(TopAppBar())
    val topAppBar: State<TopAppBar> = _topAppBar

    fun updateTopAppBar(newTitle: String = "", newActions: @Composable (RowScope.() -> Unit)?) {
        _topAppBar.value = TopAppBar(title = newTitle, actions = newActions)
    }

    fun resetGame() {
        _uiState.value.winner.value = null
        _uiState.value.players.forEach { player -> player.resetScores() }
    }

    fun getWinner(): Player? {
        return _uiState.value.winner.value
    }

    fun determineWinner(): Player? {
        _uiState.value.winner.value = _gameType.value.findWinner(_uiState.value.players)
        return _uiState.value.winner.value
    }

    fun isValidScore(score: String): Boolean {
        return _gameType.value.isValidScore(score)
    }

    fun highlightNegativeScore(): Boolean {
        return _gameType.value.highlightNegativeScore()
    }

    fun hasWinningThreshold(): Boolean {
        return _gameType.value.hasWinningThreshold()
    }

    fun getGameType(): GameType {
        return _gameType.value
    }

    fun setGameType(gameType: GameType) {
        _gameType.value = gameType
    }

    fun setGameType(name: String) {
        _gameType.value = gameTypes.first { gameType -> gameType.getName() == name }
    }

    fun setPlayers(names: List<String>) {
        _uiState.value.players.clear()
        names.forEach { name -> _uiState.value.players.add(Player(name)) }
    }

    fun getPlayers(): SnapshotStateList<Player> {
        return _uiState.value.players
    }
}