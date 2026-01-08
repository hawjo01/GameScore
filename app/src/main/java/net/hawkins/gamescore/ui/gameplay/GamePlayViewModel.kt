package net.hawkins.gamescore.ui.gameplay

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import net.hawkins.gamescore.data.FavoriteGameRepository
import net.hawkins.gamescore.data.GameProgressRepository
import net.hawkins.gamescore.data.GameRepository
import net.hawkins.gamescore.data.model.FavoriteGame
import net.hawkins.gamescore.data.model.Game
import net.hawkins.gamescore.service.GamePlayService
import net.hawkins.gamescore.service.GameProgressService
import net.hawkins.gamescore.ui.AbstractViewModel
import net.hawkins.gamescore.utils.removeElementAtIndex
import net.hawkins.gamescore.utils.replaceElementAtIndex
import javax.inject.Inject


@HiltViewModel
class GamePlayViewModel @Inject constructor(
    private val _favoriteGameRepository: FavoriteGameRepository,
    private val _gameRepository: GameRepository,
    gameProgressRepository: GameProgressRepository,
) : AbstractViewModel() {
    private val _uiState = MutableStateFlow(GamePlayUiState(Game(name = ""), emptyList()))
    val uiState: StateFlow<GamePlayUiState> = _uiState.asStateFlow()
    private val _gameProgressService: GameProgressService =
        GameProgressService(gameProgressRepository)
    private var _gamePlayService = GamePlayService(Game(name = ""))

    fun onEvent(event: GamePlayUiEvent) {
        when (event) {
            is GamePlayUiEvent.AddScore -> addScore(event.seatIndex, event.score)
            is GamePlayUiEvent.DeleteScore -> deleteScore(event.seatIndex, event.roundIndex)
            is GamePlayUiEvent.ChangeScore -> changeScore(
                event.seatIndex,
                event.roundIndex,
                event.newScore
            )
            is GamePlayUiEvent.SaveFavoriteGame -> saveFavoriteGame(event.name)
            is GamePlayUiEvent.StartGame -> startGame(event.game, event.playerNames)
            is GamePlayUiEvent.ResetGame -> resetGame()
            is GamePlayUiEvent.RefreshState -> refreshState()
            is GamePlayUiEvent.DetermineWinner -> determineWinner()
        }
    }

    private fun startGame(game: Game, playerNames: List<String>) {
        val players = playerNames.map { playerName -> Player(playerName) }
        _gamePlayService = GamePlayService(game)
        _uiState.update { currentState ->
            currentState.copy(game = game, players = players, winner = null)
        }
    }

    private fun saveFavoriteGame(name: String) {
        val favoriteGame = FavoriteGame(
            name = name.trim(),
            players = _uiState.value.players.map { player -> player.name },
            game = _uiState.value.game
        )
        _favoriteGameRepository.save(favoriteGame)
    }

    private fun resetGame() {
        val updatedPlayers = _uiState.value.players.map { player -> Player(player.name) }
        _uiState.update { currentState ->
            currentState.copy(
                players = updatedPlayers,
                winner = null
            )
        }
    }

    private fun addScore(seatIndex: Int, score: Int) {
        val player = _uiState.value.players[seatIndex]
        val newScore = _gamePlayService.buildScore(score)
        val updatedScores = player.scores.plus(newScore)
        val updatedPlayer = Player(player.name, updatedScores)
        updatePlayer(seatIndex, updatedPlayer)
    }

    private fun changeScore(seatIndex: Int, roundNumber: Int, newScore: Int) {
        val player = _uiState.value.players[seatIndex]
        val updatedScore = _gamePlayService.buildScore(newScore)
        val updatedScores = player.scores.replaceElementAtIndex(roundNumber, updatedScore)
        val updatedPlayer = Player(player.name, updatedScores)
        updatePlayer(seatIndex, updatedPlayer)
    }

    private fun deleteScore(seatIndex: Int, roundNumber: Int) {
        val player = _uiState.value.players[seatIndex]
        val updatedScores = player.scores.removeElementAtIndex(roundNumber)
        val updatedPlayer = Player(player.name, updatedScores)
        updatePlayer(seatIndex, updatedPlayer)
    }

    private fun updatePlayer(seatIndex: Int, updatedPlayer: Player) {
        val updatedPlayers = _uiState.value.players.replaceElementAtIndex(seatIndex, updatedPlayer)
        _uiState.update { currentState ->
            currentState.copy(players = updatedPlayers)
        }

        if (!_gamePlayService.isManualWinner()) {
            determineWinner()
        }
    }

    private fun determineWinner() {
        val winner = _gamePlayService.determineWinner(_uiState.value.players)
        _uiState.update { currentState ->
            currentState.copy(winner = winner)
        }
    }

    fun isValidScore(possibleScore: String): Boolean {
        return _gamePlayService.isValidScore(possibleScore)
    }

    fun saveGameProgress() {
        _gameProgressService.saveGameProgress(_uiState.value)
    }

    fun isGameInProgress(): Boolean {
        return _gameProgressService.isGameInProgress()
    }

    fun loadInProgressGame() {
        val gameProgress = _gameProgressService.getGameProgress()
        if (gameProgress != null) {
            refreshState(gameProgress.game, gameProgress.players)
        }
    }

    fun isManualWinner(): Boolean {
        return _gamePlayService.isManualWinner()
    }

    private fun refreshState() {
        val gameId = _uiState.value.game.id?: return
        val game = _gameRepository.getById(gameId)?: return

        refreshState(game, _uiState.value.players)
    }

    private fun refreshState(game: Game, players: List<Player>) {
        _gamePlayService = GamePlayService(game)

        val updatedPlayers = mutableListOf<Player>()
        for (player in players) {
            val updatedScores = mutableListOf<Score>()
            for (score in player.scores) {
                val updatedScore = _gamePlayService.buildScore(score.value)
                updatedScores.add(updatedScore)
            }
            val updatedPlayer = Player(player.name, updatedScores)
            updatedPlayers.add(updatedPlayer)
        }

        val updatedWinner = _gamePlayService.determineWinner(updatedPlayers)

        _uiState.update { currentState ->
            currentState.copy(
                game = game,
                players = updatedPlayers,
                winner = updatedWinner
            )
        }
    }
}