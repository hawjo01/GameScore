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

data class AppBarActions(
    val actions: @Composable (RowScope.() -> Unit)? = null
)

class GameViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(GameUiState())

    private val _gameTypes = listOf(
        TwentyFiveHundred(),
        BasicScore()
    )
    private var _gameType = mutableStateOf(_gameTypes[0])
    private var _winner = mutableStateOf<Player?>(null)

    private val _appBarActions = mutableStateOf(AppBarActions())
    val appBarActions: State<AppBarActions> = _appBarActions

    fun updateAppBarActions(newActions: @Composable (RowScope.() -> Unit)?) {
        _appBarActions.value = AppBarActions(newActions)
    }

    fun resetGame() {
        _winner.value = null
        _uiState.value.players.forEach { player -> player.resetScores() }
    }

    fun getWinner(): Player? {
        return _winner.value
    }

    fun determineWinner(): Player? {
        _winner.value = _gameType.value.findWinner(_uiState.value.players)
        return _winner.value
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

    fun getPlayers(): SnapshotStateList<Player> {
        return _uiState.value.players
    }

    fun addPlayer(playerName: String) {
        _uiState.value.players.add(Player(playerName))
    }

    fun removePlayer(index: Int) {
        _uiState.value.players.removeAt(index)
    }

    fun getGameTypes(): List<GameType> {
        return _gameTypes
    }

    fun getGameType(): GameType {
        return _gameType.value
    }

    fun setGameType(gameType: GameType) {
        _gameType.value = gameType
    }
}