package net.hawkins.gamescore.ui

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import net.hawkins.gamescore.data.Player
import net.hawkins.gamescore.game.BasicScore
import net.hawkins.gamescore.game.GameType
import net.hawkins.gamescore.game.TwentyFiveHundred

class GameViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()
    private val _savedPlayerLists = mutableListOf<List<String>>()
    private val _gameTypes = listOf(
        TwentyFiveHundred(),
        BasicScore()
    )
    private var _gameType = mutableStateOf(_gameTypes[0])
    private var _winner = mutableStateOf<Player?>(null)


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

    fun getScoreColor(score: Int): Color {
        return _gameType.value.getScoreColor(score)
    }

    fun hasWinningThreshold(): Boolean {
        return _gameType.value.hasWinningThreshold()
    }

    fun getPlayers(): SnapshotStateList<Player> {
        return _uiState.value.players
    }

    fun playersReady(): Boolean {
        return _uiState.value.players.isNotEmpty()
    }

    fun addPlayerName(playerName: String) {
        _uiState.value.playerNames.add(playerName)
    }

    fun removePlayerName(playerName: String) {
        _uiState.value.playerNames.remove(playerName)
    }

    fun addPlayerNames(playerNames: List<String>) {
        _uiState.value.playerNames.addAll(playerNames)
    }

    fun getPlayerNames(): List<String> {
        return _uiState.value.playerNames
    }

    fun startGame() {
        val players = _uiState.value.playerNames.map { playerName -> Player(playerName) }
        _uiState.value.playerNames.clear()
        _uiState.value.players.addAll(players)
    }

    fun getSavedPlayerNameLists(): List<List<String>> {
        return _savedPlayerLists
    }

    fun setSavedPlayerNameLists(savedLists: List<List<String>>) {
        savedLists.forEach { _savedPlayerLists.add(it) }
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

    fun movePlayer(fromIndex: Int, toIndex: Int) {
        _uiState.value.players.apply { add(toIndex,_uiState.value.players.removeAt(fromIndex) ) }
//        val playerToMove = _uiState.value.players.get(fromIndex)
//        _uiState.value.players.removeAt(fromIndex)
//        _uiState.value.players.add(toIndex, playerToMove)
        val p = _uiState.value.players
        p.forEachIndexed { i, player -> println("Player " + i + " = " + player.name) }
    }
}